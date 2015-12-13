package org.bizjak.c.network.info.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bizjak.c.network.info.MainActivity;
import org.bizjak.c.network.info.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class InfoService extends Service{
	private static final String TAG = "InfoService";
	
	private final IBinder mBinder = new ServiceBinder(this);
	public Intent serviceIntent;
	
	private final int NOTIFICATION_ID = 18327218;
	
	private TelephonyManager tm;
	
	private Info info = new Info();

	
	
	
	public void onCreate(){
		super.onCreate();
		
		Toast.makeText(this, "service onCreate", Toast.LENGTH_SHORT).show();
		
		tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		
		tm.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
				| PhoneStateListener.LISTEN_SERVICE_STATE);
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

		setNotificationInfo();
		
		// If we get killed, after returning from here, restart
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		this.serviceIntent = intent;
		return mBinder;
	}
	
	  @Override
	  public void onDestroy(){
		  Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	  }
	  
	  public void setNotificationInfo(){
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			        .setContentTitle("Network Info");
			
			if(!getCountry().equalsIgnoreCase("si")){
				mBuilder.setSmallIcon(R.drawable.ic_launcher);
				mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
				Log.d(TAG, "Notification not SI "+getCountry());
			}else if(getOperator().equals("T-2")){
				mBuilder.setSmallIcon(R.drawable.tdva_logo);
				mBuilder.setPriority(NotificationCompat.PRIORITY_MIN);
				Log.d(TAG, "Notification not T-2 "+getOperator());
			}else if(getOperator().equals("MOBITEL")){
				mBuilder.setSmallIcon(R.drawable.mobitel_logo);
				mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
				Log.d(TAG, "Notification MOBITEL "+getOperator());
			}else{
				mBuilder.setSmallIcon(R.drawable.ic_launcher);
				Log.d(TAG, "Notification not OTHER "+getOperator());
			}

			
			info.network = getNetworkType();
			info.operator = getOperator();
			info.country = getCountry();
			
			
			mBuilder.setContentText(getNetworkType()+" : "+getOperator()+" - "+getCountry());
			

			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, MainActivity.class);
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(MainActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			
			
			
			// mId allows you to update the notification later on.
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	  }
	  
	  
	  public String getCountry(){
		  return tm.getNetworkCountryIso();
	  }
	  
	  public boolean isForeignCountry(){
		  return !tm.getNetworkCountryIso().equals(tm.getSimCountryIso());
	  }
	  
	  public boolean isRoaming(){
		  return tm.isNetworkRoaming();
	  }
	  
	  public String getOperator(){
		  Log.d(TAG, "Operator id: "+tm.getNetworkOperator());
		  return tm.getNetworkOperatorName();
	  }
	  
	  
	  public String getNetworkType(){
		  	String nType = "";
	        
			int networkType = tm.getNetworkType();
			switch (networkType) {
			    case TelephonyManager.NETWORK_TYPE_GPRS:
			    	Log.i(TAG, "GPRS");
			    	nType = "GPRS";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_EDGE:
			    	Log.i(TAG, "EDGE");
			    	nType = "EDGE";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_CDMA:
			    	Log.i(TAG, "CDMA");
			    	nType = "CDMA";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_1xRTT:
			    	Log.i(TAG, "1xRTT");
			    	nType = "1xRTT";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_IDEN:
			    	Log.i(TAG, "IDEN");
			    	nType = "IDEN";
			        break;
			    //Zgoraj je 2G?
			    case TelephonyManager.NETWORK_TYPE_UMTS:
			    	Log.i(TAG, "UMTS");
			    	nType = "UMTS";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_EVDO_0:
			    	Log.i(TAG, "EVDO_0");
			    	nType = "EVDO_0";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_EVDO_A:
			    	Log.i(TAG, "EVDO_A");
			    	nType = "EVDO_A";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_HSDPA:
			    	Log.i(TAG, "HSDPA");
			    	nType = "HSDPA";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_HSUPA:
			    	Log.i(TAG, "HSUPA");
			    	nType = "HSUPA";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_HSPA:
			    	Log.i(TAG, "HSPA");
			    	nType = "HSPA";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_EVDO_B:
			    	Log.i(TAG, "EVDO_B");
			    	nType = "EVDO_B";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_EHRPD:
			    	Log.i(TAG, "EHRPD");
			    	nType = "EHRPD";
			    	break;
			    case TelephonyManager.NETWORK_TYPE_HSPAP:
			        Log.i(TAG, "HSPAP");
			        nType = "HSPAP";
			        break;
			    case TelephonyManager.NETWORK_TYPE_LTE:
			        Log.i(TAG, "LTE");
			        nType = "LTE";
			        break;
			    default:
			        Log.i(TAG, "Unknown");
			        nType = "Unknown";
			        break;
	        }
			
			return nType;
	  }
	  
	  
	  
	  public Info getInfo(){
	        final String phoneNumber = tm.getLine1Number();
	        
	        Log.d(TAG,"phone: "+phoneNumber);
	        Log.d(TAG, "Carrier: "+tm.getNetworkOperatorName());
	        Log.d(TAG, "tm2 :"+tm.getNetworkCountryIso());
	        Log.d(TAG, "tm3: "+tm.getNetworkType());
	        Log.d(TAG, "Tm4: "+tm.getNetworkOperatorName());
	        Log.d(TAG, "Tm5: "+tm.getSimOperatorName());
	        Log.d(TAG, "NetworkType: "+tm.getNetworkType());
	        
	        setNotificationInfo();
	        

	        
	        return info;
	  }
	  
	  
	  public PhoneStateListener psListener = new PhoneStateListener(){
		  
		  public void onServiceStateChanged(ServiceState state){
			  Log.d(TAG, "Service state changed: "+state.getState());
			  
			  setNotificationInfo();
		  }
		  
		  public void onDataConnectionStateChanged(int state){
			  Log.d(TAG, "New state: "+state);
			  
			  setNotificationInfo();
		  }
		  
		  public void onSignalStrengthsChanged(SignalStrength strength){
			  info.CDMA = strength.getCdmaDbm();
			  info.EVDO = strength.getEvdoDbm();
			  info.strength = strength.getGsmSignalStrength();
			  
			  Log.d(TAG,"CDMA: "+strength.getCdmaDbm()+"  EVDO: "+strength.getEvdoDbm()+" "+strength.getGsmSignalStrength());
			  
			  //setNotificationInfo();
		  }
	  };
}
