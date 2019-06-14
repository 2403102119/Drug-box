package com.tckkj.medicinebox.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.MainActivity;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.InputDialogUtil;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.util.country.Country;
import com.tckkj.medicinebox.view.ClearEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.SinglePicker;
import okhttp3.Call;

/*
* 用户信息
* */
public class UserMessageActivity extends BaseActivity {
    private LinearLayout ll_user_message_name, ll_user_message_sex, ll_user_message_birthday,
            ll_user_message_age, ll_emergency_contact_phone_one,ll_emergency_contact_phone_two;
    private TextView tv_user_message_name, tv_user_message_sex, tv_user_message_birthday,
            tv_user_message_age, tv_emergency_contact_phone_one,tv_emergency_contact_phone_two;
    private Button btn_user_message_save, btn_user_message_empty;
    private int birYear = 1900, birMonth = 1, birDay = 1;
    private TextView tv_phone_choose;
    private String areaCode1;
    private String areaCode2;
    private String pho1;
    private String pho2;
    private Calendar cal;
    private int year,month,day;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_user_message);

        title.setText(R.string.userinfo);

        ll_user_message_name = findViewById(R.id.ll_user_message_name);
        ll_user_message_sex = findViewById(R.id.ll_user_message_sex);
        ll_user_message_birthday = findViewById(R.id.ll_user_message_birthday);
        ll_user_message_age = findViewById(R.id.ll_user_message_age);
        ll_emergency_contact_phone_one = findViewById(R.id.ll_emergency_contact_phone_one);
        ll_emergency_contact_phone_two = findViewById(R.id.ll_emergency_contact_phone_two);
        tv_user_message_name = findViewById(R.id.tv_user_message_name);
        tv_user_message_sex = findViewById(R.id.tv_user_message_sex);
        tv_user_message_birthday = findViewById(R.id.tv_user_message_birthday);
        tv_user_message_age = findViewById(R.id.tv_user_message_age);
        tv_emergency_contact_phone_one = findViewById(R.id.tv_emergency_contact_phone_one);
        tv_emergency_contact_phone_two = findViewById(R.id.tv_emergency_contact_phone_two);
        btn_user_message_save = findViewById(R.id.btn_user_message_save);
        btn_user_message_empty = findViewById(R.id.btn_user_message_empty);
       /* if (App.islogin && null != App.loginMsg){
            tv_user_message_name.setText(App.loginMsg.nickName);
        }*/
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        ll_user_message_name.setOnClickListener(this);
        ll_user_message_sex.setOnClickListener(this);
        ll_user_message_birthday.setOnClickListener(this);
        ll_user_message_age.setOnClickListener(this);
        ll_emergency_contact_phone_one.setOnClickListener(this);
        ll_emergency_contact_phone_two.setOnClickListener(this);
        btn_user_message_save.setOnClickListener(this);
        btn_user_message_empty.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Getuserinformation();
        getDate();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            tv_phone_choose.setText("+" + country.code);
            areaCode1 = "+"+ country.code;
            areaCode2 = "+"+country.code;

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.ll_user_message_name:
                String x;
                if (tv_user_message_name.getText().toString().equals("未设置")){
                    x="";
                }else {
                    x=tv_user_message_name.getText().toString();
                }

                final InputDialogUtil nameDialogUtil = new InputDialogUtil(this, true, getString(R.string.nickname), true,InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT,x);
                nameDialogUtil.setOnConfirmListener(new InputDialogUtil.OnConfirmListener() {
                    @Override
                    public void onClick(String inputContent) {
                        tv_user_message_name.setText(inputContent);
                        nameDialogUtil.cancel();
                    }
                });
                nameDialogUtil.show();
                break;
            case R.id.ll_user_message_sex:
                List<String> list = new ArrayList<>();
                list.add(getString(R.string.man));
                list.add(getString(R.string.women));
                Picker(list, getString(R.string.sex_selection));
                break;
            case R.id.ll_user_message_birthday://选择生日
                int  x0=1990;
                int  x1=1;
                int  x2=1;
                if ("未设置".equals(tv_user_message_birthday.getText().toString())){
                    x0 = 1990;
                    x1 =1;
                    x2 = 1;
                }else if (tv_user_message_birthday.getText().toString().equals("")){
                    x0 = 1990;
                    x1 =1;
                    x2 = 1;
                }else {
                    String[] a9 =tv_user_message_birthday.getText().toString().split("-");
                    for (int i = 0; i <a9.length ; i++) {
                        x0 = Integer.parseInt(a9[0]);
                        x1 =Integer.parseInt(a9[1]);
                        x2 = Integer.parseInt(a9[2]);

                    }
                }
