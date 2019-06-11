package com.tckkj.medicinebox.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.adapter.MedicineWarehouseBannerHolder;
import com.tckkj.medicinebox.adapter.RemindTimeAdapter;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.InputDialogUtil;
import com.tckkj.medicinebox.util.MUIToast;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.view.ClearEditText;
import com.tckkj.medicinebox.view.NiceRecyclerView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.addapp.pickers.picker.DatePicker;
import okhttp3.Call;

/*
* 药盒管理
* */
public class
BoxManagerActivity extends BaseActivity {
    private LinearLayout ll_medicine_name, ll_drug_expired_date, ll_add_medicine_number,
            ll_medicine_number_everyday, ll_take_someday, ll_take_medicine_time;
    private MZBannerView banner_medicine_warehouse;
    private List<Integer> list = new ArrayList<>();
    private TextView tv_medicine_warehouse_code, tv_drug_expired_date, tv_add_medicine_number,
            tv_surplus_medicine_number, tv_medicine_number_everyday, tv_take_medicine_weekdays,
            tv_add_medicine_time, tv_medicine_name, tv_host_number, tv_host_name, tv_box_manager_save;
    private NiceRecyclerView nrv_remind_time;
    private NestedScrollView nsv_box_manager;
    private RemindTimeAdapter adapter;
    private List<String> timeList = new ArrayList<>();

    private Handler handler = new Handler();
    public String oid1;
    private int curPosition;
    private String a;
    private String b;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_box_manager);

        title.setText(R.string.medicine_management);
        rightTxt.setText(R.string.empty);
        rightTxt.setVisibility(View.VISIBLE);

        ll_medicine_name = findViewById(R.id.ll_medicine_name);                     //药品名称
        ll_drug_expired_date = findViewById(R.id.ll_drug_expired_date);             //药品到期日
        ll_add_medicine_number = findViewById(R.id.ll_add_medicine_number);         //本次加药量
        ll_medicine_number_everyday = findViewById(R.id.ll_medicine_number_everyday);//每日服用剂量
        ll_take_someday = findViewById(R.id.ll_take_someday);                       //服药日期（非日服）
        ll_take_medicine_time = findViewById(R.id.ll_take_medicine_time);           //服药时间

        tv_medicine_warehouse_code = findViewById(R.id.tv_medicine_warehouse_code); //药品名称
        tv_drug_expired_date = findViewById(R.id.tv_drug_expired_date);             //药品到期日
        tv_add_medicine_number = findViewById(R.id.tv_add_medicine_number);         //本次加药量
        tv_surplus_medicine_number = findViewById(R.id.tv_surplus_medicine_number);      //剩余药品量
        tv_medicine_number_everyday = findViewById(R.id.tv_medicine_number_everyday);    //每日服用剂量
        tv_take_medicine_weekdays = findViewById(R.id.tv_take_medicine_weekdays);         //每周服药日期
        tv_add_medicine_time = findViewById(R.id.tv_add_medicine_time);             //添加服药时间
        tv_medicine_name = findViewById(R.id.tv_medicine_name);                     //药品名称
        tv_host_number = findViewById(R.id.tv_host_number);                         //主机编号
        tv_host_name = findViewById(R.id.tv_host_name);                             //主机名称
        tv_box_manager_save = findViewById(R.id.tv_box_manager_save);             //保存

        banner_medicine_warehouse = findViewById(R.id.banner_medicine_warehouse);
        nrv_remind_time = findViewById(R.id.nrv_remind_time);
        nsv_box_manager = findViewById(R.id.nsv_box_manager);

        getStorehouseDataByNumber(1 + "");
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
        ll_medicine_name.setOnClickListener(this);
        ll_drug_expired_date.setOnClickListener(this);
        ll_add_medicine_number.setOnClickListener(this);
        ll_medicine_number_everyday.setOnClickListener(this);
        ll_take_someday.setOnClickListener(this);
        tv_add_medicine_time.setOnClickListener(this);
        tv_box_manager_save.setOnClickListener(this);
    }

    @Override
    protected void initData() {
         a = getIntent().getStringExtra("number");
         b = getIntent().getStringExtra("name");
//        list.add(R.mipmap.box_check_no_one);
//        list.add(R.mipmap.box_check_no_two);
//        list.add(R.mipmap.box_check_no_three);
//        list.add(R.mipmap.box_check_no_four);
//        list.add(R.mipmap.box_check_no_five);
//        list.add(R.mipmap.box_check_no_six);

//        getStorehouseDataByNumber("1");
        tv_host_number.setText(a);
        tv_host_name.setText(b);
        list.add(R.mipmap.box_check_yes_one);
        list.add(R.mipmap.box_check_yes_two);
        list.add(R.mipmap.box_check_yes_three);
        list.add(R.mipmap.box_check_yes_four);
        list.add(R.mipmap.box_check_yes_five);
        list.add(R.mipmap.box_check_yes_six);
        list.add(R.mipmap.box_check_yes_seven);
        list.add(R.mipmap.box_check_yes_eight);

        banner_medicine_warehouse.pause();
        banner_medicine_warehouse.setIndicatorVisible(false);
        banner_medicine_warehouse.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0){
                    banner_medicine_warehouse.setCanLoop(false);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                resetPic();
                switch (position){
                    case 0:
                        list.set(0, R.mipmap.box_check_yes_one);
                        break;
                    case 1:
                        list.set(1, R.mipmap.box_check_yes_two);
                        break;
                    case 2:
                        list.set(2, R.mipmap.box_check_yes_three);
                        break;
                    case 3:
                        list.set(3, R.mipmap.box_check_yes_four);
                        break;
                    case 4:
                        list.set(4, R.mipmap.box_check_yes_five);
                        break;
                    case 5:
                        list.set(5, R.mipmap.box_check_yes_six);
                        break;
                    case 6:
                        list.set(6,R.mipmap.box_check_yes_seven);
                        break;
                    case 7:
                        list.set(7,R.mipmap.box_check_yes_eight);
                        break;

                }
                tv_medicine_warehouse_code.setText(position + 1 + "");
                getStorehouseDataByNumber(position + 1 + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        banner_medicine_warehouse.setPages(list, new MZHolderCreator() {
            @Override
            public MZViewHolder createViewHolder() {
                return new MedicineWarehouseBannerHolder();
            }
        });

        adapter = new RemindTimeAdapter(this, timeList);
        nrv_remind_time.setAdapter(adapter);
        adapter.setOnItemClickListener(new RemindTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                curPosition = position;
                String timeStr = timeList.get(position);
                String time[] = timeStr.split(":");

                Intent remindIntent = new Intent(BoxManagerActivity.this, TakeMedicineTimeActivity.class);
                remindIntent.putExtra("iaAdd", false);
                remindIntent.putExtra("hour", time[0]);
                remindIntent.putExtra("minute", time[0]);
                startActivityForResult(remindIntent, 121);
            }

            @Override
            public void onDelete(int position) {
                timeList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getStorehouseDataByNumber(String number) {
        if (App.islogin){
            if (StringUtil.isSpace(App.hostOid)){
                InputDialogUtil.showHostConnectTipDialog(this);
            }else {
                getStorehouseData(number, App.hostOid);
            }
        }else {
            InputDialogUtil.showLoginTipDialog(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rightTxt:
                if (App.islogin){
                    if (StringUtil.isSpace(oid1)){
                        InputDialogUtil.showHostConnectTipDialog(this);
                    }else {
                        deleteStorehouseData(oid1);
                    }
                }else {
                    InputDialogUtil.showLoginTipDialog(this);
                }
                break;
            case R.id.ll_medicine_name:     //药品名称
                showDialog();
                break;
            case R.id.ll_drug_expired_date:
                dateSetting();
                break;
            case R.id.ll_add_medicine_number:
                final InputDialogUtil nameDialogUtil = new InputDialogUtil(this, true, getString(R.string.dosage_for_this_addition), true, InputType.TYPE_CLASS_NUMBER);
                nameDialogUtil.setOnConfirmListener(new InputDialogUtil.OnConfirmListener() {
                    @Override
                    public void onClick(String inputContent) {
                        tv_add_medicine_number.setText(inputContent);
                        nameDialogUtil.cancel();
                    }
                });
                nameDialogUtil.show();
                break;
            case R.id.ll_medicine_number_everyday:
                final InputDialogUtil nameDialogUtil2 = new InputDialogUtil(this, true, getString(R.string.daily_dose), true, InputType.TYPE_CLASS_NUMBER);
                nameDialogUtil2.setOnConfirmListener(new InputDialogUtil.OnConfirmListener() {
                    @Override
                    public void onClick(String inputContent) {
                        tv_medicine_number_everyday.setText(inputContent);
                        nameDialogUtil2.cancel();
                    }
                });
                nameDialogUtil2.show();
                break;
            case R.id.ll_take_someday:      //非日服
                Intent intent = new Intent(BoxManagerActivity.this, UnTakeEverydaytooActivity.class);
                intent.putExtra("medicineTime", tv_take_medicine_weekdays.getText().toString().trim());
                startActivityForResult(intent, 111);
                break;
            case R.id.tv_add_medicine_time:    //服药时间
                Intent remindIntent = new Intent(BoxManagerActivity.this, TakeMedicineTimeActivity.class);
                remindIntent.putExtra("iaAdd", true);
                startActivityForResult(remindIntent, 113);
                break;
            case R.id.tv_box_manager_save:
                String medicineName = tv_medicine_name.getText().toString().trim();
                String expireDate = tv_drug_expired_date.getText().toString().trim();
                String addNumber = tv_add_medicine_number.getText().toString().trim();
                String everydayNumber = tv_medicine_number_everyday.getText().toString().trim();
                String takeWeekdays = tv_take_medicine_weekdays.getText().toString().trim();
                if (getString(R.string.is_not_set).equals(medicineName) || getString(R.string.is_not_set).equals(expireDate) ||
                        getString(R.string.is_not_set).equals(takeWeekdays) || "0".equals(addNumber) || "0".equals(everydayNumber) ||
                        0 == timeList.size()){
                    toast(getString(R.string.user_info_remind));
                    break;
                }
                String times = "";
                for (int i = 0; i < timeList.size(); i++) {
                    times += timeList.get(i) + "#";
                }
               String a = times.substring(0, times.length());
                String c = a.substring(0,a.length() - 1);
                if (takeWeekdays.equals("每天")){
                    updateMedicineStorehouse(addNumber,oid1, medicineName, everydayNumber, expireDate, "9999",c);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (111 == requestCode && 222 == resultCode){
            if (null != data){
                tv_take_medicine_weekdays.setText(data.getStringExtra("weekdays"));
            }
        }

        if (requestCode == 113 && resultCode == 222){
            if (null != data){
                String time = data.getStringExtra("time");
                timeList.add(time);
                adapter.notifyDataSetChanged();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        nsv_box_manager.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }

        if (requestCode == 121 && resultCode == 222){
            if (null != data){
                String time = data.getStringExtra("time");
                timeList.set(curPosition, time);
                adapter.notifyDataSetChanged();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        nsv_box_manager.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /*
     * 设置药品到期日
     * */
    private void dateSetting() {
        Calendar cal= Calendar.getInstance();
        DatePicker datePicker = new DatePicker(this);
        datePicker.setRangeStart(cal.get(Calendar.YEAR), 1, cal.get(Calendar.DATE));    //设置起始选择时间
        datePicker.setRangeEnd(2100, 12, 31);    //设置选择的结束时间
        datePicker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));             //设置默认显示时间
        datePicker.setCanLoop(false);
        datePicker.setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day));
        datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String s, String s1, String s2) {
                tv_drug_expired_date.setText(s + "-" + s1 + "-" + s2);
            }
        });
        datePicker.show();
    }

    /*
    * 药品名称弹窗
    * */
    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_medicine_name, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Button btn_medicine_name_confirm = view.findViewById(R.id.btn_medicine_name_confirm);
        ImageView img_scan_code = view.findViewById(R.id.img_scan_code);
        final ClearEditText cet_medicine_name = view.findViewById(R.id.cet_medicine_name);
        btn_medicine_name_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = cet_medicine_name.getText().toString().trim();
                if (StringUtil.isSpace(nameStr)){
                    toast(getString(R.string.please_input_content));
                    return;
                }
                tv_medicine_name.setText(nameStr);
                dialog.dismiss();
            }
        });
        img_scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Acp.getInstance(BoxManagerActivity.this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CAMERA)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        startActivity(new Intent(BoxManagerActivity.this, ScanCodeActivity.class));
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        MUIToast.show(BoxManagerActivity.this, permissions.toString() + "权限拒绝");
                    }
                });
            }
        });
        dialog.show();
    }

    /*
    * 重置图片资源
    * */
    private void resetPic(){
        list.set(0, R.mipmap.box_check_no_one);
        list.set(1, R.mipmap.box_check_no_two);
        list.set(2, R.mipmap.box_check_no_three);
        list.set(3, R.mipmap.box_check_no_four);
        list.set(4, R.mipmap.box_check_no_five);
        list.set(5, R.mipmap.box_check_no_six);
        list.set(6,R.mipmap.box_check_yes_seven);
        list.set(7,R.mipmap.box_check_yes_eight);
    }

    /**
     * App17/药仓新增、添加或更换药品 >
     *
     * @param dosage                本次加药量
     * @param oid                   药仓表 主键
     * @param name                  药品名字
     * @param dose                  药品剂量
     * @param termOfValidity       过期时间
     * @param wayOfTaking           服用方式：1-7周一-周日
     * @param takingTime            服药时间
     */
    private void updateMedicineStorehouse(final String dosage, final String oid, final String name,
                                          final String dose, final String termOfValidity, final String wayOfTaking,
                                          final String takingTime){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateMedicineStorehouse(dosage, oid, name, dose, termOfValidity,
                            wayOfTaking, takingTime, new MApiResultCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    Bean data = new Gson().fromJson(result, Bean.class);

                                    if (1 == data.status){
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
     * App12/药仓清空数据 >
     * */
    private void deleteStorehouseData(final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.deleteStorehouseData(oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if (1 == data.status){

                                tv_medicine_name.setText(getString(R.string.is_not_set));
                                tv_drug_expired_date.setText(getString(R.string.is_not_set));
                                tv_take_medicine_weekdays.setText(getString(R.string.is_not_set));
                                tv_add_medicine_number.setText("未设置");
                                tv_medicine_number_everyday.setText("未设置");
                                tv_surplus_medicine_number.setText("0");
                                timeList.clear();
                                adapter.notifyDataSetChanged();

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
     * App10/获取药仓信息 >
     * */
    private void getStorehouseData(final String medicineSerialNumber, final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getStorehouseData(medicineSerialNumber, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.Kit_information data = new Gson().fromJson(result, Bean.Kit_information.class);

                            if (1 == data.status){
                                if (data.model.name.equals("")){

                                    tv_medicine_name.setText(getString(R.string.is_not_set));
                                }else {
                                    tv_medicine_name.setText(data.model.name);
                                }
                                if (data.model.termOfValidity.equals("")){

                                    tv_drug_expired_date.setText(getString(R.string.is_not_set));
                                }else {
                                    tv_drug_expired_date.setText(data.model.termOfValidity);
                                }
                                oid1 = data.model.oid;
                                 if (data.model.days.equals("9999")){
                                     tv_take_medicine_weekdays.setText("每天");
                                 }else if (data.model.days.equals("")){

                                     tv_take_medicine_weekdays.setText(getString(R.string.is_not_set));
                                 }else {
                                     tv_take_medicine_weekdays.setText(data.model.days);
                                 }
                                tv_add_medicine_number.setText("未设置");
                                if (data.model.dose.equals("")){

                                    tv_medicine_number_everyday.setText("0");
                                }else {
                                    tv_medicine_number_everyday.setText(data.model.dose);
                                }
                                if (data.model.allowance.equals("")){
                                    tv_surplus_medicine_number.setText("0");
                                }else {
                                    tv_surplus_medicine_number.setText(data.model.allowance);
                                }

                                if (data.model.takingTime.equals("")){
                                    timeList.clear();
                                }else {
                                    timeList.clear();
                                    String str=data.model.takingTime;
                                    String[] strArry=str.split("[#]");
                                    for (int i = 0; i < strArry.length; i++) {
                                        timeList.add(strArry[i]);
                                    }
                                }




                                adapter.notifyDataSetChanged();

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
