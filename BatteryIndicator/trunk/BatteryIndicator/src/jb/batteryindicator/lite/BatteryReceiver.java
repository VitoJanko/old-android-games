package jb.batteryindicator.lite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class BatteryReceiver extends BroadcastReceiver {
//	Messenger mService = null;
//	boolean mBound;
	
	@Override
	public void onReceive(Context context, Intent intent) {		
		if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
	        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	        int battPct = (int)(level/(float)scale);
//	        Toast.makeText(context, "Battery level: "+battPct, Toast.LENGTH_SHORT).show();
		}else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
//        	Toast.makeText(context, "Power cable off", Toast.LENGTH_SHORT).show();
		}else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			Intent service = new Intent(context,BatteryService.class);
			context.startService(service);
		}
	}	
}
