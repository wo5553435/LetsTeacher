package com.example.sinner.letsteacher.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sinner.letsteacher.R;
import com.pigcms.library.android.view.WaitingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {

	protected View mainView = null;
	protected Activity activity = null;
	protected BaseFragment fragment = null;
	protected Button leftBtn;
	protected TextView topTitle;
	protected ImageView rightBtn;
	// AlertDialog.Builder mBuilder = null;
	protected Unbinder unbinder;

	public abstract boolean onBackPressed();// 监听回退的方法

	public Toast mToast;
	private WaitingDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

//		if (!(getActivity() instanceof BackHandledInterface)) {
//			throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
//		} else {
//			this.mBackHandledInterface = (BackHandledInterface) getActivity();
//		}// 获取父类activity

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity =  activity;
	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(getMianLayout(), container, false);
		initView();
		unbinder= ButterKnife.bind(this,mainView);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initAction();
		initData();
		// mBuilder = new AlertDialog.Builder(activity);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * 布局文件
	 * 
	 * @return layout id
	 */
	public abstract int getMianLayout();

	/**
	 * 控件初始化
	 * 
	 */
	public abstract void initView();

	/**
	 * 事件监听
	 */
	public abstract void initAction();

	/**
	 * 数据处理
	 */
	public abstract void initData();

	/**
	 * 数据清空
	 */
	public abstract void clear();


	@Override
	public void onResume() {
		super.onResume();
	}

	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private long lastClickTime;

	/**
	 * @Title: isFastDoubleClick
	 * @Description: 判断事件出发时间间隔是否超过预定值
	 */
	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 开启等待层
	 */
	public void showProgressDialog() {
		if (dialog == null) {
			dialog = new WaitingDialog(activity, R.style.WaitingDialogStyle);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(true);
		}
		dialog.show();
	}

	/**
	 * 判断等待层是否开启状态
	 * @return 是否
     */
	public boolean isShowDialog(){
		if(dialog!=null)
			if(dialog.isShowing())
			return true;
		return  false;
	}
	/**
	 * 隐藏等待层
	 */
	public boolean  hideProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			return true;
		}
		return false;
	}

}