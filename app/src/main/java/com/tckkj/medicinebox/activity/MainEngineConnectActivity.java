package com.tckkj.medicinebox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.view.ClearEditText;

import okhttp3.Call;

public class MainEngineConnectActivity extends BaseActivity {
    private RelativeLayout rl_main_engine_one, rl_main_engine_two,rl_main_engine;
    private Button btn_connect_main_engine_one, btn_connect_main_engine_two, btn_connect_main_engine,
            btn_cancel_main_engine_one, btn_cancel_main_engine_two, btn_cancel_main_engine;
    private ClearEditText cet_main_engine;
    private TextView tv_search, tv_main_engine_number_one, tv_main_engine_number_two, tv_main_engine_number,tv_state,tv_state_two,tv_state_three;
    //是否已连接主机1
    private boolean isOneConnected = false;
    //是否已连接主机2
    private boolean isTwoConnected = false;

    //是否从药盒设置界面跳过来
    private boolean isFromMain = false;

    //搜索到的主机oid
    private String searchHostOid = "";
    private Dialog exitDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_main_engine_connect);

        title.setText(R.string.host_connection);
        backText.setText(R.string.log_out);
        rightTxt.setText(R.string.enter_the_home_screen);
        img_back.setVisibility(View.GONE);
        backText.setVisibility(View.VISIBLE);
        rightTxt.setVisibility(View.VISIBLE);

        rl_main_engine_one = findViewById(R.id.rl_main_engine_one);
        rl_main_engine_two = findViewById(R.id.rl_main_engine_two);
        rl_main_engine = findViewById(R.id.rl_main_engine);
        btn_connect_main_engine_one = findViewById(R.id.btn_connect_main_engine_one);
        btn_connect_main_engine_two = findViewById(R.id.btn_connect_main_engine_two);
        btn_connect_main_engine = findViewById(R.id.btn_connect_main_engine);
        btn_cancel_main_engine_one = findViewById(R.id.btn_cancel_main_engine_one);
        btn_cancel_main_engine_two = findViewById(R.id.btn_cancel_main_engine_two);
        btn_cancel_main_engine = findViewById(R.id.btn_cancel_main_engine);
        cet_main_engine = findViewById(R.id.cet_main_engine);
        tv_search = findViewById(R.id.tv_search);
        tv_main_engine_number_one = findViewById(R.id.tv_main_engine_number_one);
        tv_main_engine_number_two = findViewById(R.id.tv_main_engine_number_two);
        tv_main_engine_number = findViewById(R.id.tv_main_engine_number);
        tv_state = findViewById(R.id.tv_state);
        tv_state_two = findViewById(R.id.tv_state_two);
        tv_state_three = findViewById(R.id.tv_state_three);

        //判断主机连接状况
        if (App.islogin && null != App.loginMsg) {
            judgeHostConnect();
        }
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
        btn_connect_main_engine_one.setOnClickListener(this);
        btn_connect_main_engine_two.setOnClickListener(this);
        btn_connect_main_engine.setOnClickListener(this);
        btn_cancel_main_engine_one.setOnClickListener(this);
        btn_cancel_main_engine_two.setOnClickListener(this);
        btn_cancel_main_engine.setOnClickListener(this);
        rl_main_engine_one.setOnClickListener(this);
        rl_main_engine_two.setOnClickListener(this);
        rl_main_engine.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            isFromMain = getIntent().getBooleanExtra("isFromMain", false);
        }
        if (isFromMain){
            rightTxt.setVisibility(View.GONE);
            backText.setVisibility(View.GONE);
            img_back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTxt:     //进入主屏
                if ("1".equals(App.loginMsg.host1_connectionState) || "1".equals(App.loginMsg.host2_connectionState)){      //连接主机后方可跳转到主屏
                    startActivity(new Intent(MainEngineConnectActivity.this, MedicineBoxSettingActivity.class));
                    App.finishRealAllActivity();
                }else {
                    toast(getString(R.string.please_connect_to_the_host_first));
                }
                break;
            case R.id.back:
                if (isFromMain){//返回上一页
                    finish();
                }else {//退出登录
                    showbackDialog();
                }
                break;
            case R.id.btn_connect_main_engine_one:
                //连接主机1
                if (btn_connect_main_engine_one.getText().toString().equals("绑定")){
                    connectHost("1", App.loginMsg.host1_oid);
                }else if (btn_connect_main_engine_one.getText().toString().equals("重新绑定")){
                    connectHost("1", App.loginMsg.host1_oid);
                }else if (btn_connect_main_engine_one.getText().toString().equals("取消")){
                    connectHost("0", App.loginMsg.host1_oid);
                }

                break;
            case R.id.btn_connect_main_engine_two:
                if (btn_connect_main_engine_two.getText().toString().equals("绑定")){
                    connectHost("1", App.loginMsg.host2_oid);
                }else if (btn_connect_main_engine_two.getText().toString().equals("重新绑定")){
                    connectHost("1", App.loginMsg.host2_oid);
                }else if (btn_connect_main_engine_two.getText().toString().equals("取消")){
                    connectHost("0", App.loginMsg.host2_oid);
                }//连接主机2

                break;
            case R.id.btn_connect_main_engine:
                if (btn_connect_main_engine.getText().toString().equals("绑定")){
                    connectHost("1", searchHostOid);
                }else if (btn_connect_main_engine.getText().toString().equals("重新绑定")){
                    connectHost("1", searchHostOid);
                }

                break;
//            case R.id.btn_cancel_main_engine_one:       //取消主机1连接
//                connectHost("0", App.loginMsg.host1_oid);
//                break;
//            case R.id.btn_cancel_main_engine_two:       //取消主机2连接
//                connectHost("0", App.loginMsg.host2_oid);
//                break;
            case R.id.rl_main_engine_one:           //主机详情
                Intent intent1 = new Intent(MainEngineConnectActivity.this, MainEngineDetailActivity.class);
                intent1.putExtra("oid", App.loginMsg.host1_oid);
                startActivity(intent1);
                break;
            case R.id.rl_main_engine_two:
                startActivity(new Intent(MainEngineConnectActivity.this, MainEngineDetailActivity.class));
                break;
            case R.id.rl_main_engine:
                break;
            case R.id.tv_search:
                String searchStr = cet_main_engine.getText().toString().trim();
                if (StringUtil.isSpace(searchStr)){
                    toast(getString(R.string.hint_of_box_search));
                    break;
                }
                getHostListByNumber(searchStr);

                /*switch (searchStr){
                    case "1":
                        rl_main_engine_one.setVisibility(View.VISIBLE);
                        rl_main_engine_two.setVisibility(View.GONE);
                        break;
                    case "2":
                        rl_main_engine_one.setVisibility(View.GONE);
                        rl_main_engine_two.setVisibility(View.VISIBLE);
                        break;
                    case "0":
                        rl_main_engine_one.setVisibility(View.GONE);
                        rl_main_engine_two.setVisibility(View.GONE);
                        break;
                        default:
                            rl_main_engine_one.setVisibility(View.VISIBLE);
                            rl_main_engine_two.setVisibility(View.VISIBLE);
                            break;
                }*/
                break;
        }
    }

    /*
    点击退出登录，弹窗
     */
    private void showbackDialog() {
        exitDialog = new Dialog(MainEngineConnectActivity.this, R.style.processDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.item_dialog_backuser, null);

        TextView me_back = view.findViewById(R.id.me_back);
        TextView canclePic = view.findViewById(R.id.canclePic);

        me_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();

                SPUtil.saveData(MainEngineConnectActivity.this, "islogin", false);
                App.islogin = false;

                startActivity(new Intent(MainEngineConnectActivity.this, LoginActivity.class));
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
    * 判断主机连接情况
    * */
    private void judgeHostConnect(){
        if (StringUtil.isSpace(App.loginMsg.host1_oid)){
            rl_main_engine_one.setVisibility(View.GONE);
        }else {
            rl_main_engine_one.setVisibility(View.VISIBLE);
            rl_main_engine.setVisibility(View.GONE);
        }

        if (StringUtil.isSpace(App.loginMsg.host2_oid)){
            rl_main_engine_two.setVisibility(View.GONE);
        }else {
            rl_main_engine_two.setVisibility(View.VISIBLE);
            rl_main_engine.setVisibility(View.GONE);
        }

        if ("2".equals(App.loginMsg.host1_connectionState)){
            btn_connect_main_engine_one.setText("重新绑定");
            tv_state.setText("绑定中");

        }else if ("1".equals(App.loginMsg.host1_connectionState)){
            btn_connect_main_engine_one.setText("取消");
            tv_state.setText("已绑定");
        }else {
            btn_connect_main_engine_one.setText(getResources().getString(R.string.connect));
            tv_state.setText("未绑定");
        }

        if ("2".equals(App.loginMsg.host2_connectionState)){
            btn_connect_main_engine_two.setText("重新绑定");
            tv_state_two.setText("绑定中");
        }else if ("1".equals(App.loginMsg.host2_connectionState)){
            btn_connect_main_engine_two.setText("取消");
            tv_state_two.setText("已绑定");
        }else {
            btn_connect_main_engine_two.setText(getResources().getString(R.string.connect));
            tv_state_two.setText("未绑定");
        }

        tv_main_engine_number_one.setText(App.loginMsg.host1_hostNumber);
        tv_main_engine_number_two.setText(App.loginMsg.host2_hostNumber);
    }

    /*
     * App7/根据token获取最新账户信息 >
     * */
    private void getNewUserMessage(){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getNewUserMessage(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.LoginMsgAll data = new Gson().fromJson(result, Bean.LoginMsgAll.class);

                            if (1 == data.status){
                                SPUtil.saveBean2Sp(MainEngineConnectActivity.this, data.model, "loginMsg", "loginMsg");
                                App.loginMsg = data.model;
                                //如果最新信息中已选择主机与现有连接主机不对应，说明原主机已取消连接，则重置已选中主机
                                if (!data.model.host1_oid.equals(App.hostOid) && !data.model.host2_oid.equals(App.hostOid)){
                                    App.hostOid = "";
                                    //如果主机连接取消，重置已选中主机
                                }else if ((data.model.host1_oid.equals(App.hostOid) && "2".equals(data.model.host1_connectionState)) ||
                                        (data.model.host2_oid.equals(App.hostOid) && "2".equals(data.model.host2_connectionState))){
                                    App.hostOid = "";
                                }

                                if (StringUtil.isSpace(App.hostOid)){       //如果尚未选择已选中主机
                                    if ("1".equals(App.loginMsg.host1_connectionState)){    //如果主机1是连接状态，默认显示主机1选中
                                        SPUtil.saveData(MainEngineConnectActivity.this, "hostOid", data.model.host1_oid);
                                        SPUtil.saveData(MainEngineConnectActivity.this, "hostCode", data.model.host1_hostNumber);
                                        App.hostOid = data.model.host1_oid;
                                        App.hostCode = data.model.host1_hostNumber;
                                    }else if ("1".equals(App.loginMsg.host2_connectionState)){  //如果主机1未连接，主机2连接，默认主机2选中
                                        SPUtil.saveData(MainEngineConnectActivity.this, "hostOid", data.model.host2_oid);
                                        SPUtil.saveData(MainEngineConnectActivity.this, "hostCode", data.model.host2_hostNumber);
                                        App.hostOid = data.model.host2_oid;
                                        App.hostCode = data.model.host2_hostNumber;
                                    }
                                }

                                judgeHostConnect();
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
     * App8/根据主机编号搜索主机列表 >
     * */
    private void getHostListByNumber(final String hostNumber){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getHostListByNumber(hostNumber, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.HostList data = new Gson().fromJson(result, Bean.HostList.class);

                            if (1 == data.status){
                                rl_main_engine.setVisibility(View.VISIBLE);
                                tv_main_engine_number.setText(data.model.hostNumber);
                                searchHostOid = data.model.oid;
                                if ("1".equals(data.model.connectionState)){
                                    btn_connect_main_engine.setText("取消");
                                    tv_state_three.setText("已绑定");
                                }else if ("2".equals(data.model.connectionState)){
                                    btn_connect_main_engine.setText(getResources().getString(R.string.connect));
                                    tv_state_three.setText("未绑定");
                                }
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
     * App9/连接主机 >
     * */
    private void connectHost(final String type, final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.connectHost(type, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);

                            if (1 == data.status){
                                getNewUserMessage();
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
