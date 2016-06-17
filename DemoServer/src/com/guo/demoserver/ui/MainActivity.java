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
	
	//推送
	BmobPushManager<BmobInstallation> bmobPush;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.order_list);
		handerView = findViewById(R.id.headerview);
		setHeaderTitle(handerView, "今天的订单");
		setHeaderImage(handerView, R.drawable.mycenter, Position.RIGHT, new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,PersonActivity.class));
			}
		});
		
		
		
		//开启debug服务后，可知晓push服务是否正常启动和运行
		BmobPush.setDebugMode(true);
		//开启推送服务
		BmobPush.startWork(this);	
		bmobPush = new BmobPushManager<BmobInstallation>(this);
		
		/***数据表更新监听****/
		final BmobRealTimeData rtd = new BmobRealTimeData();
		rtd.start(this, new ValueEventListener() {
			
			@Override
			public void onDataChange(JSONObject arg0) {
				if(BmobRealTimeData.ACTION_UPDATETABLE.equals(arg0.optString("action"))){
					JSONObject data = arg0.optJSONObject("data");
					Log.d("TAG", "("+data.optString("name")+")"+"数据："+data);
					
//					order.add("name", data.opt("name").toString());
//					order.add("price", data.opt("price").toString());
//					order.add("phone", data.opt("phone").toString());
//					order.add("address", data.opt("address").toString());
//					list.add(order);
					
					//发送一个通知信息
					sendNotification(MainActivity.this, "你有新的订单", data.toString());
					chaxun();
					adapter.notifyDataSetChanged();
					
					
				}
			}
			
			@Override
			public void onConnectCompleted() {
				if(rtd.isConnected()){
					rtd.subTableUpdate("Order");
				}
				Log.d("TAG", "这是监听的**连接成功:"+rtd.isConnected());

			}
		});
		
		/**查询服务器时间**/
		chaxun();
		
		
		//点击条目，打开对话框
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//弹出一个界面，显示被点击的订单的详细的信息
				//被点击的订单信息取出来进行推送或拒绝操作
//				List<Order> list2 = (List<Order>) adapter.getItem(position);
				final Order orders = (Order) adapter.getItem(position);
				
				/**自定义一个对话框**/
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
//						//如果是新用户就为他添加数据，老用户就修改数据
//						BmobQuery<Answer> query = new BmobQuery<Answer>();
//						query.addWhereEqualTo("answername", orders.getPhone());
//						query.findObjects(MainActivity.this, new FindListener<Answer>() {
//
//							@Override
//							public void onError(int arg0, String arg1) {
//								Toast.makeText(MainActivity.this, "为新用户添加数据", 1).show();
//								
//								Answer answer = new Answer();
//								answer.setAnswername(orders.getPhone());
//								answer.setContent("店家已接单");
//								answer.save(MainActivity.this, new SaveListener() {
//									@Override
//									public void onSuccess() {
//										Toast.makeText(MainActivity.this, "成功为添加数据", 1).show();
//									}
//									
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "添加数据失败："+arg0, 1).show();
//									}
//								});
//							}
//
//							@Override
//							public void onSuccess(List<Answer> arg0) {
//
//								Answer answer = new Answer();
//								answer.setContent("店家已接单");
//								answer.update(MainActivity.this, getId(arg0), new UpdateListener() {
//									
//									
//									@Override
//									public void onSuccess() {
//										Toast.makeText(MainActivity.this, "更新数据成功：", 1).show();
//									}
//									
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "更新数据失败："+arg1, 1).show();
//									}
//								});
//							}
//
//							private String getId(List<Answer> arg0) {
//								String oid = null;
//								 for (Answer Answer : arg0) {
//						               //获得数据的objectId信息
//									 	oid = Answer.getObjectId();
//						            }
//								return oid;
//							}
//						});
//					}
//				});
//				
//				
//				//不接单的情况
//				no.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						final String a =  et.getText().toString();
//						if(TextUtils.isEmpty(a)){
//							Toast.makeText(MainActivity.this, "理由不能为空", 1).show();
//							return;
//						}
//						BmobQuery<Answer> query2 = new BmobQuery<Answer>();
//						query2.addWhereEqualTo("answername", orders.getPhone());
//						query2.findObjects(MainActivity.this, new FindListener<Answer>() {
//
//							@Override
//							public void onError(int arg0, String arg1) {
//								Toast.makeText(MainActivity.this, "为新用户添加数据", 1).show();
//
//								Answer answer = new Answer();
//								answer.setAnswername(orders.getPhone());
//								answer.setContent(a);
//								answer.save(MainActivity.this, new SaveListener() {
//									@Override
//									public void onSuccess() {
//										Toast.makeText(MainActivity.this, "成功为添加数据", 1).show();
//									}
//
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "添加数据失败："+arg0, 1).show();
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
//										Toast.makeText(MainActivity.this, "更新数据成功：", 1).show();
//									}
//									
//									@Override
//									public void onFailure(int arg0, String arg1) {
//										Toast.makeText(MainActivity.this, "更新数据失败："+arg1, 1).show();
//									}
//								});
//							}
//
//							private String getId(List<Answer> arg0) {
//								String oid = null;
//								for (Answer Answer : arg0) {
//						               //获得数据的objectId信息
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


	
	/**更新自定义的BmobInstallation字段  
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
							Log.i("bmob", "更新成功");
						}
						
						@Override
						public void onFailure(int code, String msg) {
							// TODO Auto-generated method stub
							Log.i("bmob", "更新失败："+msg);
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
//			yes("店家已接单", "5909E8A029957C1DC8FFAA9D4E871AA2");
			yes("店家已接单", order.getInstal());
			break;

		/*case R.id.detail_no:
			
			no("店家拒绝了你的订单", "5909E8A029957C1DC8FFAA9D4E871AA2");
			break;
		}*/
		case R.id.detail_no:

