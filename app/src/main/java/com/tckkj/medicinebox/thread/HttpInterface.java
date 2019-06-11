package com.tckkj.medicinebox.thread;

import android.content.Context;
import android.util.Log;

import com.tckkj.medicinebox.App;
import com.tckkj.medicinebox.util.StringUtil;
import com.tckkj.medicinebox.util.UriUtil;
import com.tckkj.medicinebox.view.LoadingDialog;

/**
 * kylin on 2017/12/12.
 */

public class HttpInterface {
    private Context context;
    public LoadingDialog loadingDialog;

    public HttpInterface(Context context) {
        this.context = context;
        if (loadingDialog == null) {
            Log.e("httpInterface", "new LoadingDialog");
            loadingDialog = new LoadingDialog(context);
            //loadingDialog.setHint("playing。。。");
        }
    }

    /**
     * 获取验证码
     *//*
    public void sendYzm(String phone, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.sendYzm);
        try {
            userClient.AddParam("model.phone", phone);  //手机号
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*
    * App1/获取验证码 >
    * */
    public void sendYzm(String type,String areaCode, String account, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.sendYzm);
        try {
            userClient.AddParam("type", type);
            userClient.AddParam("model.areaCode", areaCode);
            userClient.AddParam("model.account", account);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * App2/用户注册 >
    * */
    public void addUser(String yzm, String areaCode, String account, String password, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.addUser);
        try {
            userClient.AddParam("yzm", yzm);
            userClient.AddParam("model.areaCode", areaCode);
            userClient.AddParam("model.account", account);
            userClient.AddParam("model.password", password);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App3/用户登录 > 使用用户名+密码登录
     * */
    public void userLogin(String account, String areaCode, String password, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.userLogin);
        try {
            userClient.AddParam("model.account", account);
            userClient.AddParam("model.areaCode", areaCode);
            userClient.AddParam("model.password", password);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App4/忘记密码 >
     * */
    public void updateUserPasswordByAccount(String yzm, String areaCode, String account, String password, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.updateUserPasswordByAccount);
        try {
            userClient.AddParam("yzm", yzm);
            userClient.AddParam("model.areaCode", areaCode);
            userClient.AddParam("model.account", account);
            userClient.AddParam("model.password", password);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App5/修改用户信息 >
     * */
    public void updateUserInfo(String oid, String nickName, String sex, String age, String date, String phone1, String phone2, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.updateUserInfo);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.AddParam("model.nickName", nickName);
            userClient.AddParam("model.sex", sex);
            userClient.AddParam("model.age", age);
            userClient.AddParam("model.date", date);
            userClient.AddParam("model.phone1", phone1);
            userClient.AddParam("model.phone2", phone2);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App6/清空用户资料 >
     * */
    public void deleteUserInfo(MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.deleteUserInfo);
        try {
            userClient.AddParam("token", App.token);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App5/根据token获取最新账户信息 >
     * */
    public void getNewUserMessage(MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getNewUserMessage);
        try {
            userClient.AddParam("token", App.token);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App6/根据主机编号搜索主机列表 >
     * */
    public void getHostListByNumber(String hostNumber, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getHostListByNumber);
        try {
            userClient.AddParam("hostNumber", hostNumber);
            userClient.AddParam("token", App.token);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App7/连接主机 >
     * */
    public void connectHost(String type, String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.connectHost);
        try {
            userClient.AddParam("type", type);
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App8/查看主机详情 >
     * */
    public void getHostMessage(String oid,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getHostMessage);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * App9/切换主机>
     * */
    public void getCut(String oid,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getCut);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App12/根据药品条码值获取药品名字 >
     * */
    public void getDrugName(String drugBarcode, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getDrugName);
        try {
            userClient.AddParam("model.drugBarcode", drugBarcode);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App13/根据服药时段获取服药率数据 >
     * */
    public void getDrugRecordingList(String oid, String takingDate, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getDrugRecordingList);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.host.oid", oid);
            userClient.AddParam("model.takingDate", takingDate);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App14/临时取药 >
     * */
    public void temporaryDrugWithdrawal(String medicineAmount, String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.temporaryDrugWithdrawal);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("medicineAmount", medicineAmount);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App15/外出设置配药 >
     * */
    public void outgoingDispensing(String currentTime, String returnTime, String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.outgoingDispensing);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("currentTime", currentTime);
            userClient.AddParam("returnTime", returnTime);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App11/药仓新增、添加或更换药品 >
     * */
    public void updateMedicineStorehouse(String dosage, String oid, String name, String dose,
                                         String termOfValidity, String wayOfTaking, String takingTime,
                                         MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.updateMedicineStorehouse);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("dosage", dosage);
            userClient.AddParam("model.oid", oid);
            userClient.AddParam("model.name", name);
            userClient.AddParam("model.dose", dose);
            userClient.AddParam("model.termOfValidity", termOfValidity);
            userClient.AddParam("model.days", wayOfTaking);
            userClient.AddParam("model.takingTime", takingTime);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App12/药仓清空数据 >
     * */
    public void deleteStorehouseData(String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.deleteStorehouseData);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App19/根据主机号获取药仓信息列表 >
     * */
    public void getStorehouseListByHostNumber(String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getStorehouseListByHostNumber);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("host.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App20/获取地区数据 >
     * */
    public void getRegionMessage(MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getRegionMessage);
        try {
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App10/根据主机号及药仓序号获取药仓数据 >
     * */
    public void getStorehouseData(String medicineSerialNumber, String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getStorehouseData);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.medicineSerialNumber", medicineSerialNumber);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App22/获取配药信息 >
     * */
    public void getDispensingInformation(String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.getDispensingInformation);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App23/取消配药 >
     * */
    public void cancelDispensing(String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.cancelDispensing);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * App24/配药成功保存（改变模式） >
     * */
    public void changePattern(String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.changePattern);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
