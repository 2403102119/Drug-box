package com.tckkj.medicinebox.entity;

import java.io.Serializable;

public class Bean {
    public int status;
    public String message;

    /*
    * 注册信息返回实体类
    * */
    public class RegisterMsg{
        public String oid;
        public int status;
        public String message;
    }

    /*
     * 登录信息返回实体类    一级
     * */
    public class LoginMsgAll{
        public LoginMsg model;
        public int status;
        public String message;
    }

    /*
     * 登录信息返回实体类    二级
     * */
    public class LoginMsg implements Serializable{
        public String host1_oid;
        public String host2_oid;
        public String host1_connectionState;
        public String host2_connectionState;
        public String host1_hostNumber;
        public String host2_hostNumber;
        public String token;
        public String oid;
        public String account;
        public String serialNumber;
        public String host1_nickName;
        public String host2_nickName;
    }

    /*
     * 修改用户信息返回实体类    一级
     * */
    public class ChangeUserMsgAll{
        public ChangeUserMsg model;
        public int status;
        public String message;
    }

    /*
     * 修改用户信息返回实体类    二级
     * */
    public class ChangeUserMsg{
        public String oid;
        public String nickName;
        public String sex;
        public String date;
        public String phone1;
        public String phone2;
    }

    /*
    * 主机信息实体类   一级
    * */
    public class HostList{
        public int status;
        public String message;
        public HostInfo model;
    }

    /*
     * 主机信息实体类   二级
     * */
    public class HostInfo{
        public String oid;
        public String hostNumber;
        public String ipport;
        public String lastHeartDate;
        public String hostSerialNumber;
        public String pattern;
        public String connectionState;
        public String status;
        public String registerDate;
        public String connectDate;
        public String isVoiceReminding;
        public String isScreenReminding;
        public String volume;
        public MedicineStorehouse medicineStorehouse1;
        public MedicineStorehouse medicineStorehouse2;
        public MedicineStorehouse medicineStorehouse3;
        public MedicineStorehouse medicineStorehouse4;
        public MedicineStorehouse medicineStorehouse5;
        public MedicineStorehouse medicineStorehouse6;
    }

    /*
    * 药仓信息实体类
    * */
    public class MedicineStorehouse{
        public String oid;
    }
    /**
     *药盒管理实体类
     * */
     public class Kit_information{
        public kit_message model;
        public int status;
        public String message;
    }
    public class kit_message{
         public String takingTime;
         public String allowance;
         public String days;
         public String oid;
         public String name;
         public String termOfValidity;
         public String serialNumber;
         public String dose;
    }

    public class Get_user_information{
        public get_user model;
        public int status;
        public String message;
    }
    public class get_user{
         public String sex;
         public String nickName;
         public String oid;
         public String areaCode2;
         public String areaCode1;
         public String phone2;
         public String date;
         public String phone1;
    }
}