//			no("店家拒绝了你的订单", BmobInstallation.getCurrentInstallation(this).getInstallationId(this));
			no("店家拒绝了你的订单", order.getInstal());
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
				//查询数据
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
		Log.i("TAG","start为:" + start);
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
		//小于23：59：59
		BmobQuery<Order> q2 = new BmobQuery<Order>();
//		String end = time.getMm()+" "+"23:59:59";
		String end = cc+" "+"23:59:59";
		Log.i("TAG","end为:" + end);
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
		//添加复合与查询
		query.and(and);
		// 根据createdAt字段升序显示数据
//		query.order("createdAt");
		// 根据createdAt字段降序显示数据
		query.order("-createdAt");
		query.findObjects(MainActivity.this, new FindListener<Order>() {

			@Override
			public void onError(int arg0, String arg1) {
				Log.i("TAG", "查询失败："+arg1);
				Toast.makeText(MainActivity.this, "查询失败："+arg1, 1).show();
			}

			@Override
			public void onSuccess(List<Order> arg0) {
				Log.i("TAG", "查询成功："+arg0);
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
				
				Log.d("TAG", "查询到的order："+list.toString());
				//Toast.makeText(MainActivity.this, "查询成功："+arg0, 1).show();
				
//				Collections.sort(arg0, new Comparator<Order>() {
//
//					@Override
//					public int compare(Order arg0, Order arg1) {
//						Date date1 = DateUtil.stringToDate(arg0.getCreatedAt());  
//		                Date date2 = DateUtil.stringToDate(arg1.getCreatedAt()); 
//		                // 对日期字段进行升序，如果欲降序可采用after方法  
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
		        Log.i("TAG","当前服务器时间为:" + times);
//		        time.setMm(times);
		        listener.OnGetTimeLoadFinish(times);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("TAG","获取服务器时间失败:" + arg1);
			}
		});
		
		return null;
	}



	
	/**通知信息**/
	private void sendNotification(Context context,String ticker,String text){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
		builder.setTicker(ticker);
		builder.setContentTitle("订单嘻嘻");
		builder.setContentText(text);
		builder.setAutoCancel(true);
		Notification notification = builder.getNotification();
		manager.notify(0, notification );
	}

	
	


	/**
	 * 给指定Android用户推送消息
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
