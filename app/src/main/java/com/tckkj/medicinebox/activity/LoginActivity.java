package com.tckkj.medicinebox.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.LocalManageUtil;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.SPUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.util.country.Country;
import com.tckkj.medicinebox.view.ClearEditText;
import com.tckkj.medicinebox.view.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
/*
*
* Author：李迪迦
* Date：2019.6.4
* */
public class LoginActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private TextView tv_forget_password, tv_phone_choose, tv_user_agreement;
    private Button btn_login;
    private LinearLayout ll_phone_choose;
    private ClearEditText cet_phone, cet_login_password;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> countryList = new ArrayList<>();
    private static final String TAG = "获取.成功";
    /*
    给用户设置别名
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    Log.i("11111111111", "gotResult: " + new JPushMessage().toString());
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    SPUtil.saveData(LoginActivity.this,"isBindAlias",true);

                    App.isBindAlias = true;

                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            toast(logs);
        }
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_login);

        back.setVisibility(View.GONE);
        rightTxt.setText(R.string.register);
        rightTxt.setVisibility(View.VISIBLE);

        tv_forget_password = findViewById(R.id.tv_forget_password);
        tv_phone_choose = findViewById(R.id.tv_phone_choose);
        tv_user_agreement = findViewById(R.id.tv_user_agreement);
        btn_login = findViewById(R.id.btn_login);
        ll_phone_choose = findViewById(R.id.ll_phone_choose);
        cet_phone = findViewById(R.id.cet_phone);
        cet_login_password = findViewById(R.id.cet_login_password);
        tv_phone_choose.setText("+" + App.lastAreaCode);

        cet_phone.setText(App.lastAccount);
    }

    @Override
    protected void initListener() {
        rightTxt.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_user_agreement.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        ll_phone_choose.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        countryList.add("+86");
        countryList.add("+87");
        countryList.add("+88");
        countryList.add("+89");

        mSpinerPopWindow = new SpinerPopWindow<>(this, countryList, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTxt:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_login:
                String areaCode = tv_phone_choose.getText().toString().trim();
                String phone = cet_phone.getText().toString().trim();
                areaCode = areaCode.substring(1, areaCode.length());
                if (StringUtil.isSpace(phone)){
                    toast(getString(R.string.please_input_phone_number));
                    break;
                }
                /*if (!StringUtil.isPhone(phone)){
                    toast(getString(R.string.please_input_correct_phone_number));
                    break;
                }*/
                String passwordStr = cet_login_password.getText().toString().trim();
                if (StringUtil.isSpace(passwordStr)){
                    toast(getString(R.string.please_input_password));
                    break;
                }
//                startActivity(new Intent(LoginActivity.this, MedicineBoxSettingActivity.class));
                userLogin(phone, areaCode, passwordStr);
                break;
            case R.id.ll_phone_choose:
                startActivityForResult(new Intent(this, CountryPickActivity.class), 111);
                break;
            case R.id.tv_user_agreement:
                Intent urlIntent = new Intent(LoginActivity.this, WebShowActivity.class);
                urlIntent.putExtra("url", "http://www.baidu.com/");
                startActivity(urlIntent);
                break;
        }
    }

    public static void reStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            tv_phone_choose.setText("+" + country.code);
        }
    }

    /*
    * spinnerWindow的子控件点击事件
    * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tv_phone_choose.setText(countryList.get(position));
        mSpinerPopWindow.dismiss();
    }


    /*
     * App3/用户登录 > 使用用户名+密码登录
     * */
    private void userLogin(final String account, final String areaCode, final String password){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.userLogin(account, areaCode, password, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.LoginMsgAll data = new Gson().fromJson(result, Bean.LoginMsgAll.class);

                            if (1 == data.status){
                                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, data.model.oid));
                                //如果未连接主机则跳转连接主机界面,如果已选择跳转药盒设置界面
                                if (App.isMainEngineSelect){
                                    startActivity(new Intent(LoginActivity.this, MedicineBoxSettingActivity.class));
                                }else {
                                    startActivity(new Intent(LoginActivity.this, MainEngineConnectActivity.class));
                                }
                                App.finishRealAllActivity();
                                SPUtil.saveData(LoginActivity.this, "islogin", true);
                                SPUtil.saveData(LoginActivity.this, "token", data.model.token);
                                SPUtil.saveBean2Sp(LoginActivity.this, data.model, "loginMsg", "loginMsg");
                                SPUtil.saveData(LoginActivity.this, "lastAccount", account);
                                SPUtil.saveData(LoginActivity.this, "lastAreaCode", areaCode);
                                App.islogin = true;
                                App.token = data.model.token;
                                App.loginMsg = data.model;
                                App.lastAccount = account;
                                App.lastAreaCode = areaCode;

                                if ("1".equals(App.loginMsg.host1_connectionState)){    //如果主机1是连接状态，默认显示主机1选中
                                    SPUtil.saveData(LoginActivity.this, "hostOid", data.model.host1_oid);
                                    SPUtil.saveData(LoginActivity.this, "hostCode", data.model.host1_hostNumber);
                                    App.hostOid = data.model.host1_oid;
                                    App.hostCode = data.model.host1_hostNumber;
                                }else if ("1".equals(App.loginMsg.host2_connectionState)){  //如果主机1未连接，主机2连接，默认主机2选中
                                    SPUtil.saveData(LoginActivity.this, "hostOid", data.model.host2_oid);
                                    SPUtil.saveData(LoginActivity.this, "hostCode", data.model.host2_hostNumber);
                                    App.hostOid = data.model.host2_oid;
                                    App.hostCode = data.model.host2_hostNumber;
                                }

                                toast(data.message);
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            toast(response);
                            if (App.isMainEngineSelect){
                                startActivity(new Intent(LoginActivity.this, MedicineBoxSettingActivity.class));
                            }else {
                                startActivity(new Intent(LoginActivity.this, MainEngineConnectActivity.class));
                            }
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            if (App.isMainEngineSelect){
                                startActivity(new Intent(LoginActivity.this, MedicineBoxSettingActivity.class));
                            }else {
                                startActivity(new Intent(LoginActivity.this, MainEngineConnectActivity.class));
                            }
                        }

                        @Override
                        public void onTokenError(String response) {
                            toast(response);
                            if (App.isMainEngineSelect){
                                startActivity(new Intent(LoginActivity.this, MedicineBoxSettingActivity.class));
                            }else {
                                startActivity(new Intent(LoginActivity.this, MainEngineConnectActivity.class));
                            }
                        }
                    });
                }
            });
        }
    }

}
