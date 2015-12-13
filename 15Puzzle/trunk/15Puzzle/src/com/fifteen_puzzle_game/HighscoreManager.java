package com.fifteen_puzzle_game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class HighscoreManager{

public static boolean noninterupted=true;
	
public static boolean sendServerData(String url,String uid,String uname,String uscore,String unewuser){
	boolean uspesno = true;
	
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
//	nameValuePairs.add(new BasicNameValuePair("id","24"));
//	nameValuePairs.add(new BasicNameValuePair("name","Stric"));
//	nameValuePairs.add(new BasicNameValuePair("sex","1"));
//	nameValuePairs.add(new BasicNameValuePair("birthyear","2000"));
	nameValuePairs.add(new BasicNameValuePair("id",uid));
	nameValuePairs.add(new BasicNameValuePair("name",uname));
	nameValuePairs.add(new BasicNameValuePair("score",uscore));
	nameValuePairs.add(new BasicNameValuePair("newuser",unewuser));
	String geturl = url+"?id="+uid+"&name="+uname+"&score="+uscore+"&newuser="+unewuser;
	//Poslje podatke serverju
	int stevec = 0;
	noninterupted=true;
	while(uspesno && stevec < 10 && noninterupted){
		try{
			int timeout = 1500;
			HttpParams httpParams = new BasicHttpParams();		
			HttpConnectionParams.setConnectionTimeout(httpParams,timeout);
			HttpConnectionParams.setSoTimeout(httpParams, timeout);
			
	//		HttpClient httpclient = new DefaultHttpClient(httpParams);
	//		HttpPost httppost = new HttpPost(url);
	//		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	//		HttpResponse response = httpclient.execute(httppost);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(geturl));
			request.setParams(httpParams);
			stevec++;
			HttpResponse response = client.execute(request);
			
//			int resp=response.getStatusLine().getStatusCode();
			if(response != null){
				int resp=response.getStatusLine().getStatusCode();
				if(resp == 200){
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					StringBuilder sb = new StringBuilder("");
					String line = "";
					while ((line = reader.readLine()) != null) {
						
						sb.append(line);
					}
					reader.close();
					String uspeh=sb.toString();
					
					if(uspeh.contains("Uspeh")){
						uspesno = false;
						noninterupted=false;
					}
				}
			}
		
	}catch(Exception e){
	}}
	
	return true;
}

public static String[] getServerData(String url,String userid) {    
//   InputStream is = null;
    
   String result = "";
   String returnString = "";
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	nameValuePairs.add(new BasicNameValuePair("id",userid));
	
	boolean uspesno=true;
	int stevec = 0;
	noninterupted=true;
	while(uspesno && stevec < 5 && noninterupted){
    try{
 
    		int timeout = 3000;
    		HttpParams httpParams = new BasicHttpParams();
    		
    		HttpConnectionParams.setConnectionTimeout(httpParams,timeout);
    		HttpConnectionParams.setSoTimeout(httpParams, timeout);
   	
    		HttpClient client = new DefaultHttpClient();
    		HttpGet request = new HttpGet();
    		request.setURI(new URI(url));
    		request.setParams(httpParams);
    		
    		if(noninterupted){
	    		HttpResponse response = client.execute(request);
	
	    		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
	
	            StringBuilder sb = new StringBuilder("");
	            String line = "";
	            String NL = System.getProperty("line.separator");
	            while ((line = reader.readLine()) != null) {
	            	
	                sb.append(line + NL);
	            }
	            reader.close();
	            result=sb.toString();
	            returnString += result;
	            
				if(response != null){
					int resp=response.getStatusLine().getStatusCode();
					if(resp == 200){
						
						if(result.contains("<br />")){
							uspesno = false;
						}
					}
				}
    		}		
	    }catch(Exception e){
	            Log.e("log_tag", "Error converting result "+e.toString());
//	            returnString=("Error with your conection. Please try again later.");
	            returnString="";
	            uspesno=true;
	    }}
	    
	    
	    String[] besedilo = razcleniBesedilo(returnString);
	    return besedilo;
    	
}    
	
	//Razcleni string po <br /> (nova vrstica v php/html).
	public static String[] razcleniBesedilo(String besedilo){
		
		String[] razclenjenoBesedilo = besedilo.split("<br />");
		
		return razclenjenoBesedilo;
	}
    
}