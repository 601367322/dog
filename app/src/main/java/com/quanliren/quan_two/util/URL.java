package com.quanliren.quan_two.util;

public class URL {
//    public static final String URL="http://182.92.158.55";
//    public static final String IP="182.92.158.55";
//    public static final String URL = "http://192.168.1.22:8889";
//    public static final String IP = "192.168.1.22";

    public static final String URL = "http://" + Util.getPropertiesValue("ip") + ":" + Util.getPropertiesValue("port");
    public static final String IP = Util.getPropertiesValue("ip");
    public static final Integer SOCKET = Integer.valueOf(Util.getPropertiesValue("socket"));

    public static final String STATUS = "status";
    public static final String RESPONSE = "responses";
    public static final String INFO = "info";
    public static final String NETURL = "neturl";
    public static final String PAGEINDEX = "p";
    public static final String LIST = "list";
    public static final String RESULTMAP = "resultMap";

    public static final String appKey = "042181013";
    public static final String secret = "b218c47f5595449abe02588ea927ce82";


    public static final String DZDP_URL = "http://api.dianping.com/";

    /**
     * 注册第一步填写手机号*
     */
    public static final String REG_FIRST = URL + "/client/send_user_mobile.php";
    /**
     * 注册第二步填写验证码*
     */
    public static final String REG_SENDCODE = URL + "/client/send_user_auth.php";
    /**
     * 注册第三步填写基本信息*
     */
    public static final String REG_THIRD = URL + "/client/send_reg_info.php";
    /**
     * 找回密码第一步*
     */
    public static final String FINDPASSWORD_FIRST = URL + "/client/forget_pwd_one.php";
    /**
     * 找回密码第二部*
     */
    public static final String FINDPASSWORD_SECOND = URL + "/client/forget_pwd_two.php";
    /**
     * 修改密码*
     */
    public static final String MODIFYPASSWORD = URL + "/client/user/alert_pwd.php";
    /**
     * 退出*
     */
    public static final String LOGOUT = URL + "/client/logout.php";
    /**
     * 登陆*
     */
    public static final String LOGIN = URL + "/client/user_login.php";
    /**
     * 第三方登陆*
     */
    public static final String OTHER_LOGIN = URL + "/client/user_other_login.php";
    /**
     * 编辑用户信息*
     */
    public static final String EDIT_USER_INFO = URL + "/client/user/edit_info.php";
    /**
     * 上传用户头像*
     */
    public static final String UPLOAD_USER_LOGO = URL + "/client/user/img/avatar/upload.php";
    /**
     * 获取用户详细信息*
     */
    public static final String GET_USER_INFO = URL + "/get_user_detail.php";
    /**
     * new获取用户详细信息*
     */
    public static final String GET_USER_IN = URL + "/client/user/get_my_detail.php";
    /**
     * 上传相册*
     */
    public static final String UPLOAD_ALBUM_IMG = URL + "/client/user/img/album/upload.php";
    /**
     * 删除文字动态*
     */
    public static final String DEL_DONGTAI = URL + "/client/user/dynamic/del_dy.php";
    /**
     * 删除动态*
     */
    public static final String DELETE_DONGTAI = URL + "/client/user/dynamic/update_dy.php";
    /**
     * 删除约会*
     */
    public static final String DELETE_DATE = URL + "/client/user/dating/cancel_dating.php";
    /**
     * 获取用户详细信息*
     */
    public static final String SET_USERLOGO = URL + "/client/user/setting_avatar.php";
    /**
     * 删除头像*
     */
    public static final String DELETE_USERLOGO = URL + "/client/user/img/avatar/update_num.php";
    /**
     * 发表动态*
     */
    public static final String PUBLISH_TXT = URL + "/client/user/dynamic/pub_text.php";
    /**
     * 评论我的*
     */
    public static final String COMMENT_ME = URL + "/client/user/dynamic/comment_my_new.php";
    /**
     * 发表动态图片*
     */
    public static final String PUBLISH_IMG = URL + "/client/user/dynamic/pub_img.php";
    /**
     * 关注*
     */
    public static final String CONCERN = URL + "/client/user/atten/concern_he.php";
    /**
     * 取消关注*
     */
    public static final String CANCLECONCERN = URL + "/client/user/atten/cancel_atten.php";
    /**
     * 关注列表*
     */
    public static final String CONCERNLIST = URL + "/client/user/atten/concern_list.php";
    /**
     * 获取附近的人列表*
     */
    public static final String NearUserList = URL + "/nearby_user_list.php";
    /**
     * 获取附近的人列表*
     */
    public static final String RoamUserList = URL + "/client/user/roam_list.php";
    /**
     * 留言板*
     */
    public static final String DONGTAI = URL + "/dy_list.php";
    /**
     * 好友动态*
     */
    public static final String DONGTAI_FRIEND = URL + "/client/user/dynamic/friend_dylist.php";
    /**
     * 约会列表*
     */
    public static final String DATE_LIST = URL + "/c_dating_list.php";
    /**
     * 我的约会列表*
     */
    public static final String MY_DATE_LIST = URL + "/client/user/dating/my_dtlist.php";
    /**
     * 我参加的约会列表*
     */
    public static final String MY_APPLY_DATE_LIST = URL + "/client/user/dating/my_apply_dtlist.php";
    /**
     * 报名管理列表*
     */
    public static final String DATE_APPLY_MANAGE = URL + "/client/user/dating/dt_apply_users.php";
    /**
     * 选择某人*
     */
    public static final String DATE_CHOSE_SOMEONE = URL + "/client/user/dating/choose_he.php";
    /**
     * 约会详情*
     */
    public static final String DATE_DETAIL = URL + "/dating_detail.php";
    /**
     * 约会详情2*
     */
    public static final String DATE_DETAIL_OTHER = URL + "/client/dating_comms.php";
    /**
     * 约会报名*
     */
    public static final String DATE_APPLY = URL + "/client/user/dating/apply_dating.php";
    /**
     * 设置用户状态*
     */
    public static final String SET_STATE = URL + "/client/user/setting/user_state.php";
    /**
     * 个人动态*
     */
    public static final String PERSONALDONGTAI = URL + "/user_dy_list.php";
    /**
     * 我收藏的约会*
     */
    public static final String DATE_MY_FAVORITE = URL + "/client/user/dating/my_collect_dtlist.php";
    /**
     * 获取动态详情*
     */
    public static final String GETDONGTAI_DETAIL = URL + "/dynamic_detail.php";
    /**
     * 获取动态详情2*
     */
    public static final String GETDONGTAI_DETAIL_OTHER = URL + "/client/dynamic_detail.php";
    /**
     * 评论*
     */
    public static final String REPLY_DONGTAI = URL + "/client/user/dynamic/reply_dy.php";
    /**
     * 评论约会*
     */
    public static final String REPLY_DATE = URL + "/client/user/dating/reply_dating.php";
    /**
     * 获取支付宝单号*
     */
    public static final String GETALIPAY = URL + "/client/pay/build_alipay.php";
    /**
     * 获取微信订单号*
     */
    public static final String GETWXPAY = URL + "/client/wxpay/build_alipay.php";
    /**
     * 举报并拉黑*
     */
    public static final String JUBAOANDBLACK = URL + "/client/user/black/report_and_black.php";
    /**
     * 举报（动态和偷偷约详情）*
     */
    public static final String JUBAO = URL + "/client/user/black/report.php";
    /**
     * 加入黑名单*
     */
    public static final String ADDTOBLACK = URL + "/client/user/black/add_black.php";
    /**
     * 取消黑名单*
     */
    public static final String CANCLEBLACK = URL + "/client/user/black/cancel_black.php";
    /**
     * 黑名单列表*
     */
    public static final String BLACKLIST = URL + "/client/user/black/black_list.php";
    /**
     * 我兑换的记录*
     */
    public static final String MYEXCHANGELIST = URL + "/client/user/exch/my_list.php";
    /**
     * 兑换会员*
     */
    public static final String VIPEXCHANGE = URL + "/client/user/exch/exch_vip.php";
    /**
     * 删除兑换*
     */
    public static final String DELETEMYEXCHANGE = URL + "/client/user/exch/del_exch.php";
    /**
     * 访客记录*
     */
    public static final String VISITLIST = URL + "/client/user/visit/v_list.php";
    /**
     * 删除访客记录*
     */
    public static final String DELETE_VISITLIST = URL + "/client/user/visit/del_visit.php";
    /**
     * 放弃报名*
     */
    public static final String CANCLEAPPLY = URL + "/client/user/dating/cancel_apply.php";
    /**
     * 发送语音图片*
     */
    public static final String SENDFILE = URL + "/client/msg/send_file_msg.php";
    /**
     * 发送视频
     */
    public static final String SENDVIDEO = URL + "/client/msg/send_videofile_msg.php";
    /**
     * 发送文字聊天
     */
    public static final String SENDMSG = URL + "/client/msg/send_characters_msg.php";
    /**
     * 赠送体力*
     */
    public static final String GIVETILI = URL + "/client/user/to_give_power.php";
    /**
     * 统计*
     */
    public static final String TONGJI = URL + "/reg_channel.php";
    /**
     * 上传背景*
     */
    public static final String UPLOAD_USERINFO_BG = URL + "/client/user/img/avatar/up_bgimg.php";
    /**
     * 查找好友*
     */
    public static final String SEARCH_FRIEND = URL + "/client/user/atten/find_friends.php";
    /**
     * 发布约会*
     */
    public static final String PUB_DATA = URL + "/client/user/dating/pub_dating.php";
    /**
     * 收藏约会*
     */
    public static final String DATE_COLLECT = URL + "/client/user/dating/collect_dating.php";
    /**
     * 兌換商品列表*
     */
    public static final String PRODUCT_LIST = URL + "/goods_list.php";
    /**
     * 兑换商品详情*
     */
    public static final String PRODUCT_DETAIL = URL + "/goods_detail.php";
    /**
     * 兑换商品填写信息*
     */
    public static final String APPLY_EXCHANGE = URL + "/client/user/exch/apply_exch.php";
    /**
     * 表情下载列表*
     */
    public static final String EMOTICON_DOWNLOAD_LIST = URL + "/client/user/phiz/get_phiz_list.php";
    /**
     * 82	获取条幅列表*
     */
    public static final String ADBANNER_LIST = URL + "/client/get_banners.php";
    /**
     * 表情详情*
     */
    public static final String EMOTICON_DETAIL = URL + "/client/user/phiz/get_phiz_detail.php";
    /**
     * 下载表情*
     */
    public static final String DOWNLOAD_EMOTICON_FIRST = URL + "/client/user/phiz/ready_down.php";
    /**
     * 表情管理*
     */
    public static final String EMOCTION_MANAGE = URL + "/client/user/phiz/buy_phiz_list.php";
    /**
     * 发送错误日志*
     */
    public static final String SEND_LOG = URL + "/uploading_log.php";
    /**
     * 商城
     */
    public static final String SHOPLIST = URL + "/pricing_list.php";
    /**
     * 移除粉丝
     */
    public static final String DELETE_CARE = URL + "/client/user/atten/remove_fan.php";
    /**
     * 是否有进行中的约会
     */
    public static final String AFFIRM_PUB = URL + "/client/user/dating/affirm_pub.php";
    /**
     * 删除留言板评论(自己发表的)
     */
    public static final String D_DT_DYREPLY = URL + "/client/user/dynamic/d_dyreply.php";
    /**
     * 删除偷偷约评论(自己发表的)
     */
    public static final String D__DATE_DAREPLY = URL + "/client/user/dating/d_dareply.php";
    /**
     * 偷偷约详情追加狗粮
     */
    public static final String ADD_COIN_DATE = URL + "/client/user/dating/add_coin.php";
    /**
     * 获取用户计数
     */
    public static final String STATISTIC = URL + "/client/user/info_cnt.php";
    /**
     * 他发布的约会
     */
    public static final String OTHERP_DATE = URL + "/client/user/dating/he_dtlist.php";
    /**
     * 本月最热/最新红线
     */
    public static final String RED_LINE_RANKING = URL + "/client/user/thread/ranking_list.php";
    /**
     * 发布传递红线
     */
    public static final String PUBLISH_REDLINE = URL + "/client/user/thread/add_update_thread.php";
    /**
     * 红线详情
     */
    public static final String RED_LINE_DETAIL = URL + "/client/user/thread/one_thread.php";
    /**
     * 红线 赞
     */
    public static final String RED_ZAN = URL + "/client/user/thread/change_zambia.php";


