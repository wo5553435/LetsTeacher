package com.example.sinner.letsteacher.interfaces;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


import java.io.UnsupportedEncodingException;


/**
 * @ClassName: MyInputTextWatcher
 * 
 * @Description: 指定edittext输入规则,规定默认和回车关闭输入法,以及输入内容事件
 * 
 * @author: sinner
 * 
 * @date 2015-12-9 上午9:37:19
 */
public class MyInputTextWatcher implements TextWatcher,OnEditorActionListener {
	
		private Context context;
		private Activity activity;
        private String temp;
        private int editStart;
        private int editEnd;
        private Handler mhandler;
        private EditText content;
        int num = 140;//限制的最大字数
        private TextView tv;
        private onTexTChangeEvent textevent;
        private int lastno=0;
        private boolean isEmpty=true;
        private boolean isOver=false;//是否触发只一遍全满输入框事件
        
        /**
         * 
         * @param context 上下文
         * @param activity 载体
         * @param handler UI线程
         * @param content 输入框 ，会绑定去除回车键
         */
        public MyInputTextWatcher(Context context ,Activity activity,Handler handler,EditText content){
        	super();
        	this.mhandler=handler;
        	this.activity=activity;
        	this.context=context;
        	this.content=content;
        	content.setOnEditorActionListener(this);
        }
        
        
        
        /**
         * 
         * @param context 上下文
         * @param activity 载体
         * @param handler UI线程
         * @param content 输入框 ，会绑定去除回车键
         * @param tv  需要回显的textview
         */
        public MyInputTextWatcher(Context context ,Activity activity,Handler handler,EditText content,TextView tv){
        	super();
        	this.mhandler=handler;
        	this.activity=activity;
        	this.context=context;
        	this.content=content;
        	this.tv=tv;
        	content.setOnEditorActionListener(this);
        }
        
        public void setTextEvent(onTexTChangeEvent event){
        	this.textevent=event;
        }
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s.toString();
            if(textevent!=null){
            	textevent.OnTextChange(s, start, before, count);
            }
            
        }

        public void setMaxLength(int max){
        	this.num=max;
        }
        
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        	 int number = num - s.length(); 
        	 if(tv!=null){
             tv.setText("" + number);
             }
             Log.e("剩余字数", ""+number);
             editStart = content.getSelectionStart(); 
             editEnd = content.getSelectionEnd(); 
             if (temp.length() > num) { 
                 s.delete(editStart - 1, editEnd); 
                 int tempSelection = editEnd; 
                 content.setText(s); 
                 content.setSelection(tempSelection);//设置光标在最后 
                 if(textevent!=null&&!isOver){
            		 textevent.onFullInput();
            		 isOver=true;
            	 }
             } else{
            	 if(number==num){//如果当前是未输入字数
                	 if(!isEmpty){//之前不是空输入状态
                		 if(textevent!=null){
                		 textevent.onBeEmptyInput();
                	 	}
                	 }
                	 isEmpty=true;
                 }else{
                	 if(isEmpty){
                		 if(textevent!=null){
                			 textevent.onNoEmptyInput();
                			 
                		 }
                	 }
                	 isEmpty=false;
                 }
            	 isOver=false;
             }
           // mMainHandler.removeMessages(HD_MSG_UPDATE_HINT);
           // mCurrentHint = s.toString().trim();
        	Log.e("changed", "----");
            if (!TextUtils.isEmpty(temp)) {
                
            }
         //   mMainHandler.sendEmptyMessageDelayed(HD_MSG_UPDATE_HINT, HINT_UPDATE_DALEY_TIME);
        }
    /**
     * 
     *@Description: 特殊需求判断
     *@param inputStr 新增输入字段
     *@return
     */
    private String getLimitSubstring(String inputStr) {
        int orignLen = inputStr.length();
        int resultLen = 0;
        String temp = null;
        for (int i = 0; i < orignLen; i++) {
            temp = inputStr.substring(i, i + 1);
            try {// 3 bytes to indicate chinese word,1 byte to indicate english
                 // word ,in utf-8 encode
                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    resultLen++;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (resultLen > 30) {
                return inputStr.substring(0, i);
            }
        }
        return inputStr;
    }


	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(event==null){
				return true;
			}
			if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
				Log.e("KeyEvent.KEYCODE_Enter", "---"+(KeyEvent.KEYCODE_ENTER));
				//关闭输入法
				InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 
				InputMethodManager.HIDE_NOT_ALWAYS); 

			 return (event.getKeyCode()==KeyEvent.KEYCODE_ENTER);  
			}else{
				Log.e("other keys", "---");
				return false;
			}
			 
	}

	
	public interface onTexTChangeEvent {
		/**
		 * 从有内容到无内容事件
		 */
		public void  onBeEmptyInput();
		/**
		 * 
		 *@Description: 从无内容到有内容事件
		 */
		public void  onNoEmptyInput();
		
		/**
		 * 
		 *@Description: 内容输入满时事件
		 */
		public void onFullInput();
		
		/**
		 * 内容输入时触发
		 */
		public  void OnTextChange(CharSequence s, int start, int before, int count);
	}
	
}
