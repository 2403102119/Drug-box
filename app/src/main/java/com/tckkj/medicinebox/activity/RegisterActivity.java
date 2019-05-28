package com.tckkj.medicinebox.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.util.country.Country;
import com.tckkj.medicinebox.view.ClearEditText;
import com.tckkj.medicinebox.view.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/*
* 注册
* */
public class RegisterActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private TextView tv_forget_password, tv_phone_choose, tv_user_agreement, tv_get_verification;
    private LinearLayout ll_phone_choose;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> countryList = new ArrayList<>();
    private Button btn_register;
    private ClearEditText cet_phone, cet_input_verification, cet_password;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_register);

        tv_forget_password = findViewById(R.id.tv_forget_password);
        tv_phone_choose = findViewById(R.id.tv_phone_choose);
        tv_user_agreement = findViewById(R.id.tv_user_agreement);
        tv_get_verification = findViewById(R.id.tv_get_verification);
        ll_phone_choose = findViewById(R.id.ll_phone_choose);
        btn_register = findViewById(R.id.btn_register);
        cet_phone = findViewById(R.id.cet_phone);
        cet_input_verification = findViewById(R.id.cet_input_verification);
        cet_password = findViewById(R.id.cet_password);

        tv_phone_choose.setText("+" + App.lastAreaCode);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        ll_phone_choose.setOnClickListener(this);
        tv_user_agreement.setOnClickListener(this);
        tv_get_verification.setOnClickListener(this);
        btn_register.setOnClickListener(this);
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
            case R.id.back:
                finish();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(RegisterActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.ll_phone_choose:
                /*mSpinerPopWindow.setWidth(ll_phone_choose.getWidth());
                mSpinerPopWindow.showAsDropDown(ll_phone_choose);*/
                startActivityForResult(new Intent(this, CountryPickActivity.class), 111);
                break;
            case R.id.tv_user_agreement:
                Intent urlIntent = new Intent(RegisterActivity.this, WebShowActivity.class);
                urlIntent.putExtra("url", "http://www.baidu.com/");
                startActivity(urlIntent);
                break;
            case R.id.tv_get_verification:
                String phoneStr = cet_phone.getText().toString().trim();
                String areaCode = tv_phone_choose.getText().toString().trim();
                areaCode = areaCode.substring(1,areaCode.length());
                if (StringUtil.isSpace(phoneStr)){
                    toast(getString(R.string.please_input_phone_number));
                    break;
                }
                /*if (!StringUtil.isPhone(phoneStr)){
                    toast(getString(R.string.please_input_correct_phone_number));
                    break;
                }*/
                getYzm(areaCode, phoneStr);
                break;
            case R.id.btn_register:
                String areaCode2 = tv_phone_choose.getText().toString().trim();
                String phone = cet_phone.getText().toString().trim();
                areaCode2 = areaCode2.substring(1, areaCode2.length());
                if (StringUtil.isSpace(phone)){
                    toast(getString(R.string.please_input_phone_number));
                    break;
                }
                /*if (!StringUtil.isPhone(phone)){
                    toast(getString(R.string.please_input_correct_phone_number));
                    break;
                }*/
                String verificationStr = cet_input_verification.getText().toString().trim();
                if (StringUtil.isSpace(verificationStr)){
                    toast(getString(R.string.please_input_verification));
                    break;
                }
                String passwordStr = cet_password.getText().toString().trim();
                if (StringUtil.isSpace(passwordStr)){
                    toast(getString(R.string.please_input_password));
                    break;
                }

                addUser(verificationStr, areaCode2, phone, passwordStr);

                break;
        }
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
    * App1/获取验证码 >
    * */
    private void getYzm(final String areaCode, final String account){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.sendYzm("register",areaCode, account, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if ("1".equals(data.status)){
                                toast(getString(R.string.code_sent_successfully));
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }


    /*
     * App2/用户注册 >
     * */
    private void addUser(final String yzm, final String areaCode, final String account, final String password){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.addUser(yzm, areaCode, account, password, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.RegisterMsg data = new Gson().fromJson(result, Bean.RegisterMsg.class);

                            if (1 == data.status){
                                toast(data.message);
                                finish();
                            }else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

}
