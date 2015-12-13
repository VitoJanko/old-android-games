package jb.batteryindicator.lite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class BatteryService extends Service{
    /** Command to the service to display a message */
    public static final int MSG_SAY_HELLO = 1;
    public static final int MSG_BATTERY_CHANGED = 2;
    private int batteryLvl = -1;

    @Override
    public void onCreate(){
    	super.onCreate();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    	filter.addAction(Intent.ACTION_POWER_CONNECTED);
    	filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    	
    	registerReceiver(batteryChangereceiver,filter);
    	Toast.makeText(getApplicationContext(), "Service is started.", Toast.LENGTH_SHORT).show();
    }
   
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	Toast.makeText(getApplicationContext(), "Service is stopped.", Toast.LENGTH_SHORT).show();
    	unregisterReceiver(batteryChangereceiver);
    }
    
    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    
    
	public void showNotification(int imageId){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(imageId)
		        .setContentTitle("Battery level")
		        .setContentText("Battery level is at "+ batteryLvl+"%");
		// Creates an explicit intent for an Activity in your app
		
		Intent resultIntent = new Intent(this, Main.class);

		
		Notification note  = mBuilder.build();
		

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

		note.contentIntent = contentIntent;
		
		note.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
//		note.defaults |= Notification.DEFAULT_LIGHTS; // LED
//		note.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
//		note.defaults |= Notification.DEFAULT_SOUND; // Sound
		note.flags |= Notification.FLAG_ONGOING_EVENT;
		
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(2, note);
	}
	
	
	
	public int getBatteryId(int percentage){
		switch(percentage){
		case 100:
			return R.drawable.sto;
		case 99:
			return R.drawable.devetindevetdeset;
		case 98:
			return R.drawable.osemindevetdeset;
		case 97:
			return R.drawable.sedemindevetdeset;
		case 96:
			return R.drawable.sestindevetdeset;
		case 95:
			return R.drawable.petindevetdeset;
		case 94:
			return R.drawable.stiriindevetdeset;
		case 93:
			return R.drawable.triindevetdeset;
		case 92:
			return R.drawable.dvaindevetdeset;
		case 91:
			return R.drawable.enaindevetdeset;
		case 90:
			return R.drawable.devetdeset;
		case 89:
			return R.drawable.devetinosemdeset;
		case 88:
			return R.drawable.oseminosemdeset;
		case 87:
			return R.drawable.sedeminosemdeset;
		case 86:
			return R.drawable.sestinosemdeset;
		case 85:
			return R.drawable.petinosemdeset;
		case 84:
			return R.drawable.stiriinosemdeset;
		case 83:
			return R.drawable.triinosemdeset;
		case 82:
			return R.drawable.dvainosemdeset;
		case 81:
			return R.drawable.enainosemdeset;
		case 80:
			return R.drawable.osemdeset;
		case 79:
			return R.drawable.devetinosemdeset;
		case 78:
			return R.drawable.oseminsedemdeset;
		case 77:
			return R.drawable.sedeminsedemdeset;
		case 76:
			return R.drawable.sestinsedemdeset;
		case 75:
			return R.drawable.petinsedemdeset;
		case 74:
			return R.drawable.stiriinsedemdeset;
		case 73:
			return R.drawable.triinsedemdeset;
		case 72:
			return R.drawable.dvainsedemdeset;
		case 71:
			return R.drawable.enainsedemdeset;
		case 70:
			return R.drawable.sedemdeset;
		case 69:
			return R.drawable.devetinsestdeset;
		case 68:
			return R.drawable.oseminsestdeset;
		case 67:
			return R.drawable.sedeminsestdeset;
		case 66:
			return R.drawable.sestinsestdeset;
		case 65:
			return R.drawable.petinsestdeset;
		case 64:
			return R.drawable.stiriinsestdeset;
		case 63:
			return R.drawable.triinsestdeset;
		case 62:
			return R.drawable.dvainsestdeset;
		case 61:
			return R.drawable.enainsestdeset;
		case 60:
			return R.drawable.sestdeset;
		case 59:
			return R.drawable.devetinpetdeset;
		case 58:
			return R.drawable.oseminpetdeset;
		case 57:
			return R.drawable.sedeminpetdeset;
		case 56:
			return R.drawable.sestinpetdeset;
		case 55:
			return R.drawable.petinpetdeset;
		case 54:
			return R.drawable.stiriinpetdeset;
		case 53:
			return R.drawable.triinpetdeset;
		case 52:
			return R.drawable.dvainpetdeset;
		case 51:
			return R.drawable.enainpetdeset;
		case 50:
			return R.drawable.petdeset;
		case 49:
			return R.drawable.devetinstirideset;
		case 48:
			return R.drawable.oseminstirideset;
		case 47:
			return R.drawable.sedeminstirideset;
		case 46:
			return R.drawable.sestinstirideset;
		case 45:
			return R.drawable.petinstirideset;
		case 44:
			return R.drawable.stiriinstirideset;
		case 43:
			return R.drawable.triinstirideset;
		case 42:
			return R.drawable.dvainstirideset;
		case 41:
			return R.drawable.enainstirideset;
		case 40:
			return R.drawable.stirideset;
		case 39:
			return R.drawable.devetintrideset;
		case 38:
			return R.drawable.osemintrideset;
		case 37:
			return R.drawable.sedemintrideset;
		case 36:
			return R.drawable.sestintrideset;
		case 35:
			return R.drawable.petintrideset;
		case 34:
			return R.drawable.stiriintrideset;
		case 33:
			return R.drawable.triintrideset;
		case 32:
			return R.drawable.dvaintrideset;
		case 31:
			return R.drawable.enaintrideset;
		case 30:
			return R.drawable.trideset;
		case 29:
			return R.drawable.devetindvajset;
		case 28:
			return R.drawable.osemindvajset;
		case 27:
			return R.drawable.sedemindvajset;
		case 26:
			return R.drawable.sestindvajset;
		case 25:
			return R.drawable.petindvajset;
		case 24:
			return R.drawable.stiriindvajset;
		case 23:
			return R.drawable.triindvajset;
		case 22:
			return R.drawable.dvaindvajset;
		case 21:
			return R.drawable.enaindvajst;
		case 20:
			return R.drawable.dvajst;
		case 19:
			return R.drawable.devetnajst;
		case 18:
			return R.drawable.osemnajst;
		case 17:
			return R.drawable.sedemnajst;
		case 16:
			return R.drawable.sestnajst;
		case 15:
			return R.drawable.petnajst;
		case 14:
			return R.drawable.stirinajst;
		case 13:
			return R.drawable.trinajst;
		case 12:
			return R.drawable.dvanajst;
		case 11:
			return R.drawable.enajst;
		case 10:
			return R.drawable.deset;
		case 9:
			return R.drawable.devet;
		case 8:
			return R.drawable.osem;
		case 7:
			return R.drawable.sedem;
		case 6:
			return R.drawable.sest;
		case 5:
			return R.drawable.pet;
		case 4:
			return R.drawable.stiri;
		case 3:
			return R.drawable.tri;
		case 2:
			return R.drawable.dva;
		case 1:
			return R.drawable.ena;
		case 0:
			return R.drawable.nic;
		default:
			return R.drawable.nic;
		}
	}

	
	private final BroadcastReceiver batteryChangereceiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
			        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -2);
			        int battPct = (int)(level*100/(float)scale);
//			        Toast.makeText(context, "Battery level: "+battPct, Toast.LENGTH_SHORT).show();
			        batteryLvl = battPct;
			        showNotification(getBatteryId(battPct));
				}else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
		        	Toast.makeText(context, "Power cable off", Toast.LENGTH_SHORT).show();
				}
		   }
		};
}
