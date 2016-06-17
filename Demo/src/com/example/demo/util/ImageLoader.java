package com.example.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.example.demo.util.DiskLruCache.Editor;
import com.example.demo.util.DiskLruCache.Snapshot;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	//��������ִ����Ϻ��͵�message��what
	public static final int LOAD_FINISH = 101;
	public static final int NEW_TASK = 102;
	
	//�̳߳����̵߳�����
	//(�������Ӧ�����豸cpu�ĺ�����ͬ)
	public static int Thread_Count;
	public static Context c;//������
	//�ڴ滺��
	public static LruCache<String, Bitmap> MemCache;
	//(˫��)�������
	//δ�����Ը�������ѡ����FIFO����LIFO
	public static LinkedBlockingDeque<Runnable> taskQueue;
	//�̳߳�
	public static ExecutorService exec;
	//�����������ȡ���������handler
	public static Handler pollHandler;
	//����������Ϻ�����ˢ��ListView��Ŀ��ʾ����ͼƬ��Handler
	public static Handler uiHandler;
	//"����"pollHandler
	public static Thread pollThread;
	//���̻���
	public static DiskLruCache DiskCache;
	
	//��ʾ�����ã�����ȷ��ImageLoaderֻҪ����һ��
	//��ʼ����������
	public static boolean isFirstTime = true;
	
	
	
	
	/**
	 * ��ʼ��ImageLoader�������Ե�ֵ
	 * @param context
	 */
	public static void init(Context context){
		
		if(!isFirstTime){
			return;
		}
		
		//c���Ը�ֵ
		c = context;
		//��ȡcpu�ĺ���ΪThread_Count��ֵ
		Thread_Count = getNumberOfCores();
		//��ʼ���ڴ滺��
		MemCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getHeight()*value.getRowBytes();
			}
			
		};
		
		//��ʼ�����̻���
		try {
			DiskCache = DiskLruCache.open(directory(), appVersion(), 1, 1024*1024*10);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//��ʼ���������
		taskQueue = new LinkedBlockingDeque<Runnable>();
		exec = Executors.newFixedThreadPool(Thread_Count);
		//pollHandler, uiHandler
		uiHandler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				//�����̳߳���ִ����ϵ����ݣ���ˢ��ListView
				if(msg.what==LOAD_FINISH){
					//msg.obj���׷�ʲô��
					//msg.obj��ʲô�ܽ��ListView���ء�������Ч��
					ValueObject vo = (ValueObject) msg.obj;
					ImageView iv = vo.iv;
					String url = vo.url;
					Bitmap bitmap = vo.bitmap;
					if(iv.getTag().toString().equals(url)){
						iv.setImageBitmap(bitmap);
					}
					
				}else{
					super.handleMessage(msg);
				}
			}
		};
		
		pollThread = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				
				pollHandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						if(msg.what==NEW_TASK){
							//������������б��������¡���������
							//ȥ������ȡ����
							try {
								Runnable task = taskQueue.takeFirst();
								//��ȡ��������ŵ��̳߳���ִ��
								exec.execute(task);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}else{
							super.handleMessage(msg);
						}
					}
					
				};
				
				
				Looper.loop();
			}
			
		};
		
		pollThread.start();
		
		isFirstTime = false;
		
	}
	private static int appVersion() {
		
		try {
			PackageInfo info = c.getPackageManager().
			getPackageInfo(c.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
	private static File directory() {
		//��Щ������ִ��getExternalCacheDir��ȡ
		//�ⲿ�Ļ���·����ʱ����ܻᱨ��ָ���쳣
		//��������������ڲ�����·��
		String path = c.getCacheDir().getPath();
		return new File(path,"imageloadercache");
	}
	/**
	 * ��ȡurlָ���ͼ�񣬷ŵ�iv����ʾ
	 * @param url ͼ��·��
	 * @param iv ��ʾͼ���ImageView
	 */
	public static void loadImage(final String url,final ImageView iv){
		if(isFirstTime){
			throw new RuntimeException("ImageLoaderδ����ʼ��");
		}
		
		//���жϣ�url��ָ���ͼ���Ƿ��ڻ������б���
		
		//urlתΪMD5��ʽ���ַ���
		
		final String md5Url = getMD5(url);
		//����TAG����ʱ��Ҫ��vo�е�URL���бȶԣ�
		iv.setTag(md5Url);
		Bitmap bitmap = MemCache.get(md5Url);
		if(bitmap!=null){
			Log.d("TAG","ͼ���Ǵ��ڴ滺���м��ص�");
			iv.setImageBitmap(bitmap);
			return;
		}
		
		try {
			//�Ӵ��̻�������ͼȡ��url����Ӧ��ͼƬ
			Snapshot snap = DiskCache.get(md5Url);
			if(snap!=null){
				Log.d("TAG","ͼ���ǴӴ��̻����л�ȡ��");
				InputStream in = snap.getInputStream(0);
				bitmap = BitmapFactory.decodeStream(in);
				//���Ӵ��̻����л�õ�ͼƬ�ŵ��ڴ滺��洢һ��
				MemCache.put(md5Url, bitmap);
				iv.setImageBitmap(bitmap);
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//������������		
		taskQueue.add(new Runnable() {
			
			@Override
			public void run() {
				//��������
				//�����������ӣ����ͼ����Դ
				try{
					URL u = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) u.openConnection();
					connection.setRequestMethod("GET");
					connection.setDoInput(true);
					connection.connect();
					InputStream is = connection.getInputStream();
					//Ҫ��ͼ�����ѹ��
					Bitmap bitmap = compress(is,iv);
					is.close();
					//��ѹ�����ͼ��ŵ��ڴ滺���д洢
					MemCache.put(md5Url, bitmap);
					//��ѹ�����ͼ��ŵ����̻����д洢
					Editor editor = DiskCache.edit(md5Url);
					OutputStream os = editor.newOutputStream(0);
					bitmap.compress(CompressFormat.JPEG, 100, os);
					editor.commit();
					//д��־(��ѡ����)
					DiskCache.flush();
					
					ValueObject vo = new ValueObject(iv, md5Url, bitmap);
					Message.obtain(uiHandler, LOAD_FINISH, vo).sendToTarget();
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		Message.obtain(pollHandler, NEW_TASK).sendToTarget();
	}
	
	/**
	 * ����Ҫ��ʾ��ImageView�Ĵ�С��ͼ�����ѹ��
	 * @param is ͼ��Դ
	 * @param iv δ��Ҫ��ʾͼ���ImageView
	 * @return ѹ�������ͼ��
	 */
	
	protected static Bitmap compress(InputStream is, ImageView iv) {
		Bitmap bitmap = null;
		try{
			
			//1)�Ȼ��ԭʼͼ��(ͼ��Դ)�ĳߴ��С
			//����Optioins����ȡͼ��Ĵ�С
			//is----------byte[]
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buff = new byte[4096];
			int len = -1;
			while((len=is.read(buff))!=-1){
				out.write(buff, 0, len);
			}
			
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			//optsһ���趨��inJustDecodeBounds = true
			//BitmapFactory�ǲ��᷵��bitmap����ֻ��null
			BitmapFactory.decodeByteArray(out.toByteArray(),0, out.toByteArray().length, opts);
			int width = opts.outWidth;//ͼ��Ŀ��(������)
			int height = opts.outHeight;//ͼ��߶�(������)
			//2)��ȡϣ���Ŀ��/�߶�
			int targetWidth  = iv.getWidth();//ImageView�Ŀ��
			int targetHeight = iv.getHeight();//Imageview�ĸ߶�
			//���ȡImageView���/�߶�ȡ����
			if(targetHeight==0||targetWidth==0){
				//�����ֶ�ָ��ֵ(80dp,100dp)
				//�Ե�ǰ�豸��Ļ�Ŀ��/�͸߶�����ΪtargetWiddth/targetHeight
				targetWidth = c.getResources().
						      getDisplayMetrics().
						      widthPixels;
				targetHeight = c.getResources().
						      getDisplayMetrics().
						      heightPixels;
			}
			
			//3)����ѹ����
			int sampleSize = 1;
			
			if(width*1.0/targetWidth>1||height*1.0/targetHeight>1){
				sampleSize = (int) Math.ceil(
						Math.max(width*1.0/targetWidth, 
								 height*1.0/targetHeight));
			}
			
			//4)ѹ��ͼ��
			opts.inSampleSize = sampleSize;
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.toByteArray().length,opts);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}
	private static String getMD5(String url) {
		String result="";
		
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			md.update(url.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for(byte b:bytes){
				String str = Integer.toHexString(b & 0xFF);
				if(str.length()==1){
					sb.append("0");
				}
				sb.append(str);
			}
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	private static int getNumberOfCores() {
		File file = new File("/sys/devices/system/cpu/");
		File[] files = file.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				
				if(filename.matches("cpu[0-9]")){
					return true;
				}else{
					return false;
				}
			}
		});
		
		if(files.length>0){
			return files.length;
		}else{
			return 1;
		}
		
	}
	
	private static class ValueObject{
		ImageView iv;//��������������Ǹ�ImageView
		String url;//����������Ҫ���ص��Ǹ���ַ
		Bitmap bitmap;//url����Ӧ��ͼƬ
		public ValueObject(ImageView iv, String url, Bitmap bitmap) {
			super();
			this.iv = iv;
			this.url = url;
			this.bitmap = bitmap;
		}
	}
}
