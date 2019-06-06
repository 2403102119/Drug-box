package com.tckkj.medicinebox.util;

public class UriUtil {

//    public static String ip = "http:/47.106.229.1:8080/MedicineBox/";
    public static String ip = "http://192.168.0.140:8080/MedicineBox/";


    public static String sendYzm = ip + "action/baseUser/sendYzm";//App1/获取验证码 >
    public static String addUser = ip + "action/baseUser/addUser";//App2/用户注册 >
    public static String userLogin = ip + "action/baseUser/userLogin";//App3/用户登录 > 使用用户名+密码登录
    public static String updateUserPasswordByAccount = ip + "action/baseUser/updateUserPasswordByAccount";//App4/忘记密码 >
    public static String updateUserInfo = ip + "action/baseHost/updateUserInfo";//App5/修改用户信息 >
    public static String deleteUserInfo = ip + "action/baseUser/deleteUserInfo";//App6/清空用户资料 >
    public static String getNewUserMessage = ip + "action/baseUser/getNewUserMessage";//App5/根据token获取最新账户信息 >
    public static String getHostListByNumber = ip + "action/baseHost/getHostByNumber";//App6/根据主机编号搜索主机列表 >
    public static String connectHost = ip + "action/baseHost/connectHost";//App7/连接主机 >
    public static String getHostMessage = ip + "action/baseHost/getHostMessage";//App8/查看主机详情 >
    public static String getCut = ip + "action/baseHost/switchHost";//App9/切换主机 >
    public static String getStorehouseData = ip + "action/baseMedicineStorehouse/getMedicineStorehouse";//App10/根据主机号及药仓序号获取药仓数据 >
    public static String updateMedicineStorehouse = ip + "action/baseMedicineStorehouse/updateMedicineStorehouse";//App11/药仓新增、添加或更换药品 >
    public static String deleteStorehouseData = ip + "action/baseMedicineStorehouse/deleteMedicineStorehouse";//App12/药仓清空数据 >

    public static String getDrugName = ip + "action/baseSystemData/getDrugName";//App12/根据药品条码值获取药品名字 >
    public static String getDrugRecordingList = ip + "action/baseDrugRecording/getDrugRecordingList";//App13/根据服药时段获取服药率数据 >
    public static String temporaryDrugWithdrawal = ip + "action/baseHost/temporaryDrugWithdrawal";//App14/临时取药 >
    public static String outgoingDispensing = ip + "action/baseHost/outgoingDispensing";//App15/外出设置配药 >
    public static String getStorehouseListByHostNumber = ip + "action/baseMedicineStorehouse/getStorehouseListByHostNumber";//App19/根据主机号获取药仓信息列表 >
    public static String getRegionMessage = ip + "action/baseSystemData/getRegionMessage";//App20/获取地区数据 >
    public static String getDispensingInformation = ip + "action/baseHost/getDispensingInformation";//App22/获取配药信息 >
    public static String cancelDispensing = ip + "action/baseHost/cancelDispensing";//App23/取消配药 >
    public static String changePattern = ip + "action/baseHost/changePattern";//App24/配药成功保存（改变模式） >

}