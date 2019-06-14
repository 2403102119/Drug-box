package com.tckkj.medicinebox.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.activity.LoginActivity;
import com.tckkj.medicinebox.activity.MainEngineConnectActivity;
import com.tckkj.medicinebox.view.ClearEditText;

public class InputDialogUtil {
    private Dialog dialog;

    private OnConfirmListener onConfirmListener;

    public InputDialogUtil(final Context context, boolean isShowTitle, String title, boolean isCancelTouchOutside, int inputType,String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
        dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(isCancelTouchOutside);
        TextView tv_input_title = view.findViewById(R.id.tv_input_title);
        Button btn_input_confirm = view.findViewById(R.id.btn_input_confirm);
        final ClearEditText cet_input_content = view.findViewById(R.id.cet_input_content);
        cet_input_content.setInputType(inputType);
        cet_input_content.setText(text);

        if (isShowTitle) {
            tv_input_title.setText(title);
        }else {
            tv_input_title.setVisibility(View.GONE);
        }
        btn_input_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputContent = cet_input_content.getText().toString().trim();
                if (StringUtil.isSpace(inputContent)){
                    Toast.makeText(context, R.string.please_input_content, Toast.LENGTH_SHORT).show();
                    return;
                }

                onConfirmListener.onClick(inputContent);
            }
        });
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener{
        void onClick(String inputContent);
    }

    public void show(){
        dialog.show();
    }

    public void cancel(){
        dialog.cancel();
    }

    /*
    * 登录异常提示
    * */
    public static void showLoginTipDialog(Context context){
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login_tip, null);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                App.finishRealAllActivity();
                SPUtil.saveData(context, "token", "");
                SPUtil.saveData(context,"islogin",false);
                App.token="";
                App.islogin=false;
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*
     * 主机未连接提示
     * */
    public static void showHostConnectTipDialog(Context context){
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_host_connect_tip, null);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                App.finishRealAllActivity();
                context.startActivity(new Intent(context, MainEngineConnectActivity.class));
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