    /**
     * 以下是kong的接口
     * 第三方绑定
     */
    public static final String OTHER_BIND = URL + "/client/user/binding_other_user.php";

    /**
     * 判断是否绑定手机以及同步通讯录*
     */
    public static final String LOOK_STATE = URL + "/client/user/binding_phone_mile.php";
    /**
     * 验证是不是正确的手机号*
     */
    public static final String PHONE__BIND_CORRECT = URL + "/client/binding_verification_user.php";
    /**
     * 获取验证码*
     */
    public static final String VERTIFY_SENDCODE = URL + "/client/binding_phonesuccess_auth.php";


    /**
     * 查看通讯录*
     */
    public static final String LOOK_CONTACTS = URL + "/client/user/findmileList.php";
    /**
     * 同步通讯录*
     */
    public static final String SYNC_CONTACTS = URL + "/client/user/addmileList.php";

    /**
     * 取消绑定*
     */
    public static final String CANCLE_BIND = URL + "/client/user/cancel_binding_user.php";

    /**
     * 收藏红线
     */
    public static final String RED_LINE_FAVORITE = URL + "/client/user/thread/change_collection.php";
    /**
     * 关闭红线或踢人
     */
    public static final String RED_LINE_CLOSE = URL + "/client/user/thread/delete_thread.php";
    /**
     * 红线牵手
     */
    public static final String RED_LINE_COMPLETE = URL + "/client/user/thread/success_thread.php";
    /**
     * 消费排行
     */
    public static final String CONSUMER_RANKING = URL + "/client/cost_rankinglist.php";
    /**
     * 添加，附近未关注的人*
     */
    public static final String NEAR_NO_RELATION = URL + "/nearbynoatt_user_list.php";
    /**
     * 我的狗粮*
     */
    public static final String DOG_FOOD_RECORD = URL + "/client/user/dog_food.php";
    /**
     * 记录分享分享*
     */
    public static final String SHARE_RECORD = URL + "/client/user/add_share.php";
    /**
     * 记录分享分享*
     */
    public static final String EVERYDAY_LOGIN = URL + "/client/user/login_add_food.php";
    /**
     * 真心话
     */
    public static final String CHAT_ZHENXINHUA = URL + "/client/user/fun/truth.php";
    /**
     * 大冒险
     */
    public static final String CHAT_DAMAOXIAN = URL + "/client/user/fun/adventure.php";
    /**
     * 动态 赞
     */
    public static final String DONGTAI_ZAN = URL + "/client/user/dynamic/change_zambia.php";
}
