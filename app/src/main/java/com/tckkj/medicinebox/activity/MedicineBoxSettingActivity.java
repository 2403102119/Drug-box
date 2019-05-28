package com.tckkj.medicinebox.activity;
/*
* 药盒设置
* */
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.thread.MApiResultCallback;
import com.tckkj.medicinebox.thread.ThreadPoolManager;
import com.tckkj.medicinebox.util.NetUtil;
import com.tckkj.medicinebox.util.SPUtil;

import okhttp3.Call;

public class MedicineBoxSettingActivity extends BaseActivity {
    private DrawerLayout dl_medicine_box_setting;
    private LinearLayout ll_user_message, ll_medicine_box_manage, ll_remind_setting,
            ll_temporary_get_medicine, ll_go_out_setting, ll_medicine_box_pandect,
            ll_medicine_knowledge, ll_push_suggest, ll_statement_message,
            ll_about_us, ll_back_connect, ll_language_choose, ll_unlogin;
    private LinearLayout ll_medicine_box_more;      //侧滑界面
    private RelativeLayout rl_main_engine_one, rl_main_engine_two;
    private ImageView img_main_engine_icon_one, img_main_engine_icon_two;
    private TextView tv_main_engine_name_one, tv_main_engine_name_two, tv_main_engine_content_one, tv_main_engine_content_two;
    private TextView tv_host_number, tv_host_name;
    private Dialog exitDialog;

