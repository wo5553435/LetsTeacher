package com.pigcms.library.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pigcms.library.R;


/**
 * 自定义对话框
 */
public class MyDialog extends Dialog implements View.OnClickListener {

	private Button bt_cancel;
	private Button bt_ok;
	private TextView tv_content;
	private TextView tv_title;
	private View view_line;
	private boolean isCenter=true;

	private OnResultListener mListener;// 回调

	public MyDialog(Context context) {
		super(context);
		init();
	}


	public MyDialog(Context context, int theme){
		super(context,theme);
		init();
	}

	public MyDialog(Context context, int theme, boolean isCenter) {
		super(context, theme);
		this.isCenter=isCenter;
		init();
	}

	public MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		setContentView(R.layout.dialog);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_ok = (Button) findViewById(R.id.bt_ok);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		view_line = (View) findViewById(R.id.view_line);
		bt_cancel.setOnClickListener(this);
		bt_ok.setOnClickListener(this);
		if(!isCenter){
			tv_content.setGravity(Gravity.CENTER_VERTICAL);
		}
	}

	/**
	 * 监听回调
	 */
	public interface OnResultListener {
		void Ok();

		void Cancel();
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.bt_ok){
			mListener.Ok();
		}

		else if( v.getId()==R.id.bt_cancel){
			mListener.Cancel();
		}

	}

	public void setOnResultListener(OnResultListener mListener) {
		this.mListener = mListener;
	}

	/**
	 * 设置内容
	 */
	public void setTextContent(String content) {
		tv_content.setText(content);
	}

	//setText(styledText, TextView.BufferType.SPANNABLE)
	public void setTextContent(SpannableString styledText){
		tv_content.setText(styledText, TextView.BufferType.SPANNABLE);
	}

	/**
	 * 设置标题
	 */
	public void setTextTitle(String title) {
		tv_title.setText(title);
	}

	/**
	 * 设置取消按钮的字
	 */
	public void setButtonContent(String content) {
		bt_cancel.setText(content);
	}

	/**
	 * 设置OK按钮的字
	 */
	public void setButtonOk(String content) {
		bt_ok.setText(content);
	}

	/**
	 * 是否只有一个确定按钮
	 */
	public void setOnlyOk(boolean b) {
		if (b) {
			bt_cancel.setVisibility(View.GONE);
			view_line.setVisibility(View.GONE);
			bt_ok.setBackgroundResource(R.drawable.dialog_ok2);
		} else {
			bt_cancel.setVisibility(View.VISIBLE);
			view_line.setVisibility(View.VISIBLE);
			bt_ok.setBackgroundResource(R.drawable.dialog_ok);
		}
	}
}