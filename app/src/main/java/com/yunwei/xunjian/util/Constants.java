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

//    public static final String BASIC_URL = "http://106.12.204.182:8080/hb2";
    public static final String BASIC_URL = "http://172.16.18.253:8080";
//    public static final String BASIC_URL = "http://192.168.14.249:8080";
//    public static final String BASIC_URL = "http://61.163.34.143:8081";

    /*登陆*/
    public static final String LOGIN_URL = BASIC_URL + "/login/xyUser";

    /*注册--获取供电单位*/
    public static final String GET_ELECTRIC_UNIT = BASIC_URL + "/register/getUnit";

    /*注册--获取部门*/
    public static final String GET_ELECTRIC_DEPARTMENT = BASIC_URL + "/register/getDepartment";

    /*注册--获取岗位*/
    public static final String GET_ELECTRIC_JOBS = BASIC_URL + "/register/getJobs";

    /*注册--提交*/
    public static final String SEND_REGISTER = BASIC_URL + "/register/sendRegister";

    /*获取已建勘测任务的台区列表*/
//    public static final String GET_AREA_LIST = BASIC_URL + "/transformer/transformerList";
    public static final String GET_AREA_LIST = BASIC_URL + "/lineLoss/selectAreaListByUsername";

    /*获取台区信息*/
//    public static final String GET_AREA_INFO = BASIC_URL + "/transformer/transformerInfo";
    public static final String GET_AREA_INFO = BASIC_URL + "/lineLoss/selectAreaByAreaCode";

    /*获取照片*/
    public static final String SHOW_PICTURE = BASIC_URL + "/lsUpload/zzdy/xiansun/";

    /*获取变压器列表*/
//    public static final String SEARCH_TRANSFORMER = BASIC_URL + "/transformer/transformers";
    public static final String SEARCH_TRANSFORMER = BASIC_URL + "/lineLoss/selectAreaListByNumOrName";

    /*计算变压器线损*/
    public static final String CALCULATE_WIRE_WASTE = BASIC_URL + "/transformer/calculateWireWaste";

    /*请求修改变压器*/
    public static final String MODIFY_TRANSFORMER_REQUEST = BASIC_URL + "/transformer/modifyTransformerRequest";

    /*请求修改台区*/
    public static final String MODIFY_AREA_REQUEST = BASIC_URL + "/lineLoss/updateAreaStart";

    /*取消修改台区*/
    public static final String CANCEL_AREA_REQUEST = BASIC_URL + "/lineLoss/updateAreaCancel";

    /*保存台区信息-新建*/
    public static final String SAVE_AREA_NEW = BASIC_URL + "/lineLoss/saveAddArea";

    /*保存台区信息-修改*/
    public static final String SAVE_AREA_OLD = BASIC_URL + "/lineLoss/saveUpdateArea";

    /*保存变压器-新建*/
    public static final String SAVE_TRANSFORMER_NEW = BASIC_URL + "/transformer/saveTransformerNew";

    /*保存变压器-修改*/
    public static final String SAVE_TRANSFORMER_OLD = BASIC_URL + "/transformer/saveTransformerOld";

    /*获取台区的分段线路列表*/
    public static final String GET_SECTIONALIZED_LINE_LIST = BASIC_URL + "/lowerVoltageLine/lowerVoltageLineList";

    /*获取低压线路信息*/
    public static final String GET_LOWER_VOLTAGE_LINE = BASIC_URL + "/lowerVoltageLine/getLowerVoltageLineInfo";

    /*请求修改低压线路*/
    public static final String MODIFY_LOWER_VOLTAGE_LINE_REQUEST = BASIC_URL + "/lowerVoltageLine/apply2ChangeLowerVoltageLineInfoByLineId";

    /*保存低压线路*/
    public static final String SAVE_LOWER_VOLTAGE_LINE = BASIC_URL + "/lowerVoltageLine/saveLowerVoltageLineInfo";

    /*我的*/
    public static final String MY_INFO = BASIC_URL + "/realUser/getRealUserInfo";

    /*修改身份证号和电话号码*/
    public static final String MODIFY_IDC_AND_PHONE = BASIC_URL + "/realUser/changeIdcNoAndPhone";

    /*查看问题反馈*/
    public static final String GET_FEEDBACK = BASIC_URL + "/feedback/getFeedback";

    /*添加问题反馈*/
    public static final String ADD_FEEDBACK = BASIC_URL + "/feedback/addFeedback";

    /*添加问题反馈和图片*/
    public static final String ADD_FEEDBACK_WITH_PICTURE = BASIC_URL + "/feedback/addFeedbackPic";

    /*修改密码*/
    public static final String MODIFY_PASSWORD = BASIC_URL + "/changePassword";
}