    private long lastExitTime = 0l;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_medicine_box_setting);

        title.setText(R.string.PCS_set);
        img_back.setImageResource(R.mipmap.icon_more);

        ll_user_message = findViewById(R.id.ll_user_message);
        ll_medicine_box_manage = findViewById(R.id.ll_medicine_box_manage);
        ll_remind_setting = findViewById(R.id.ll_remind_setting);
        ll_temporary_get_medicine = findViewById(R.id.ll_temporary_get_medicine);
        ll_go_out_setting = findViewById(R.id.ll_go_out_setting);
        ll_medicine_box_pandect = findViewById(R.id.ll_medicine_box_pandect);
        ll_medicine_knowledge = findViewById(R.id.ll_medicine_knowledge);
        ll_push_suggest = findViewById(R.id.ll_push_suggest);
        ll_statement_message = findViewById(R.id.ll_statement_message);
        ll_about_us = findViewById(R.id.ll_about_us);
        ll_back_connect = findViewById(R.id.ll_back_connect);
        ll_language_choose = findViewById(R.id.ll_language_choose);
        ll_unlogin = findViewById(R.id.ll_unlogin);

        ll_medicine_box_more = findViewById(R.id.ll_medicine_box_more);

        img_main_engine_icon_one = findViewById(R.id.img_main_engine_icon_one);
        img_main_engine_icon_two = findViewById(R.id.img_main_engine_icon_two);
        tv_main_engine_name_one = findViewById(R.id.tv_main_engine_name_one);
        tv_main_engine_name_two = findViewById(R.id.tv_main_engine_name_two);
        tv_main_engine_content_one = findViewById(R.id.tv_main_engine_content_one);
        tv_main_engine_content_two = findViewById(R.id.tv_main_engine_content_two);
        tv_host_number = findViewById(R.id.tv_host_number);
        tv_host_name = findViewById(R.id.tv_host_name);

        rl_main_engine_one = findViewById(R.id.rl_main_engine_one);
        rl_main_engine_two = findViewById(R.id.rl_main_engine_two);

        dl_medicine_box_setting = findViewById(R.id.dl_medicine_box_setting);
          //TODO
        //如果主机1未连接，则不显示，下同理
        if (!"1".equals(App.loginMsg.host1_connectionState)){
            rl_main_engine_one.setVisibility(View.GONE);
        }
        if (!"1".equals(App.loginMsg.host2_connectionState)){
            rl_main_engine_two.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        ll_user_message.setOnClickListener(this);
        ll_medicine_box_manage.setOnClickListener(this);
        ll_remind_setting.setOnClickListener(this);
        rl_main_engine_one.setOnClickListener(this);
        ll_temporary_get_medicine.setOnClickListener(this);
        ll_go_out_setting.setOnClickListener(this);
        ll_medicine_box_pandect.setOnClickListener(this);
        rl_main_engine_two.setOnClickListener(this);
        ll_medicine_knowledge.setOnClickListener(this);
        ll_push_suggest.setOnClickListener(this);
        ll_statement_message.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        ll_back_connect.setOnClickListener(this);
        ll_language_choose.setOnClickListener(this);
        ll_unlogin.setOnClickListener(this);

        dl_medicine_box_setting.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (slideOffset > 0){
                    if (dl_medicine_box_setting.isDrawerOpen(ll_medicine_box_more)){
                        closeDrawerLayout();
                    }else {
                        openDrawerLayout();
                    }
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_user_message:              //用户信息
                startActivity(new Intent(MedicineBoxSettingActivity.this, UserMessageActivity.class));
                break;
            case R.id.ll_medicine_box_manage:       //药盒管理
                startActivity(new Intent(MedicineBoxSettingActivity.this, BoxManagerActivity.class));
                break;
            case R.id.ll_remind_setting:            //提醒设置
                startActivity(new Intent(MedicineBoxSettingActivity.this, RemindSettingActivity.class));
                break;
            case R.id.ll_temporary_get_medicine:    //临时取药
                startActivity(new Intent(MedicineBoxSettingActivity.this, TemporaryGetMedicineActivity.class));
                break;
            case R.id.ll_go_out_setting:            //外出设置
                startActivity(new Intent(MedicineBoxSettingActivity.this, GoOutSettingActivity.class));
                break;
            case R.id.ll_medicine_box_pandect:      //药盒总览
                startActivity(new Intent(MedicineBoxSettingActivity.this, MedicineBoxPandectActivity.class));
                break;
            case R.id.ll_medicine_knowledge:        //药物知识
                Intent urlIntent = new Intent(MedicineBoxSettingActivity.this, WebShowActivity.class);
                urlIntent.putExtra("url", "http://www.baidu.com/");
                startActivity(urlIntent);
                break;
            case R.id.ll_push_suggest:              //推送建议
                showTipDialog();
                break;
            case R.id.ll_statement_message:         //报表信息
                startActivity(new Intent(MedicineBoxSettingActivity.this, StatementMessageActivity.class));
                break;
            case R.id.rl_main_engine_one:
                rl_main_engine_one.setBackgroundResource(R.color.mainEngineCheckBg);
                img_main_engine_icon_one.setImageResource(R.mipmap.main_engine_check_yes);
                tv_main_engine_name_one.setTextColor(getResources().getColor(R.color.greenText));
                tv_main_engine_content_one.setTextColor(getResources().getColor(R.color.greenText));

                rl_main_engine_two.setBackgroundResource(R.color.white);
                img_main_engine_icon_two.setImageResource(R.mipmap.main_engine_check_no);
                tv_main_engine_name_two.setTextColor(getResources().getColor(R.color.gray));
                tv_main_engine_content_two.setTextColor(getResources().getColor(R.color.gray));

                //选中主机1后保存选中主机信息
                SPUtil.saveData(this, "hostOid", App.loginMsg.host1_oid);
                SPUtil.saveData(this, "hostCode", App.loginMsg.host1_hostNumber);
                App.hostOid = App.loginMsg.host1_oid;
                App.hostCode = App.loginMsg.host1_hostNumber;

                break;
            case R.id.rl_main_engine_two:
                rl_main_engine_two.setBackgroundResource(R.color.mainEngineCheckBg);
                img_main_engine_icon_two.setImageResource(R.mipmap.main_engine_check_yes);
                tv_main_engine_name_two.setTextColor(getResources().getColor(R.color.greenText));
                tv_main_engine_content_two.setTextColor(getResources().getColor(R.color.greenText));

                rl_main_engine_one.setBackgroundResource(R.color.white);
                img_main_engine_icon_one.setImageResource(R.mipmap.main_engine_check_no);
                tv_main_engine_name_one.setTextColor(getResources().getColor(R.color.gray));
                tv_main_engine_content_one.setTextColor(getResources().getColor(R.color.gray));

                //选中主机2后保存选中主机信息
                SPUtil.saveData(this, "hostOid", App.loginMsg.host2_oid);
                SPUtil.saveData(this, "hostCode", App.loginMsg.host2_hostNumber);
                App.hostOid = App.loginMsg.host2_oid;
                App.hostCode = App.loginMsg.host2_hostNumber;

                break;
            case R.id.back:
                if (dl_medicine_box_setting.isDrawerOpen(ll_medicine_box_more)){
                    closeDrawerLayout();
                }else {
                    openDrawerLayout();
                }
                break;
            case R.id.ll_about_us:          //关于我们
                startActivity(new Intent(MedicineBoxSettingActivity.this, AboutUsActivity.class));
                break;
            case R.id.ll_back_connect:         //返回连接
                Intent intent = new Intent(MedicineBoxSettingActivity.this, MainEngineConnectActivity.class);
                intent.putExtra("isFromMain", true);
                startActivity(intent);
                break;
            case R.id.ll_language_choose:   //语言选择
                Intent intent1 = new Intent(MedicineBoxSettingActivity.this, LanguageSelectActivity.class);
                intent1.putExtra("isMain", true);
                startActivity(intent1);
                break;
            case R.id.ll_unlogin:           //退出登录
//                closeDrawerLayout();
                showBackDialog();
                break;
        }
    }

    /*
    * 打开抽屉
    * */
    private void openDrawerLayout() {
        img_back.setImageResource(R.mipmap.icon_close);
        dl_medicine_box_setting.openDrawer(Gravity.LEFT);
        dl_medicine_box_setting.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.LEFT);
    }

    /*
    * 关闭抽屉
    * */
    private void closeDrawerLayout(){
        img_back.setImageResource(R.mipmap.icon_more);
        dl_medicine_box_setting.closeDrawer(ll_medicine_box_more);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastExitTime > 2000l){
            lastExitTime = System.currentTimeMillis();
            toast(getString(R.string.press_the_return_key_again_to_exit));
        }else {
            super.onBackPressed();
            App.finishAllActivity();
        }
    }

    public static void reStart(Context context) {
        Intent intent = new Intent(context, MedicineBoxSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /*
    点击退出登录，弹窗
     */
    private void showBackDialog() {
        exitDialog = new Dialog(MedicineBoxSettingActivity.this, R.style.processDialog);
        View view = LayoutInflater.from(MedicineBoxSettingActivity.this).inflate(R.layout.item_dialog_backuser, null);

        TextView me_back = view.findViewById(R.id.me_back);
        TextView canclePic = view.findViewById(R.id.canclePic);

        me_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();

                SPUtil.saveData(MedicineBoxSettingActivity.this, "islogin", false);
                App.islogin = false;

                startActivity(new Intent(MedicineBoxSettingActivity.this, LoginActivity.class));
                App.finishRealAllActivity();
            }
        });
        canclePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        exitDialog.setContentView(view);
        exitDialog.setCanceledOnTouchOutside(false);

        Window window = exitDialog.getWindow();
        //底部弹出
        window.setGravity(Gravity.BOTTOM);
        //弹出动画
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        exitDialog.show();
    }

    /*
    * 功能尚未开放的提示弹窗
    * */
    private void showTipDialog(){
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_development_tip, null);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