//
//
//                dateSetting(x0,x1,x2);
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        tv_user_message_birthday.setText(year+"-"+(++month)+"-"+dayOfMonth);
                    }

//                    @Override
//                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
//                          //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
//                    }
                };
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                DatePickerDialog dialog=new DatePickerDialog(UserMessageActivity.this, DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);
                DatePickerDialog dialog=new DatePickerDialog(UserMessageActivity.this, DatePickerDialog.THEME_HOLO_LIGHT,listener,x0,x1-1,x2);
                //主题在这里！后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                //当结束时间已经选择而且是点击开始时间弹出的picker
                if(!TextUtils.isEmpty(year + "-" + (month + 1) + "-" + day)){
                    try {
                        Date date=sdf.parse(year + "-" + (month + 1) + "-" + day);
                        //设置最大可选择时间
                        dialog.getDatePicker().setMaxDate(date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                dialog.show();
                break;



            case R.id.ll_user_message_age:
                final InputDialogUtil ageDialogUtil = new InputDialogUtil(this, true, getString(R.string.age), true, InputType.TYPE_CLASS_NUMBER,"");
                ageDialogUtil.setOnConfirmListener(new InputDialogUtil.OnConfirmListener() {
                    @Override
                    public void onClick(String inputContent) {
                        tv_user_message_age.setText(inputContent);
                        ageDialogUtil.cancel();
                    }
                });
                ageDialogUtil.show();
                break;
            case R.id.ll_emergency_contact_phone_one:
                String b1 = "";
                String c1 = "+86";
                if ("未设置".equals(tv_emergency_contact_phone_one.getText().toString()))
                {
                    b1="";
                    c1="+86";
                } else if (tv_emergency_contact_phone_one.getText().toString().equals("")){
                    b1="";
                    c1="+86";
                }else {
                    String[] a1 = tv_emergency_contact_phone_one.getText().toString().split(" ");

                    for (int i = 0; i <a1.length ; i++) {
                        b1 = a1[1];
                        c1 = a1[0];
                    }
                }

                showPhoneInputDialog(getString(R.string.emergency_contact_number1), tv_emergency_contact_phone_one,b1,c1);
                /*final InputDialogUtil phoneOneDialogUtil = new InputDialogUtil(this, true, getString(R.string.emergency_contact_number1), true, InputType.TYPE_CLASS_PHONE);
                phoneOneDialogUtil.setOnConfirmListener(new InputDialogUtil.OnConfirmListener() {
                    @Override
                    public void onClick(String inputContent) {
                        tv_emergency_contact_phone_one.setText(inputContent);
                        phoneOneDialogUtil.cancel();
                    }
                 });
                phoneOneDialogUtil.show();*/
                break;
            case R.id.ll_emergency_contact_phone_two:
                String d = "";
                String e = "+86";
                if ("未设置".equals(tv_emergency_contact_phone_two.getText().toString())){
                    d="";
                    e = "+86";
                }else if ( tv_emergency_contact_phone_two.getText().toString().equals("")){
                    d="";
                    e = "+86";
                }else {
                    String[] c = tv_emergency_contact_phone_two.getText().toString().split(" ");

                    for (int i = 0; i <c.length ; i++) {
                        d =c[1];
                        e = c[0];

                    }
                }

                showPhoneInputDialog(getString(R.string.emergency_contact_number2), tv_emergency_contact_phone_two,d,e);
                /*final InputDialogUtil phoneTwoDialogUtil = new InputDialogUtil(this, true, getString(R.string.emergency_contact_number2), true, InputType.TYPE_CLASS_PHONE);
                phoneTwoDialogUtil.setOnConfirmListener(new InputDialogUtil.OnConfirmListener() {
                    @Override
                    public void onClick(String inputContent) {
                        tv_emergency_contact_phone_two.setText(inputContent);
                        phoneTwoDialogUtil.cancel();
                    }
                });
                phoneTwoDialogUtil.show();*/
                break;
            case R.id.btn_user_message_save:
                String nickName = tv_user_message_name.getText().toString().trim();
                String sex = tv_user_message_sex.getText().toString().trim();
                String age = tv_user_message_age.getText().toString().trim();
                String birthDate = tv_user_message_birthday.getText().toString().trim();
                String phone1 = tv_emergency_contact_phone_one.getText().toString().trim();
                String phone2 = tv_emergency_contact_phone_two.getText().toString().trim();
                if (phone1.equals("")){
                    areaCode1= "";
                    pho1 = "";
                }else {
                    String[] a=phone1.split(" ");
                    for (int i = 0; i < a.length; i++) {
                        areaCode1=a[0].substring(1);
                        pho1 = a[1];
                    }
                }

                if (phone2.equals("")){
                    areaCode2= "";
                    pho2 = "";
                }else {

                    String[] b=phone2.split(" ");
                    for (int i = 0; i < b.length; i++) {
                        areaCode2=b[0].substring(1);
                        pho2 = b[1];
                    }
                }

                if (getString(R.string.is_not_set).equals(nickName) || getString(R.string.is_not_set).equals(sex) ||
                        getString(R.string.is_not_set).equals(birthDate) || getString(R.string.is_not_set).equals(phone1)){
                    toast(getString(R.string.user_info_remind));
                    break;
                }
                /*if (!StringUtil.isPhone(phone1) || (getString(R.string.is_not_set).equals(StringUtil.isSpace(phone2)) && !StringUtil.isPhone(phone2))){
                    toast(getString(R.string.please_input_correct_phone_number));
                    break;
                }*/
                String sexCode = "";
                if (getString(R.string.man).equals(tv_user_message_sex.getText().toString())){
                    sexCode = "1";
                }
                if (getString(R.string.women).equals(tv_user_message_sex.getText().toString())){
                    sexCode = "2";
                }
                if (getString(R.string.is_not_set).equals(phone2)){
                    phone2 = "";
                }
                if (App.islogin) {
                    if (StringUtil.isSpace(App.hostOid)){
                        InputDialogUtil.showHostConnectTipDialog(this);
                    }else {
                        updateUserInfo(nickName, sexCode,birthDate, pho1, pho2,areaCode1,areaCode2);
                    }
                }else {
                    InputDialogUtil.showLoginTipDialog(this);
                }

                break;
            case R.id.btn_user_message_empty:
                deleteUserInfo();
                break;
        }
    }
    //获取当前日期
    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        Log.i("wxy","year"+year);
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }



    /*
    * 展示联系人填写弹窗
    * */
    private void showPhoneInputDialog(String title, TextView tv,String b,String c) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_phone_input, null);
        Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_input_title = view.findViewById(R.id.tv_input_title);
        Button btn_input_confirm = view.findViewById(R.id.btn_input_confirm);
        final ClearEditText cet_phone = view.findViewById(R.id.cet_phone);
        final LinearLayout ll_phone_choose = view.findViewById(R.id.ll_phone_choose);
        tv_phone_choose = view.findViewById(R.id.tv_phone_choose);
        tv_input_title.setText(title);
        cet_phone.setText(b);
        tv_phone_choose.setText(c);
        ll_phone_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(UserMessageActivity.this, CountryPickActivity.class), 111);
            }
        });

        btn_input_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (StringUtil.isSpace(cet_phone.getText().toString())){
//
//                    toast(getString(R.string.please_input_content));
//                    return;
//                }
                    if (cet_phone.getText().toString().equals("")){
                        tv.setText("");
                    }else {
                        tv.setText(tv_phone_choose.getText().toString().trim() + " " + cet_phone.getText().toString().trim());
                    }
                dialog.cancel();
            }
        });
        dialog.show();
    }



    /*
    * 设置生日
    * */
    private void dateSetting(int x,int x1,int x2) {
        Calendar cal= Calendar.getInstance();
        DatePicker datePicker = new DatePicker(this);
        datePicker.setRangeStart(1900, 1,1);    //设置起始选择时间
        datePicker.setRangeEnd(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));    //设置选择的结束时间
        datePicker.setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day));
        datePicker.setSelectedItem(x, x1, x2);             //设置默认显示时间
        datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String s, String s1, String s2) {
                tv_user_message_birthday.setText(s + "-" + s1 + "-" + s2);

                birYear = Integer.parseInt(s);
                birMonth = Integer.parseInt(s1);
                birDay = Integer.parseInt(s2);
            }
        });
        datePicker.show();
    }

    /*
    * 设置性别
    * */
    private void Picker(List<String> list, String title) {
        SinglePicker picker = new SinglePicker(this, list);
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(ContextCompat.getColor(this, R.color.white));
        picker.setTopHeight(45);
        picker.setTopLineColor(ContextCompat.getColor(this, R.color.white));
        picker.setTopLineHeight(1);
        picker.setTitleText(title);
        picker.setTitleTextColor(ContextCompat.getColor(this, R.color.normalText));
        picker.setTitleTextSize(14);
        picker.setCancelTextColor(ContextCompat.getColor(this, R.color.normalText));
        picker.setCancelTextSize(14);
        picker.setSubmitTextColor(ContextCompat.getColor(this, R.color.normalText));
        picker.setSubmitTextSize(14);
        picker.setSelectedTextColor(ContextCompat.getColor(this, R.color.normalText));
        picker.setUnSelectedTextColor(ContextCompat.getColor(this, R.color.hintColor));
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(ContextCompat.getColor(this, R.color.hintColor));//线颜色
        config.setAlpha(120);//线透明度
        picker.setLineConfig(config);
        picker.setItemWidth(300);
        picker.setBackgroundColor(ContextCompat.getColor(this, R.color.appBg));
        picker.setSelectedIndex(0);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                tv_user_message_sex.setText(item);
            }
        });
        picker.show();
    }

    /*
     * App6/获取用户资料 >
     * */
    private void Getuserinformation(){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.Getuserinformation(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.Get_user_information data = new Gson().fromJson(result, Bean.Get_user_information.class);
                            if (data.status==1){
                                tv_user_message_name.setText(data.model.nickName);
                                if (data.model.sex.equals("1")){
                                    tv_user_message_sex.setText("男");
                                }else if (data.model.sex.equals("2")){
                                    tv_user_message_sex.setText("女");
                                }else {
                                    tv_user_message_sex.setText("");
                                }

                                tv_user_message_birthday.setText(data.model.date);
                                tv_emergency_contact_phone_one.setText(data.model.areaCode1+" "+data.model.phone1);
                                tv_emergency_contact_phone_two.setText(data.model.areaCode2+" "+data.model.phone2);
                                toast(data.message);
                      //TODO
//                                Intent intent =new Intent();
//                                intent.putExtra("namehei",data.model.nickName);
//                                setResult(222, intent);
//                                finish();
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
     * App14/修改用户信息 >
     * */
    private void updateUserInfo( final String nickName, final String sex, final String date, final String phone1, final String phone2,final String areaCode1,final String areaCode2){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateUserInfo(nickName, sex, date, phone1, phone2,areaCode1,areaCode2, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.ChangeUserMsgAll data = new Gson().fromJson(result, Bean.ChangeUserMsgAll.class);

                            if ("1".equals(data.status)){
                                Getuserinformation();

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


    /*
     * App15/清空用户资料 >
     * */
    private void deleteUserInfo(){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.deleteUserInfo(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if ("1".equals(data.status)){
                                tv_user_message_name.setText(R.string.is_not_set);
                                tv_user_message_sex.setText(R.string.is_not_set);
                                tv_user_message_birthday.setText(R.string.is_not_set);
                                tv_user_message_age.setText(R.string.is_not_set);
                                tv_emergency_contact_phone_one.setText(R.string.is_not_set);
                                tv_emergency_contact_phone_two.setText(R.string.is_not_set);
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
