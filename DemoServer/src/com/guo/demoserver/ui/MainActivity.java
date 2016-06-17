package com.guo.demoserver.ui;

import java.io.ObjectStreamException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.guo.demoserver.R;
import com.guo.demoserver.R.id;
import com.guo.demoserver.R.layout;
import com.guo.demoserver.R.menu;
import com.guo.demoserver.adapter.OrderAdapter;
import com.guo.demoserver.bean.Answer;
import com.guo.demoserver.bean.MyBmobInstallation;
import com.guo.demoserver.bean.Order;
import com.guo.demoserver.bean.Time;
import com.guo.demoserver.listener.OnGetTimeLoadFinishListener;
import com.guo.demoserver.util.DateUtil;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity{
	ListView listView;
	View handerView;
	Order order;
	List<Order> list;
	OrderAdapter adapter;
	Time time = new Time();
	
	private OnGetTimeLoadFinishListener listener;
	
	//����
	BmobPushManager<BmobInstallation> bmobPush;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.order_list);
		handerView = findViewById(R.id.headerview);
		setHeaderTitle(handerView, "����Ķ���");
		setHeaderImage(handerView, R.drawable.mycenter, Position.RIGHT, new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,PersonActivity.class));
			}
		});
		
		
		
		//����debug����󣬿�֪��push�����Ƿ���������������
		BmobPush.setDebugMode(true);
		//�������ͷ���
		BmobPush.startWork(this);	
		bmobPush = new BmobPushManager<BmobInstallation>(this);
		
		/***���ݱ���¼���****/
		final BmobRealTimeData rtd = new BmobRealTimeData();
		rtd.start(this, new ValueEventListener() {
			
			@Override
			public void onDataChange(JSONObject arg0) {
				if(BmobRealTimeData.ACTION_UPDATETABLE.equals(arg0.optString("action"))){
					JSONObject data = arg0.optJSONObject("data");
					Log.d("TAG", "("+data.optString("name")+")"+"���ݣ�"+data);
					
//					order.add("name", data.opt("name").toString());
//					order.add("price", data.opt("price").toString());
//					order.add("phone", data.opt("phone").toString());
//					order.add("address", data.opt("address").toString());
//					list.add(order);
					
					//����һ��֪ͨ��Ϣ
					sendNotification(MainActivity.this, "�����µĶ���", data.toString());
					chaxun();
					adapter.notifyDataSetChanged();
					
					
				}
			}
			
			@Override
			public void onConnectCompleted() {
				if(rtd.isConnected()){
					rtd.subTableUpdate("Order");
				}
				Log.d("TAG", "���Ǽ�����**���ӳɹ�:"+rtd.isConnected());

			}
		});
		
		/**��ѯ������ʱ��**/
		chaxun();
		
		
		//�����Ŀ���򿪶Ի���
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//����һ�����棬��ʾ������Ķ�������ϸ����Ϣ
				//������Ķ�����Ϣȡ�����������ͻ�ܾ�����
//				List<Order> list2 = (List<Order>) adapter.getItem(position);
				final Order orders = (Order) adapter.getItem(position);
				
				/**�Զ���һ���Ի���**/
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				final AlertDialog dialog = builder.create();
				dialog.show();
				Window window = dialog.getWindow();
				LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				View v = inflater.inflate(R.layout.detail2_layout, null);
				window.setContentView(v);
				
				Button yes = (Button) findViewById(R.id.detail_yes);
				Button no = (Button) findViewById(R.id.detail_no);
				
