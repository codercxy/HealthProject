package com.nju.android.health.bswk;

/**
 * Created by Administrator on 2016/1/25.
 */
public class HttpInfo {
    public static final String PATH = "http://app.njbswk.com/";                 //接口地址
    public static final String PATH_YM = "http://app.njbswk.com/V_2_3_4/";                 //页面地址
    public static final String RECORDUSER = "getRecordUser.jsp?";
    public static final int REQUEST = 200;
    public static final String SETZAN = "setZan.jsp?";
    //获取图像
    public static final String GET_PHOTO = "getReport.jsp?";
    //插入图像
    public static final String SET_PHOTO = "getReportPic.jsp?";
    public static final String PHOTO_UID = "uid=1&";
    public static final String PHOTO_TYPE = "&type=2";
    //读取遗传病史
    public static final String CHAXUN_JIWANGSHI="getDiseaseHistory.jsp?";
    public static final String YICHUANBING_DUQU ="getRecordYC.jsp?";
    //修改遗传病史
    public static final String YICHUANBING__XIUGAI="setRecordYC.jsp?";
    public static final String Xy = "getBloodPressure.jsp?";  //血压
    public static final String Dt = "getDynamic.jsp?";  //动态

    public static final String DANGAN_CHANGGUI = "getRecordRoutine.jsp?";
    public static final String Daxiuggaixinxi = "setRecordRoutine.jsp?";  //动态
    public static final String ID = "mem_account=18261938910&mem_token=0ddfe4b00304d705af3a4dbeb431680f";//测试Id
    //推荐资讯
    public static final String TUIJIANZIXUN = "getMessageList.jsp?";
    //推荐资讯参数
    public static final String NEWSTJ = "newstj=0";
    //资讯
    public static final String ZIXUN = "getMessage.jsp";
    //资讯列表
    public static final String ZIXUNLIST = "getMessageList.jsp?";
    //资讯列表参数
    public static final String NEWSTYPE = "newstype=";
    //资讯详情
    public static final String ZIXUNDETAIL = "getMessageDetail.jsp?";
    //资讯详情参数
    public static final String NEWSID = "newsid=";
    //修改生活习惯
    public static final String HABIT = "setRecordHabit.jsp?";
    //获取生活习惯
    public static final String LIFE_STYLE = "getRecordHabit.jsp?";
    //获取生活习惯参数
   /* public static final String LIFE_ACCOUNT = "mem_account=18261938910";
    public static final String LIFE_MEM_TOKEN = "&mem_token=0ddfe4b00304d705af3a4dbeb431680f";*/
    //过敏史
    public static final String GUOMINSHI = "setRecordGM.jsp?";
    //获得过敏史
    public static final String GET_GUOMINSHI = "getRecordGM.jsp?";
    //查询家族史
    public static final String GET_JIAZUSHI="getDiseaseFamily.jsp?";
    //添加家族史
    public static final String SET_JIAZUSHI="setDisease.jsp?";
    //疾病列表
    public static final String GET_JIBINGLIST="getDiseaseList.jsp?";
    //添加疾病列表
    public static final String SET_JIBINGLIST="setDisease.jsp?";
    //添加既往史列表
    public static final String SET_JIWANGSHI="setDiseaseHistory.jsp?";
   /* public static final String GET_GUOMIN_MEM_ACCOUNT = "mem_account=18261938910&";
    public static final String GET_GUOMIN_MEM_TOKEN = "mem_token=0ddfe4b00304d705af3a4dbeb431680f";*/
   //刷新资讯列表参数
   public static final String ZIXUN_CANSHU="newstype=1";
    //刷新资讯列表参数2
    public static final String ZIXUN_CANSHUT="&number_page=";

    //动态信息展示
    public static final String DONGTAIXINXI="getInformation.jsp?";
    //添加动态
    public static final String TIANJIADONGTAI="AddInformation.jsp?";

    //健康信息
    public static final String GETINFORMATION = "getInformation.jsp?";

    //赠送积分
    public static final String GETINTEGRAL = "PresentExp.jsp?";

    //首页搜索
    public static final String GETSELECT  ="getSelectList.jsp?";

    //获取血压血糖等不可输入的信息的
    public static final String GETXIANGXIXINXI  ="getQuestionnaireInformation.jsp?";

    //问卷提交接口
    public static final String SETBIAODANXINXI  ="addQuestionnaireInformation.jsp?";

    //------------------------------集团部分接口-------------------------------------
    public static final String PATH_JT = "http://app.njbswk.com/";
    //查找用户
    public static final String getMemberDetailList = PATH_JT + "getMemberDetailList.jsp?";
    //修改用户
    public static final String setMemberDetail = PATH_JT + "setMemberDetail.jsp?";
    //添加用户
    public static final String setAddMember = PATH_JT + "setAddMember.jsp?";

}
