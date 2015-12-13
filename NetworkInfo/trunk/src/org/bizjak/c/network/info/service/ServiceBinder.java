package org.bizjak.c.network.info.service;

import android.os.Binder;

public class ServiceBinder extends Binder {
	InfoService service;
	
	public ServiceBinder(InfoService service){
		this.service = service;
	}
	
	public InfoService getService(){
		return service;
	}
}
