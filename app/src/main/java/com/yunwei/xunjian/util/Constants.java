package com.yunwei.xunjian.util;

public class Constants {

    /*    public static final String UNIT_CODE_ZZDY = "ZZDY11000000";
        public static final String UNIT_CODE_XIXIAN = "JGGW1100000000";
        public static final String UNIT_CODE_XINXIAN = "JGXX1100000000";
        public static final String UNIT_CODE_LUOSHAN = "JGLS1100000000";
        public static final String UNIT_CODE_SHANGCHENG = "JGSC1100000000";
    */
    public static final int NULL_LISTVIEW = 0;
    public static final int UPDATE_LISTVIEW = 1;
    public static final int SEARCH_TRANSFORMER_SUCCESS = 2;
    public static final int SEARCH_TRANSFORMER_FAIL = 3;
    public static final int TAKE_PHOTO = 4;
    public static final int GET_PIC_FAIL = 15;
    public static final int GET_PIC_SUCCESS = 16;

    //相册请求码
    public static final int ALBUM_REQUEST_CODE = 100;
    // 相机请求码
    public static final int CAMERA_REQUEST_CODE = 101;
    // 剪裁请求码
    public static final int CROP_REQUEST_CODE = 102;
    //相机权限请求码
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 103;
    //相册权限请求码
    public static final int WRITE_PERMISSION_REQUEST_CODE = 104;

    public static double LONGITUDE = 0;//经度
    public static double LATITUDE = 0;//纬度

    public static final String BASIC_URL = "http://192.168.1.234:8080/yunwei2/app";
//    public static final String BASIC_URL = "http://172.16.18.253:8080";
//    public static final String BASIC_URL = "http://192.168.14.249:8080";
//    public static final String BASIC_URL = "http://61.163.34.143:8081";

    /*登陆*/
    public static final String LOGIN_URL = BASIC_URL + "/user/checkLogin";


    /*待办工单*/
    /*获取待办工单*/
    public static final String GTASKS = BASIC_URL + "/workList/getAllBacklogWorkListByHandler";

    /*获取待办工单*/
    public static final String FINISHED_LIST =BASIC_URL + "/workList/getAllWorkListByHandler";
    /*我的*/
    public static final String MY_INFO = BASIC_URL + "/user/getUserInfo";

    /*修改身份证号和电话号码*/
    public static final String MODIFY_IDC_AND_PHONE = BASIC_URL + "/user/updatePhoneByOrganizCode";

    /*查看问题反馈*/
    public static final String GET_FEEDBACK = BASIC_URL + "/feedback/getFeedback";

    /*添加问题反馈*/
    public static final String ADD_FEEDBACK = BASIC_URL + "/feedback/addFeedback";

    /*添加问题反馈和图片*/
    public static final String ADD_FEEDBACK_WITH_PICTURE = BASIC_URL + "/user/addFeedbackInfo";

    /*修改密码*/
    public static final String MODIFY_PASSWORD = BASIC_URL + "/user/updatePwByOrganizCode";

    /* 查看公司*/
    public static final String GET_Orgnizetion =BASIC_URL + "/organiz/getTopOrganizInfo";
    /* 查看部门或人员*/
    public static final String GET_DepartOrContact =BASIC_URL + "/organiz/getOrganizInfo";

}
