package com.example.sinner.letsteacher.activity;


import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.utils.Logs;
import com.polyak.iconswitch.IconSwitch;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BasicActivity {

    @BindView(R.id.layout_login_title)
    RelativeLayout layout_title;

    @BindView(R.id.title_lin_back)
    LinearLayout titleLinBack;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.login_userName_icon)
    ImageView loginUserNameIcon;
    @BindView(R.id.login_userName_ed)
    EditText loginUserNameEd;
    @BindView(R.id.icon_switch)
    IconSwitch iconSwitch;
    @BindView(R.id.login_check_four)
    ImageView loginCheckFour;
    @BindView(R.id.login_check_four_text)
    TextView loginCheckFourText;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.icon_login_wechat)
    ImageView iconLoginWechat;
    @BindDrawable(R.drawable.circle_blue1_bg)
    Drawable drawable_blue1;
    @BindDrawable(R.drawable.circle_blue_bg)
    Drawable drawable_blue;

    private boolean isfirst=true;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initGui() {

    }

    @Override
    protected void initData() {

    }


    private void Setdata(List<? extends String> c){
        for (int i = 0; i < c.size(); i++) {
            Logs.e("i:"+i,c.get(i));
        }
    }
    @Override
    protected void initAction() {
            iconSwitch.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
                @Override
                public void onCheckChanged(IconSwitch.Checked current) {
                    ChangeBtnStatu(current== IconSwitch.Checked.LEFT?false:true);
                }
            });
    }


    @OnClick({R.id.title_lin_back, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_lin_back:
                finish();
                break;
            case R.id.login_btn:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initAnimation();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void ChangeBtnStatu(boolean isMember){
        layout_title.setBackgroundColor(isMember?getResources().getColor(R.color.menu_blue2):getResources().getColor(R.color.menu_blue));
        loginBtn.setBackground(isMember?drawable_blue1:drawable_blue);
        if(Build.VERSION.SDK_INT>21){
            getWindow().setStatusBarColor(isMember?getResources().getColor(R.color.menu_blue2):getResources().getColor(R.color.menu_blue));
            if(animator!=null)
                animator.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initAnimation() {
        if(isfirst) return;
        animator= ViewAnimationUtils.createCircularReveal(loginBtn,
                loginBtn.getWidth()/2, loginBtn.getHeight()/2,
                loginBtn.getHeight()/2, loginBtn.getWidth()/2)
                .setDuration(400);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(iconSwitch.getChecked() == IconSwitch.Checked.LEFT){
                    loginBtn.setBackground(drawable_blue);
//                    getWindow().setStatusBarColor(getResources().getColor(R.color.menu_blue));
                }else{
                    loginBtn.setBackground(drawable_blue1);

                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        isfirst=false;
    }

    Animator animator;
}




/*import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.entity.UserVo;
import com.example.sinner.letsteacher.utils.Logs;
import com.example.sinner.letsteacher.utils.SuperToastUtil;
import com.example.sinner.letsteacher.utils.bmob.BmobSearchListener;
import com.example.sinner.letsteacher.utils.bmob.BmobUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

import static android.Manifest.permission.READ_CONTACTS;

*//**
 * A login screen that offers login via email/password.
 * <p>
 * Id to identity READ_CONTACTS permission request.
 * <p>
 * A dummy authentication store containing known user names and passwords.
 * TODO: remove after connecting to a real authentication system.
 * <p>
 * Keep track of the login task to ensure we can cancel it if requested.
 * <p>
 * Callback received when a permissions request has been completed.
 * <p>
 * Attempts to sign in or register the account specified by the login form.
 * If there are form errors (invalid email, missing fields, etc.), the
 * errors are presented and no actual login attempt is made.
 * <p>
 * Shows the progress UI and hides the login form.
 *//*
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    *//**
 * Id to identity READ_CONTACTS permission request.
 *//*
    private static final int REQUEST_READ_CONTACTS = 0;

    *//**
 * A dummy authentication store containing known user names and passwords.
 * TODO: remove after connecting to a real authentication system.
 *//*
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    *//**
 * Keep track of the login task to ensure we can cancel it if requested.
 *//*
    // private UserLoginTask mAuthTask = null;

    // UI references.
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @BindView(R.id.password)
    EditText mPasswordView;


    @BindView(R.id.email_sign_in_button)
    Button emailSignInButton;

    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;

    @BindView(R.id.btn_login_regist)
    TextView btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // Set up the login form.

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("5069096f8313d0c157878a756cf32776")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //emailSignInButton= (Button) findViewById(R.id.email_sign_in_button);
        emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        btn_reg.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btn_reg.getPaint().setAntiAlias(true);//抗锯齿
    }

    @OnClick(R.id.email_sign_in_button)
    public void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } *//*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*//*

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            LoginAction(mEmailView.getText().toString(), mPasswordView.getText().toString());
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);
        }
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @OnClick(R.id.btn_login_regist)
    public void ToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 2:
                if (data != null) {
                    mEmailView.setText(data.getStringExtra("username"));
                    mPasswordView.setText(data.getStringExtra("password"));
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    *//**
 * Callback received when a permissions request has been completed.
 *//*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    *//**
 * Attempts to sign in or register the account specified by the login form.
 * If there are form errors (invalid email, missing fields, etc.), the
 * errors are presented and no actual login attempt is made.
 *//*


    //登录操作
    private void LoginAction(String username, String password) {
        HashMap<String, String> params = new HashMap<>();
        // private String username;
        // private String password;
        params.put("username", username);
        params.put("password", password);

        mProgressView.setVisibility(View.VISIBLE);
        BmobUtil.getInstance().SearchData("UserVo", params, new BmobSearchListener<UserVo>(UserVo.class) {
            @Override
            public void OnSuccess(ArrayList<UserVo> arrayList) {
                Logs.e("OnSuccess", "--");
                mProgressView.setVisibility(View.GONE);
                if (arrayList != null && arrayList.size() == 0) {
                    SuperToastUtil.getInstance(LoginActivity.this).showToast("错误的用户名或密码!");
                } else {
                    if (arrayList.size() == 1) {//这里是正确的情况
                        UserVo usew = arrayList.get(0);
                        SuperToastUtil.getInstance(LoginActivity.this).showToast("您好!" + usew.getUsername());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                        } else {
                            startActivity(intent);
                        }
                    } else {//竟然查到多个用户信息，有问题

                    }
                }
            }

            @Override
            public void OnFail(String errorcode, String errormsg) {
                Logs.e("OnFail", "" + errormsg);
                mProgressView.setVisibility(View.GONE);
                SuperToastUtil.getInstance(LoginActivity.this).showToast(errormsg);
            }

        });

    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    *//**
 * Shows the progress UI and hides the login form.
 *//*
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}*/

