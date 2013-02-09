package cz.zitkino.client;

import java.util.LinkedList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import cz.zitkino.client.request.Request;
import cz.zitkino.utility.Logcat;


public class RequestManager
{
	private LinkedList<ApiCall> mQueue = new LinkedList<ApiCall>();
	
	
	public RequestManager()
	{

	}
	
	
	public void executeRequest(Request request, OnApiCallListener onApiCallListener)
	{
		ApiCall apiCall = new ApiCall(request, onApiCallListener);
		mQueue.add(apiCall);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			// use AsyncTask.THREAD_POOL_EXECUTOR or AsyncTask.SERIAL_EXECUTOR 
			apiCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{
			apiCall.execute();
		}
	}
	
	
	public boolean finishRequest(ApiCall apiCall)
	{
		return mQueue.remove(apiCall);
	}
	
	
	public int getRequestsCount()
	{
		return mQueue.size();
	}
	
	
	public boolean hasRunningRequest(Class<?> cls)
	{
		String className = cls.getSimpleName();
		
		for(ApiCall call : mQueue)
		{
			String callName = call.getRequest().getClass().getSimpleName();
			if(className.equals(callName)) return true;
		}
		
		return false;
	}
	
	
	public void cancelAllRequests()
	{
		for(int i=mQueue.size()-1;i>=0;i--)
		{
			ApiCall call = mQueue.get(i);
			if(call!=null)
			{
				call.cancel(true);
				mQueue.remove(call);
			}
		}
	}
	
	
	public void printQueue()
	{
		for(ApiCall call : mQueue)
		{
			Logcat.d("RequestManager.printQueue(): " + (call==null ? "null" : (call.getRequest().getClass().getSimpleName() + " / " + call.getStatus().toString())));
		}
		
		if(mQueue.isEmpty()) Logcat.d("RequestManager.printQueue(): empty");
	}
	
	
	public static boolean isOnline(Context context)
	{
		// needs android.permission.ACCESS_NETWORK_STATE
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null && netInfo.isAvailable() && netInfo.isConnected());
	}
}
