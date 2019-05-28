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
* 忘记密码
* */
public class ForgetPasswordActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private LinearLayout ll_phone_choose;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> countryList = new ArrayList<>();
    private TextView tv_phone_choose, tv_get_verification;
    private ClearEditText cet_phone, cet_input_verification, cet_password;
    private Button btn_confirm;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_forget_password);

        ll_phone_choose = findViewById(R.id.ll_phone_choose);
        tv_phone_choose = findViewById(R.id.tv_phone_choose);
        tv_get_verification = findViewById(R.id.tv_get_verification);
        cet_phone = findViewById(R.id.cet_phone);
        cet_input_verification = findViewById(R.id.cet_input_verification);
        cet_password = findViewById(R.id.cet_password);
        btn_confirm = findViewById(R.id.btn_confirm);

        tv_phone_choose.setText("+" + App.lastAreaCode);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        ll_phone_choose.setOnClickListener(this);
        tv_get_verification.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
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
            case R.id.ll_phone_choose:
//                mSpinerPopWindow.setWidth(ll_phone_choose.getWidth());
//                mSpinerPopWindow.showAsDropDown(ll_phone_choose);
                startActivityForResult(new Intent(this, CountryPickActivity.class), 111);
                break;
            case R.id.tv_get_verification:
                String areaCode = tv_phone_choose.getText().toString().trim();
                String phoneStr = cet_phone.getText().toString().trim();
                areaCode = areaCode.substring(1, areaCode.length());
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
            case R.id.btn_confirm:
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
                    toast(getString(R.string.please_input_new_password));
                    break;
                }
                updateUserPasswordByAccount(verificationStr, areaCode2, phone, passwordStr);
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
                    httpInterface.sendYzm("modify", areaCode, account, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if ("1".equals(data.status)){
                                toast("验证码发送成功");
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
     * App4/忘记密码 >
     * */
    private void updateUserPasswordByAccount(final String yzm, final String areaCode, final String account, final String password){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateUserPasswordByAccount(yzm, areaCode, account, password, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if ("1".equals(data.status)){
                                finish();
                                toast(data.message);
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