//				yes.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						//��������û���Ϊ��������ݣ����û����޸�����
//						BmobQuery<Answer> query = new BmobQuery<Answer>();
//						query.addWhereEqualTo("answername", orders.getPhone());
//						query.findObjects(MainActivity.this, new FindListener<Answer>() {
//
//							@Override
//							public void onError(int arg0, String arg1) {
//								Toast.makeText(MainActivity.this, "Ϊ���û��������", 1).show();
//								
//								Answer answer = new Answer();
//								answer.setAnswername(orders.getPhone());
//								answer.setContent("����ѽӵ�");
//								answer.save(MainActivity.this, new SaveListener() {
//									@Override
//									public void onSuccess() {
//										Toast.makeText(MainActivity.this, "�ɹ�Ϊ�������", 1).show();
//									}
//									
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "�������ʧ�ܣ�"+arg0, 1).show();
//									}
//								});
//							}
//
//							@Override
//							public void onSuccess(List<Answer> arg0) {
//
//								Answer answer = new Answer();
//								answer.setContent("����ѽӵ�");
//								answer.update(MainActivity.this, getId(arg0), new UpdateListener() {
//									
//									
//									@Override
//									public void onSuccess() {
//										Toast.makeText(MainActivity.this, "�������ݳɹ���", 1).show();
//									}
//									
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "��������ʧ�ܣ�"+arg1, 1).show();
//									}
//								});
//							}
//
//							private String getId(List<Answer> arg0) {
//								String oid = null;
//								 for (Answer Answer : arg0) {
//						               //������ݵ�objectId��Ϣ
//									 	oid = Answer.getObjectId();
//						            }
//								return oid;
//							}
//						});
//					}
//				});
//				
//				
//				//���ӵ������
//				no.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						final String a =  et.getText().toString();
//						if(TextUtils.isEmpty(a)){
//							Toast.makeText(MainActivity.this, "���ɲ���Ϊ��", 1).show();
//							return;
//						}
//						BmobQuery<Answer> query2 = new BmobQuery<Answer>();
//						query2.addWhereEqualTo("answername", orders.getPhone());
//						query2.findObjects(MainActivity.this, new FindListener<Answer>() {
//
//							@Override
//							public void onError(int arg0, String arg1) {
//								Toast.makeText(MainActivity.this, "Ϊ���û��������", 1).show();
//
//								Answer answer = new Answer();
//								answer.setAnswername(orders.getPhone());
//								answer.setContent(a);
//								answer.save(MainActivity.this, new SaveListener() {
//									@Override
//									public void onSuccess() {
//										Toast.makeText(MainActivity.this, "�ɹ�Ϊ�������", 1).show();
//									}
//
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "�������ʧ�ܣ�"+arg0, 1).show();
//									}
//								});
//							}
//
//							@Override
//							public void onSuccess(List<Answer> arg0) {
//								Answer answer = new Answer();
//								answer.setContent(a);
//								answer.update(MainActivity.this, getId(arg0), new UpdateListener() {
//									
//									
//									@Override
//									public void onSuccess() {
//										Toast.makeText(MainActivity.this, "�������ݳɹ���", 1).show();
//									}
//									
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "��������ʧ�ܣ�"+arg1, 1).show();
//									}
//								});
//							}
//
//							private String getId(List<Answer> arg0) {
//								String oid = null;
//								for (Answer Answer : arg0) {
//						               //������ݵ�objectId��Ϣ
//									 	oid = Answer.getObjectId();
//						            }
//								return oid;
//							}
//						});
//						
//					}
//				});
				
			}
		});
		
		BmobInstallation.getCurrentInstallation(this).save();
		
	}


	
	/**�����Զ����BmobInstallation�ֶ�  
	 * @return void  
	 * @exception   
	 */
	public void updateBmobInstallation(){
		BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
		query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(this));
		query.findObjects(this, new FindListener<MyBmobInstallation>() {
			
			@Override
			public void onSuccess(List<MyBmobInstallation> object) {
				// TODO Auto-generated method stub
				if(object.size() > 0){
					MyBmobInstallation mbi = object.get(0);
					mbi.setUid("uid");
					mbi.update(MainActivity.this,new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Log.i("bmob", "���³ɹ�");
						}
						
						@Override
						public void onFailure(int code, String msg) {
							// TODO Auto-generated method stub
							Log.i("bmob", "����ʧ�ܣ�"+msg);
						}
					});
				}else{
				}
			}
			
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.detail_yes:
//			yes("����ѽӵ�", "5909E8A029957C1DC8FFAA9D4E871AA2");
			yes("����ѽӵ�", order.getInstal());
			break;

		/*case R.id.detail_no:
			
			no("��Ҿܾ�����Ķ���", "5909E8A029957C1DC8FFAA9D4E871AA2");
			break;
		}*/
		case R.id.detail_no:

