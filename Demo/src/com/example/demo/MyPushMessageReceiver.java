package com.example.demo;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.helper.NotificationCompat;

public class MyPushMessageReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
		Log.i("BmobClient", "�յ���������Ϣ��"+message);
		Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_LONG).show();
		//ʹ��cn.bmob.v3.helper���µ�NotificationCompat������֪ͨ����Ҳ����ʹ��support_v4�����NotificationCompat��
		Intent i = new Intent();
		i.setClass(context, MyPushMessageReceiver.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
		NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context)
        .setTicker("�յ���ҵ���Ϣ����")
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("��Ϣ")
        .setContentText(message)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
        .setContentIntent(pi);
		// ����֪ͨ
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = mBuilder.build();  
        nm.notify(0, n);
	}

	/*@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
			Log.d("TAG", "�ͻ����յ��������ݣ�"+intent.getStringExtra("msg"));
			String msg = intent.getStringExtra("msg").toString();
			try {
				JSONObject jobj = new JSONObject(msg);
				String a = jobj.getString("alert");
//				Toast.makeText(context, "�ͻ����յ��������ݣ�"+a, Toast.LENGTH_LONG).show();
				
				sendNotification(context, "�������Ϣ", a);
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
//			NotificationManager manager = 
		}
	}
	
	private void sendNotification(Context context,String ticker,String text){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
		builder.setTicker(ticker);
		builder.setContentTitle("֪ͨ");
		builder.setContentText(text);
		builder.setAutoCancel(true);
		Notification notification = builder.getNotification();
		manager.notify(0, notification );
	}*/
}
