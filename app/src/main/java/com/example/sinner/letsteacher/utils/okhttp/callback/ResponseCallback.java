package com.example.sinner.letsteacher.utils.okhttp.callback;


public interface ResponseCallback {
	
	public void OnSuccess(String classinfo);
	
	public void OnFail(String arg0, String arg1);
	//未拓展
	/*public  void OnStart();
	//未拓展
	public void OnFinsh();*/

}