//			no("��Ҿܾ�����Ķ���", BmobInstallation.getCurrentInstallation(this).getInstallationId(this));
			no("��Ҿܾ�����Ķ���", order.getInstal());
			break;
		}
	
}
	

	@Override
	protected void onResume() {
		super.onResume();
		
	}


	private void chaxun() {
		getTime(new OnGetTimeLoadFinishListener() {
			
			@Override
			public void OnGetTimeLoadFinish(String times) {
				String cc = times;
//				time.setMm(times);
				//��ѯ����
				getInformation(cc);
			}
		});
	}
	


	



	



	private void getInformation(String cc) {
		
		BmobQuery<Order> query = new BmobQuery<Order>();
		List<BmobQuery<Order>> and = new ArrayList<BmobQuery<Order>>();
		BmobQuery<Order> q1 = new BmobQuery<Order>();
//		String start = time.getMm()+" "+"00:00:00";
		String start = cc+" "+"00:00:00";
		Log.i("TAG","startΪ:" + start);
//		String start = "2015-05-01 00:00:00";  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date  = null;
		try {
		date = sdf.parse(start);
		} catch (ParseException e) {
		e.printStackTrace();
		}  
		
		q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
		and.add(q1);
		//С��23��59��59
		BmobQuery<Order> q2 = new BmobQuery<Order>();
//		String end = time.getMm()+" "+"23:59:59";
		String end = cc+" "+"23:59:59";
		Log.i("TAG","endΪ:" + end);
//		String end = "2015-05-01 23:59:59"; 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date1  = null;
		try {
		date1 = sdf1.parse(end);
		} catch (ParseException e) {
		e.printStackTrace();
		}  
		q2.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date1));
		and.add(q2);
		//��Ӹ������ѯ
		query.and(and);
		// ����createdAt�ֶ�������ʾ����
//		query.order("createdAt");
		// ����createdAt�ֶν�����ʾ����
		query.order("-createdAt");
		query.findObjects(MainActivity.this, new FindListener<Order>() {

			@Override
			public void onError(int arg0, String arg1) {
				Log.i("TAG", "��ѯʧ�ܣ�"+arg1);
				Toast.makeText(MainActivity.this, "��ѯʧ�ܣ�"+arg1, 1).show();
			}

			@Override
			public void onSuccess(List<Order> arg0) {
				Log.i("TAG", "��ѯ�ɹ���"+arg0);
				order = new Order();
				list = new ArrayList<Order>();
				
				for (Order Order : arg0) {
					order.setName(Order.getName());
					order.setPrice(Order.getPrice());
					order.setPhone(Order.getPhone());
					order.setAddress(Order.getAddress());
					order.setInstal(Order.getInstal());
					time.setMm(Order.getCreatedAt());
					
					list.add(Order);
              }
				
				Log.d("TAG", "��ѯ����order��"+list.toString());
				//Toast.makeText(MainActivity.this, "��ѯ�ɹ���"+arg0, 1).show();
				
//				Collections.sort(arg0, new Comparator<Order>() {
//
//					@Override
//					public int compare(Order arg0, Order arg1) {
//						Date date1 = DateUtil.stringToDate(arg0.getCreatedAt());  
//		                Date date2 = DateUtil.stringToDate(arg1.getCreatedAt()); 
//		                // �������ֶν����������������ɲ���after����  
//		                if (date1.before(date2)) {  
//		                    return 1;  
//		                }  
//		                return -1;
//					}
//				});
				
				adapter = new OrderAdapter(MainActivity.this, list);
				listView.setAdapter(adapter);
//				listView.setAdapter(new OrderAdapter(MainActivity.this, list));
				
			}
		});
		
	}

	private String getTime(final OnGetTimeLoadFinishListener listener) {
		Bmob.getServerTime(this, new GetServerTimeListener() {
			
			@Override
			public void onSuccess(long arg0) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		         String times = formatter.format(new Date(arg0 * 1000L));
		        Log.i("TAG","��ǰ������ʱ��Ϊ:" + times);
//		        time.setMm(times);
		        listener.OnGetTimeLoadFinish(times);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("TAG","��ȡ������ʱ��ʧ��:" + arg1);
			}
		});
		
		return null;
	}



	
	/**֪ͨ��Ϣ**/
	private void sendNotification(Context context,String ticker,String text){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
		builder.setTicker(ticker);
		builder.setContentTitle("��������");
		builder.setContentText(text);
		builder.setAutoCancel(true);
		Notification notification = builder.getNotification();
		manager.notify(0, notification );
	}

	
	


	/**
	 * ��ָ��Android�û�������Ϣ
	 * @param message
	 * @param installId
	 */
	private void yes(String message, String installId){
//		bmobPush.pushMessage(message, installId);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("installationId", installId);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
	private void no(String message, String installId){
//		bmobPush.pushMessage(message, installId);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("installationId", installId);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}
	
}
