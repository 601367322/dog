package com.quanliren.quan_two.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.AreaAdapter;
import com.quanliren.quan_two.bean.Area;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.share.CommonShared;
import com.quanliren.quan_two.util.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ChosePositionFragment extends MenuFragmentBase{
    EditText input;
    ListView listview;
    TextView now_position;
    LinearLayout layoutIndex;
    TextView title_bar;
    private AreaAdapter adapter;
    private CommonShared cs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.chose_position, null);
        input=(EditText)v.findViewById(R.id.edittext);
        listview=(ListView)v.findViewById(R.id.position_list);
        now_position=(TextView)v.findViewById(R.id.now_position);
        layoutIndex=(LinearLayout)v.findViewById(R.id.layoutindex);
        title_bar=(TextView)v.findViewById(R.id.title_bar);
        cs=new CommonShared(getActivity().getApplication());
//        now_position.setText("当前选择城市："+cs.getChoseLocation().replace("市", ""));
        adapter = new AreaAdapter(getActivity(), initList(getAreas()));
//		adapter.addFirstItem(new Area("全国", 990100, 990000, "qg"));
        listview.setAdapter(adapter);
        setListener();
        return v;
    }
    public static final ArrayList<Area> getAreas() {
        ArrayList<Area> list = new ArrayList<Area>();
        list.add(new Area(1001, "北京", "beijing", true));
        list.add(new Area(1003, "上海", "shanghai", true));
        list.add(new Area(1002, "天津", "tianjin", true));
        list.add(new Area(1004, "重庆", "chongqing", true));
        list.add(new Area(1027, "沈阳", "shenyang", true));
        list.add(new Area(1125, "济南", "jinan", true));
        list.add(new Area(1063, "南京", "nanjing", true));
        list.add(new Area(1078, "杭州", "hangzhou", true));
        list.add(new Area(1200, "深圳", "shenzhen", true));
        list.add(new Area(1190, "广州", "guangzhou", true));
        list.add(new Area(1159, "武汉", "wuhan", true));
        list.add(new Area(1225, "成都", "chengdou", true));
        list.add(new Area(1297, "西安", "xian", true));

        list.add(new Area(1001, "北京", "beijing"));
        list.add(new Area(1002, "天津", "tianjin"));
        list.add(new Area(1003, "上海", "shanghai"));
        list.add(new Area(1004, "重庆", "chongqing"));
        list.add(new Area(1005, "邯郸", "handan"));
        list.add(new Area(1006, "石家庄", "shijiazhuang"));
        list.add(new Area(1007, "保定", "baoding"));
        list.add(new Area(1008, "张家口", "zhangjiakou"));
        list.add(new Area(1009, "承德", "chengde"));
        list.add(new Area(1010, "唐山", "tangshan"));
        list.add(new Area(1011, "廊坊", "langfang"));
        list.add(new Area(1012, "沧州", "cangzhou"));
        list.add(new Area(1013, "衡水", "hengshui"));
        list.add(new Area(1014, "邢台", "xingtai"));
        list.add(new Area(1015, "秦皇岛", "qinhuangdao"));
        list.add(new Area(1016, "朔州", "shuozhou"));
        list.add(new Area(1017, "忻州", "xinzhou"));
        list.add(new Area(1018, "太原", "taiyuan"));
        list.add(new Area(1019, "大同", "datong"));
        list.add(new Area(1020, "阳泉", "yangquan"));
        list.add(new Area(1021, "晋中", "jinzhong"));
        list.add(new Area(1022, "长治", "zhangzhi"));
        list.add(new Area(1023, "晋城", "jincheng"));
        list.add(new Area(1024, "临汾", "linfen"));
        list.add(new Area(1025, "吕梁", "lvliang"));
        list.add(new Area(1026, "运城", "yuncheng"));
        list.add(new Area(1027, "沈阳", "shenyang"));
        list.add(new Area(1028, "铁岭", "tieling"));
        list.add(new Area(1029, "大连", "dalian"));
        list.add(new Area(1030, "鞍山", "anshan"));
        list.add(new Area(1031, "抚顺", "fushun"));
        list.add(new Area(1032, "本溪", "benxi"));
        list.add(new Area(1033, "丹东", "dandong"));
        list.add(new Area(1034, "锦州", "jinzhou"));
        list.add(new Area(1035, "营口", "yingkou"));
        list.add(new Area(1036, "阜新", "fuxin"));
        list.add(new Area(1037, "辽阳", "liaoyang"));
        list.add(new Area(1038, "朝阳", "chaoyang"));
        list.add(new Area(1039, "盘锦", "panjin"));
        list.add(new Area(1040, "葫芦岛", "huludao"));
        list.add(new Area(1041, "长春", "zhangchun"));
        list.add(new Area(1042, "吉林", "jilin"));
        list.add(new Area(1043, "延边", "yanbian"));
        list.add(new Area(1044, "四平", "siping"));
        list.add(new Area(1045, "通化", "tonghua"));
        list.add(new Area(1046, "白城", "baicheng"));
        list.add(new Area(1047, "辽源", "liaoyuan"));
        list.add(new Area(1048, "松原", "songyuan"));
        list.add(new Area(1049, "白山", "baishan"));
        list.add(new Area(1050, "哈尔滨", "haerbin"));
        list.add(new Area(1051, "齐齐哈尔", "qiqihaer"));
        list.add(new Area(1052, "鸡西", "jixi"));
        list.add(new Area(1053, "牡丹江", "mudanjiang"));
        list.add(new Area(1054, "七台河", "qitaihe"));
        list.add(new Area(1055, "佳木斯", "jiamusi"));
        list.add(new Area(1056, "鹤岗", "hegang"));
        list.add(new Area(1057, "双鸭山", "shuangyashan"));
        list.add(new Area(1058, "绥化", "suihua"));
        list.add(new Area(1059, "黑河", "heihe"));
        list.add(new Area(1060, "大兴安岭", "daxinganling"));
        list.add(new Area(1061, "伊春", "yichun"));
        list.add(new Area(1062, "大庆", "daqing"));
        list.add(new Area(1063, "南京", "nanjing"));
        list.add(new Area(1064, "无锡", "wuxi"));
        list.add(new Area(1065, "镇江", "zhenjiang"));
        list.add(new Area(1066, "苏州", "suzhou"));
        list.add(new Area(1067, "南通", "nantong"));
        list.add(new Area(1068, "扬州", "yangzhou"));
        list.add(new Area(1069, "盐城", "yancheng"));
        list.add(new Area(1070, "徐州", "xuzhou"));
        list.add(new Area(1071, "淮安", "huaian"));
        list.add(new Area(1072, "连云港", "lianyungang"));
        list.add(new Area(1073, "常州", "changzhou"));
        list.add(new Area(1074, "泰州", "taizhou"));
        list.add(new Area(1075, "宿迁", "suqian"));
        list.add(new Area(1076, "舟山", "zhoushan"));
        list.add(new Area(1077, "衢州", "quzhou"));
        list.add(new Area(1078, "杭州", "hangzhou"));
        list.add(new Area(1079, "湖州", "huzhou"));
        list.add(new Area(1080, "嘉兴", "jiaxing"));
        list.add(new Area(1081, "宁波", "ningbo"));
        list.add(new Area(1082, "绍兴", "shaoxing"));
        list.add(new Area(1083, "温州", "wenzhou"));
        list.add(new Area(1084, "丽水", "lishui"));
        list.add(new Area(1085, "金华", "jinhua"));
        list.add(new Area(1086, "台州", "taizhou"));
        list.add(new Area(1087, "合肥", "hefei"));
        list.add(new Area(1088, "芜湖", "wuhu"));
        list.add(new Area(1089, "蚌埠", "bangbu"));
        list.add(new Area(1090, "淮南", "huainan"));
        list.add(new Area(1091, "马鞍山", "maanshan"));
        list.add(new Area(1092, "淮北", "huaibei"));
        list.add(new Area(1093, "铜陵", "tongling"));
        list.add(new Area(1094, "安庆", "anqing"));
        list.add(new Area(1095, "黄山", "huangshan"));
        list.add(new Area(1096, "滁州", "chuzhou"));
        list.add(new Area(1097, "阜阳", "fuyang"));
        list.add(new Area(1098, "宿州", "suzhou"));
        list.add(new Area(1099, "巢湖", "chaohu"));
        list.add(new Area(1100, "六安", "liuan"));
        list.add(new Area(1101, "亳州", "bozhou"));
        list.add(new Area(1102, "池州", "chizhou"));
        list.add(new Area(1103, "宣城", "xuancheng"));
        list.add(new Area(1104, "福州", "fuzhou"));
        list.add(new Area(1105, "厦门", "shamen"));
        list.add(new Area(1106, "宁德", "ningde"));
        list.add(new Area(1107, "莆田", "putian"));
        list.add(new Area(1108, "泉州", "quanzhou"));
        list.add(new Area(1109, "漳州", "zhangzhou"));
        list.add(new Area(1110, "龙岩", "longyan"));
        list.add(new Area(1111, "三明", "sanming"));
        list.add(new Area(1112, "南平", "nanping"));
        list.add(new Area(1113, "鹰潭", "yingtan"));
        list.add(new Area(1114, "新余", "xinyu"));
        list.add(new Area(1115, "南昌", "nanchang"));
        list.add(new Area(1116, "九江", "jiujiang"));
        list.add(new Area(1117, "上饶", "shangrao"));
        list.add(new Area(1118, "抚州", "fuzhou"));
        list.add(new Area(1119, "宜春", "yichun"));
        list.add(new Area(1120, "吉安", "jian"));
        list.add(new Area(1121, "赣州", "ganzhou"));
        list.add(new Area(1122, "景德镇", "jingdezhen"));
        list.add(new Area(1123, "萍乡", "pingxiang"));
        list.add(new Area(1124, "菏泽", "heze"));
        list.add(new Area(1125, "济南", "jinan"));
        list.add(new Area(1126, "青岛", "qingdao"));
        list.add(new Area(1127, "淄博", "zibo"));
        list.add(new Area(1128, "德州", "dezhou"));
        list.add(new Area(1129, "烟台", "yantai"));
        list.add(new Area(1130, "潍坊", "weifang"));
        list.add(new Area(1131, "济宁", "jining"));
        list.add(new Area(1132, "泰安", "taian"));
        list.add(new Area(1133, "临沂", "linyi"));
        list.add(new Area(1134, "滨州", "binzhou"));
        list.add(new Area(1135, "东营", "dongying"));
        list.add(new Area(1136, "威海", "weihai"));
        list.add(new Area(1137, "枣庄", "zaozhuang"));
        list.add(new Area(1138, "日照", "rizhao"));
        list.add(new Area(1139, "莱芜", "laiwu"));
        list.add(new Area(1140, "聊城", "liaocheng"));
        list.add(new Area(1141, "商丘", "shangqiu"));
        list.add(new Area(1142, "郑州", "zhengzhou"));
        list.add(new Area(1143, "安阳", "anyang"));
        list.add(new Area(1144, "新乡", "xinxiang"));
        list.add(new Area(1145, "许昌", "xuchang"));
        list.add(new Area(1146, "平顶山", "pingdingshan"));
        list.add(new Area(1147, "信阳", "xinyang"));
        list.add(new Area(1148, "南阳", "nanyang"));
        list.add(new Area(1149, "开封", "kaifeng"));
        list.add(new Area(1150, "洛阳", "luoyang"));
        list.add(new Area(1151, "济源", "jiyuan"));
        list.add(new Area(1152, "焦作", "jiaozuo"));
        list.add(new Area(1153, "鹤壁", "hebi"));
        list.add(new Area(1154, "濮阳", "puyang"));
        list.add(new Area(1155, "周口", "zhoukou"));
        list.add(new Area(1156, "漯河", "luohe"));
        list.add(new Area(1157, "驻马店", "zhumadian"));
        list.add(new Area(1158, "三门峡", "sanmenxia"));
        list.add(new Area(1159, "武汉", "wuhan"));
        list.add(new Area(1160, "襄樊", "xiangfan"));
        list.add(new Area(1161, "鄂州", "ezhou"));
        list.add(new Area(1162, "孝感", "xiaogan"));
        list.add(new Area(1163, "黄冈", "huanggang"));
        list.add(new Area(1164, "黄石", "huangshi"));
        list.add(new Area(1165, "咸宁", "xianning"));
        list.add(new Area(1166, "荆州", "jingzhou"));
        list.add(new Area(1167, "宜昌", "yichang"));
        list.add(new Area(1168, "恩施", "enshi"));
        list.add(new Area(1169, "神农架", "shennongjia"));
        list.add(new Area(1170, "十堰", "shiyan"));
        list.add(new Area(1171, "随州", "suizhou"));
        list.add(new Area(1172, "荆门", "jingmen"));
        list.add(new Area(1173, "仙桃", "xiantao"));
        list.add(new Area(1174, "天门", "tianmen"));
        list.add(new Area(1175, "潜江", "qianjiang"));
        list.add(new Area(1176, "岳阳", "yueyang"));
        list.add(new Area(1177, "长沙", "zhangsha"));
        list.add(new Area(1178, "湘潭", "xiangtan"));
        list.add(new Area(1179, "株洲", "zhuzhou"));
        list.add(new Area(1180, "衡阳", "hengyang"));
        list.add(new Area(1181, "郴州", "chenzhou"));
        list.add(new Area(1182, "常德", "changde"));
        list.add(new Area(1183, "益阳", "yiyang"));
        list.add(new Area(1184, "娄底", "loudi"));
        list.add(new Area(1185, "邵阳", "shaoyang"));
        list.add(new Area(1186, "湘西", "xiangxi"));
        list.add(new Area(1187, "张家界", "zhangjiajie"));
        list.add(new Area(1188, "怀化", "huaihua"));
        list.add(new Area(1189, "永州", "yongzhou"));
        list.add(new Area(1190, "广州", "guangzhou"));
        list.add(new Area(1191, "汕尾", "shanwei"));
        list.add(new Area(1192, "阳江", "yangjiang"));
        list.add(new Area(1193, "揭阳", "jieyang"));
        list.add(new Area(1194, "茂名", "maoming"));
        list.add(new Area(1195, "惠州", "huizhou"));
        list.add(new Area(1196, "江门", "jiangmen"));
        list.add(new Area(1197, "韶关", "shaoguan"));
        list.add(new Area(1198, "梅州", "meizhou"));
        list.add(new Area(1199, "汕头", "shantou"));
        list.add(new Area(1200, "深圳", "shenzhen"));
        list.add(new Area(1201, "珠海", "zhuhai"));
        list.add(new Area(1202, "佛山", "foshan"));
        list.add(new Area(1203, "肇庆", "zhaoqing"));
        list.add(new Area(1204, "湛江", "zhanjiang"));
        list.add(new Area(1205, "中山", "zhongshan"));
        list.add(new Area(1206, "河源", "heyuan"));
        list.add(new Area(1207, "清远", "qingyuan"));
        list.add(new Area(1208, "云浮", "yunfu"));
        list.add(new Area(1209, "潮州", "chaozhou"));
        list.add(new Area(1210, "东莞", "dongguan"));
        list.add(new Area(1211, "兰州", "lanzhou"));
        list.add(new Area(1212, "金昌", "jinchang"));
        list.add(new Area(1213, "白银", "baiyin"));
        list.add(new Area(1214, "天水", "tianshui"));
        list.add(new Area(1215, "嘉峪关", "jiayuguan"));
        list.add(new Area(1216, "武威", "wuwei"));
        list.add(new Area(1217, "张掖", "zhangye"));
        list.add(new Area(1218, "平凉", "pingliang"));
        list.add(new Area(1219, "酒泉", "jiuquan"));
        list.add(new Area(1220, "庆阳", "qingyang"));
        list.add(new Area(1221, "定西", "dingxi"));
        list.add(new Area(1222, "陇南", "longnan"));
        list.add(new Area(1223, "临夏", "linxia"));
        list.add(new Area(1224, "甘南", "gannan"));
        list.add(new Area(1225, "成都", "chengdou"));
        list.add(new Area(1226, "攀枝花", "panzhihua"));
        list.add(new Area(1227, "自贡", "zigong"));
        list.add(new Area(1228, "绵阳", "mianyang"));
        list.add(new Area(1229, "南充", "nanchong"));
        list.add(new Area(1230, "达州", "dazhou"));
        list.add(new Area(1231, "遂宁", "suining"));
        list.add(new Area(1232, "广安", "guangan"));
        list.add(new Area(1233, "巴中", "bazhong"));
        list.add(new Area(1234, "泸州", "luzhou"));
        list.add(new Area(1235, "宜宾", "yibin"));
        list.add(new Area(1236, "资阳", "ziyang"));
        list.add(new Area(1237, "内江", "neijiang"));
        list.add(new Area(1238, "乐山", "leshan"));
        list.add(new Area(1239, "眉山", "meishan"));
        list.add(new Area(1240, "凉山", "liangshan"));
        list.add(new Area(1241, "雅安", "yaan"));
        list.add(new Area(1242, "甘孜", "ganzi"));
        list.add(new Area(1243, "阿坝", "aba"));
        list.add(new Area(1244, "德阳", "deyang"));
        list.add(new Area(1245, "广元", "guangyuan"));
        list.add(new Area(1246, "贵阳", "guiyang"));
        list.add(new Area(1247, "遵义", "zunyi"));
        list.add(new Area(1248, "安顺", "anshun"));
        list.add(new Area(1249, "黔南", "qiannan"));
        list.add(new Area(1250, "黔东南", "qiandongnan"));
        list.add(new Area(1251, "铜仁", "tongren"));
        list.add(new Area(1252, "毕节", "bijie"));
        list.add(new Area(1253, "六盘水", "liupanshui"));
        list.add(new Area(1254, "黔西南", "qianxinan"));
        list.add(new Area(1255, "海口", "haikou"));
        list.add(new Area(1256, "三亚", "sanya"));
        list.add(new Area(1257, "五指山", "wuzhishan"));
        list.add(new Area(1258, "琼海", "qionghai"));
        list.add(new Area(1259, "儋州", "danzhou"));
        list.add(new Area(1260, "文昌", "wenchang"));
        list.add(new Area(1261, "万宁", "wanning"));
        list.add(new Area(1262, "东方", "dongfang"));
        list.add(new Area(1263, "澄迈", "chengmai"));
        list.add(new Area(1264, "定安", "dingan"));
        list.add(new Area(1265, "屯昌", "tunchang"));
        list.add(new Area(1266, "临高", "lingao"));
        list.add(new Area(1267, "白沙", "baisha"));
        list.add(new Area(1268, "昌江", "changjiang"));
        list.add(new Area(1269, "乐东", "ledong"));
        list.add(new Area(1270, "陵水", "lingshui"));
        list.add(new Area(1271, "保亭", "baoting"));
        list.add(new Area(1272, "琼中", "qiongzhong"));
        list.add(new Area(1273, "西双版纳", "xishuangbanna"));
        list.add(new Area(1274, "德宏", "dehong"));
        list.add(new Area(1275, "昭通", "zhaotong"));
        list.add(new Area(1276, "昆明", "kunming"));
        list.add(new Area(1277, "大理", "dali"));
        list.add(new Area(1278, "红河", "honghe"));
        list.add(new Area(1279, "曲靖", "qujing"));
        list.add(new Area(1280, "保山", "baoshan"));
        list.add(new Area(1281, "文山", "wenshan"));
        list.add(new Area(1282, "玉溪", "yuxi"));
        list.add(new Area(1283, "楚雄", "chuxiong"));
        list.add(new Area(1284, "普洱", "puer"));
        list.add(new Area(1285, "临沧", "lincang"));
        list.add(new Area(1286, "怒江", "nujiang"));
        list.add(new Area(1287, "迪庆", "diqing"));
        list.add(new Area(1288, "丽江", "lijiang"));
        list.add(new Area(1289, "海北", "haibei"));
        list.add(new Area(1290, "西宁", "xining"));
        list.add(new Area(1291, "海东", "haidong"));
        list.add(new Area(1292, "黄南", "huangnan"));
        list.add(new Area(1293, "海南", "hainan"));
        list.add(new Area(1294, "果洛", "guoluo"));
        list.add(new Area(1295, "玉树", "yushu"));
        list.add(new Area(1296, "海西", "haixi"));
        list.add(new Area(1297, "西安", "xian"));
        list.add(new Area(1298, "咸阳", "xianyang"));
        list.add(new Area(1299, "延安", "yanan"));
        list.add(new Area(1300, "榆林", "yulin"));
        list.add(new Area(1301, "渭南", "weinan"));
        list.add(new Area(1302, "商洛", "shangluo"));
        list.add(new Area(1303, "安康", "ankang"));
        list.add(new Area(1304, "汉中", "hanzhong"));
        list.add(new Area(1305, "宝鸡", "baoji"));
        list.add(new Area(1306, "铜川", "tongchuan"));
        list.add(new Area(1307, "防城港", "fangchenggang"));
        list.add(new Area(1308, "南宁", "nanning"));
        list.add(new Area(1309, "崇左", "chongzuo"));
        list.add(new Area(1310, "来宾", "laibin"));
        list.add(new Area(1311, "柳州", "liuzhou"));
        list.add(new Area(1312, "桂林", "guilin"));
        list.add(new Area(1313, "梧州", "wuzhou"));
        list.add(new Area(1314, "贺州", "hezhou"));
        list.add(new Area(1315, "贵港", "guigang"));
        list.add(new Area(1316, "玉林", "yulin"));
        list.add(new Area(1317, "百色", "baise"));
        list.add(new Area(1318, "钦州", "qinzhou"));
        list.add(new Area(1319, "河池", "hechi"));
        list.add(new Area(1320, "北海", "beihai"));
        list.add(new Area(1321, "拉萨", "lasa"));
        list.add(new Area(1322, "日喀则地区", "rikazediqu"));
        list.add(new Area(1323, "山南地区", "shannandiqu"));
        list.add(new Area(1324, "林芝地区", "linzhidiqu"));
        list.add(new Area(1325, "昌都地区", "changdoudiqu"));
        list.add(new Area(1326, "那曲地区", "neiqudiqu"));
        list.add(new Area(1327, "阿里地区", "alidiqu"));
        list.add(new Area(1328, "银川", "yinchuan"));
        list.add(new Area(1329, "石嘴山", "shizuishan"));
        list.add(new Area(1330, "吴忠", "wuzhong"));
        list.add(new Area(1331, "固原", "guyuan"));
        list.add(new Area(1332, "中卫", "zhongwei"));
        list.add(new Area(1333, "塔城地区", "tachengdiqu"));
        list.add(new Area(1334, "哈密地区", "hamidiqu"));
        list.add(new Area(1335, "和田地区", "hetiandiqu"));
        list.add(new Area(1336, "阿勒泰地区", "aletaidiqu"));
        list.add(new Area(1337, "克孜勒苏", "kezilesu"));
        list.add(new Area(1338, "博尔塔拉", "boertala"));
        list.add(new Area(1339, "克拉玛依", "kelamayi"));
        list.add(new Area(1340, "乌鲁木齐", "wulumuqi"));
        list.add(new Area(1341, "石河子", "shihezi"));
        list.add(new Area(1342, "昌吉", "changji"));
        list.add(new Area(1343, "五家渠", "wujiaqu"));
        list.add(new Area(1344, "吐鲁番地区", "tulufandiqu"));
        list.add(new Area(1345, "巴音郭楞", "bayinguoleng"));
        list.add(new Area(1346, "阿克苏地区", "akesudiqu"));
        list.add(new Area(1347, "阿拉尔", "alaer"));
        list.add(new Area(1348, "喀什地区", "kashendiqu"));
        list.add(new Area(1349, "图木舒克", "tumushuke"));
        list.add(new Area(1350, "伊犁", "yili"));
        list.add(new Area(1351, "呼伦贝尔", "hulunbeier"));
        list.add(new Area(1352, "呼和浩特", "huhehaote"));
        list.add(new Area(1353, "包头", "baotou"));
        list.add(new Area(1354, "乌海", "wuhai"));
        list.add(new Area(1355, "乌兰察布", "wulanchabu"));
        list.add(new Area(1356, "通辽", "tongliao"));
        list.add(new Area(1357, "赤峰", "chifeng"));
        list.add(new Area(1358, "鄂尔多斯", "eerduosi"));
        list.add(new Area(1359, "巴彦淖尔", "bayannaoer"));
        list.add(new Area(1360, "锡林郭勒盟", "xilinguolemeng"));
        list.add(new Area(1361, "兴安盟", "xinganmeng"));
        list.add(new Area(1362, "阿拉善盟", "alashanmeng"));
        list.add(new Area(1363, "台北", "taibei"));
        list.add(new Area(1364, "高雄", "gaoxiong"));
        list.add(new Area(1365, "基隆", "jilong"));
        list.add(new Area(1366, "台中", "taizhong"));
        list.add(new Area(1367, "台南", "tainan"));
        list.add(new Area(1368, "新竹", "xinzhu"));
        list.add(new Area(1369, "嘉义", "jiayi"));
        list.add(new Area(1370, "澳门", "aomen"));
        list.add(new Area(1371, "香港", "xianggang"));


        return list;
    }

    public static final char[] firstChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z' };

    private List<Area> list;


    public void setListener() {
        // TODO Auto-generated method stub
        input.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence text, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                List<Area> list = new ArrayList<Area>();
                for (Area a : getAreas()) {
                    if (a.name.indexOf(text.toString()) > -1
                            || a.hzpy.indexOf(text.toString()) > -1) {
                        list.add(a);
                    }
                }
                list = initList(list);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            public void afterTextChanged(Editable view) {
            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                closeInput();
                return false;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                Area a = list.get(position);
//                cs.setChoseLocation(a.name);
//                cs.setChoseLocationID((int)a.id);
                Intent intent=new Intent();
                intent.putExtra("area",a);
                getActivity().setResult(11,intent);
                getActivity().finish();
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                Area a = (Area) adapter.getItem(firstVisibleItem);
                if (a!=null&&a.hideTitle != null && !a.hideTitle.equals("")) {
                    title_bar.setText(a.hideTitle);
                    title_bar.setVisibility(View.VISIBLE);
                } else {
                    title_bar.setText("");
                    title_bar.setVisibility(View.GONE);
                }
            }
        });

        getIndexView();
    }

    public List<Area> initList(List<Area> olist) {
        if (list != null) {
            list.clear();
        } else {
            list = new ArrayList<Area>();
        }
        int i = 0;
        for (Area a : olist) {
            if (a.hot) {
                if (i == 0) {
                    a.title = "热门城市";
                }
                a.hideTitle = "热门城市";
                list.add(a);
                i++;
            }
        }
        for (char c : firstChar) {
            i = 0;
            for (Area a : olist) {
                if (a.hzpy.charAt(0) == c && !a.hot) {
                    if (i == 0) {
                        a.title = String.valueOf(c).toUpperCase();
                        indexMap.put(a.title, list.size());
                    }
                    a.hideTitle = String.valueOf(c).toUpperCase();
                    list.add(a);
                    i++;
                }
            }
        }
        return list;
    }

    public void closeInput() {
        if (getActivity().getCurrentFocus() != null) {
            ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        }
    }


    HashMap<String, Integer> indexMap = new HashMap<String, Integer>();

    /** 绘制索引列表 */
    public void getIndexView() {
        final int height = (getResources().getDisplayMetrics().heightPixels - ImageUtil
                .dip2px(getActivity(), 138)) / firstChar.length;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, height);
        // params.setMargins(10, 5, 10, 0);
        for (int i = 0; i < firstChar.length; i++) {
            final TextView tv = new TextView(getActivity());
            tv.setLayoutParams(params);
            tv.setText(String.valueOf(firstChar[i]).toUpperCase() + "");
            tv.setTextColor(getResources().getColor(R.color.location_txt));
            tv.setTextSize(12);
            tv.setGravity(Gravity.CENTER);
            layoutIndex.addView(tv);
            layoutIndex.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event)

                {
                    float y = event.getY();
                    int index = (int) (y / height);
                    if (index > -1 && index < firstChar.length) {// 防止越界
                        String key = String.valueOf(firstChar[index])
                                .toUpperCase();
                        if(indexMap.containsKey(key)){
                            int pos = indexMap.get(key);
                            listview.setSelection(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }

}
