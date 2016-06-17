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
	//下载任务执行完毕后发送的message的what
	public static final int LOAD_FINISH = 101;
	public static final int NEW_TASK = 102;
	
	//线程池中线程的数量
	//(这个数量应该与设备cpu的核数相同)
	public static int Thread_Count;
	public static Context c;//上下文
	//内存缓存
	public static LruCache<String, Bitmap> MemCache;
	//(双向)任务队列
	//未来可以根据需求选择是FIFO还是LIFO
	public static LinkedBlockingDeque<Runnable> taskQueue;
	//线程池
	public static ExecutorService exec;
	//从任务队列中取下载任务的handler
	public static Handler pollHandler;
	//任务下载完毕后，用来刷新ListView条目显示下载图片的Handler
	public static Handler uiHandler;
	//"养活"pollHandler
	public static Thread pollThread;
	//磁盘缓存
	public static DiskLruCache DiskCache;
	
	//标示的作用，用来确保ImageLoader只要进行一次
	//初始化操作即可
	public static boolean isFirstTime = true;
	
	
	
	
	/**
	 * 初始化ImageLoader所有属性的值
	 * @param context
	 */
	public static void init(Context context){
		
		if(!isFirstTime){
			return;
		}
		
		//c属性赋值
		c = context;
		//获取cpu的核数为Thread_Count赋值
		Thread_Count = getNumberOfCores();
		//初始化内存缓存
		MemCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getHeight()*value.getRowBytes();
			}
			
		};
		
		//初始化磁盘缓存
		try {
			DiskCache = DiskLruCache.open(directory(), appVersion(), 1, 1024*1024*10);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//初始化任务队列
		taskQueue = new LinkedBlockingDeque<Runnable>();
		exec = Executors.newFixedThreadPool(Thread_Count);
		//pollHandler, uiHandler
		uiHandler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				//利用线程池中执行完毕的内容，来刷新ListView
				if(msg.what==LOAD_FINISH){
					//msg.obj到底放什么？
					//msg.obj放什么能解决ListView加载“动画”效果
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
							//现在任务队列中被放入了新“下载任务”
							//去队列中取任务
							try {
								Runnable task = taskQueue.takeFirst();
								//将取出的任务放到线程池中执行
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
		//有些环境在执行getExternalCacheDir获取
		//外部的缓存路径的时候可能会报空指针异常
		//所以这里采用了内部缓存路径
		String path = c.getCacheDir().getPath();
		return new File(path,"imageloadercache");
	}
	/**
	 * 获取url指向的图像，放到iv中显示
	 * @param url 图像路径
	 * @param iv 显示图像的ImageView
	 */
	public static void loadImage(final String url,final ImageView iv){
		if(isFirstTime){
			throw new RuntimeException("ImageLoader未做初始化");
		}
		
		//先判断，url所指向的图像是否在缓存中有保存
		
		//url转为MD5格式的字符串
		
		final String md5Url = getMD5(url);
		//设置TAG，到时候要与vo中的URL进行比对！
		iv.setTag(md5Url);
		Bitmap bitmap = MemCache.get(md5Url);
		if(bitmap!=null){
			Log.d("TAG","图像是从内存缓存中加载的");
			iv.setImageBitmap(bitmap);
			return;
		}
		
		try {
			//从磁盘缓存中试图取出url所对应的图片
			Snapshot snap = DiskCache.get(md5Url);
			if(snap!=null){
				Log.d("TAG","图像是从磁盘缓存中获取的");
				InputStream in = snap.getInputStream(0);
				bitmap = BitmapFactory.decodeStream(in);
				//将从磁盘缓存中获得的图片放到内存缓存存储一份
				MemCache.put(md5Url, bitmap);
				iv.setImageBitmap(bitmap);
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//从网络中下载		
		taskQueue.add(new Runnable() {
			
			@Override
			public void run() {
				//下载任务
				//发起网络连接，获得图像资源
				try{
					URL u = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) u.openConnection();
					connection.setRequestMethod("GET");
					connection.setDoInput(true);
					connection.connect();
					InputStream is = connection.getInputStream();
					//要对图像进行压缩
					Bitmap bitmap = compress(is,iv);
					is.close();
					//将压缩后的图像放到内存缓存中存储
					MemCache.put(md5Url, bitmap);
					//将压缩后的图像放到磁盘缓存中存储
					Editor editor = DiskCache.edit(md5Url);
					OutputStream os = editor.newOutputStream(0);
					bitmap.compress(CompressFormat.JPEG, 100, os);
					editor.commit();
					//写日志(可选操作)
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
	 * 根据要显示的ImageView的大小对图像进行压缩
	 * @param is 图像源
	 * @param iv 未来要显示图像的ImageView
	 * @return 压缩过后的图像
	 */
	
	protected static Bitmap compress(InputStream is, ImageView iv) {
		Bitmap bitmap = null;
		try{
			
			//1)先获得原始图像(图像源)的尺寸大小
			//借助Optioins来获取图像的大小
			//is----------byte[]
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buff = new byte[4096];
			int len = -1;
			while((len=is.read(buff))!=-1){
				out.write(buff, 0, len);
			}
			
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			//opts一旦设定了inJustDecodeBounds = true
			//BitmapFactory是不会返回bitmap对象，只会null
			BitmapFactory.decodeByteArray(out.toByteArray(),0, out.toByteArray().length, opts);
			int width = opts.outWidth;//图像的宽度(像素数)
			int height = opts.outHeight;//图像高度(像素数)
			//2)获取希望的宽度/高度
			int targetWidth  = iv.getWidth();//ImageView的宽度
			int targetHeight = iv.getHeight();//Imageview的高度
			//如果取ImageView宽度/高度取不到
			if(targetHeight==0||targetWidth==0){
				//可以手动指定值(80dp,100dp)
				//以当前设备屏幕的宽度/和高度来作为targetWiddth/targetHeight
				targetWidth = c.getResources().
						      getDisplayMetrics().
						      widthPixels;
				targetHeight = c.getResources().
						      getDisplayMetrics().
						      heightPixels;
			}
			
			//3)计算压缩比
			int sampleSize = 1;
			
			if(width*1.0/targetWidth>1||height*1.0/targetHeight>1){
				sampleSize = (int) Math.ceil(
						Math.max(width*1.0/targetWidth, 
								 height*1.0/targetHeight));
			}
			
			//4)压缩图像
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
		ImageView iv;//发起下载任务的那个ImageView
		String url;//下载任务中要下载的那个地址
		Bitmap bitmap;//url所对应的图片
		public ValueObject(ImageView iv, String url, Bitmap bitmap) {
			super();
			this.iv = iv;
			this.url = url;
			this.bitmap = bitmap;
		}
	}
}
