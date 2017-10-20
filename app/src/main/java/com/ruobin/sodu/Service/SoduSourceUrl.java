package com.ruobin.sodu.Service;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ds on 2017/10/20.
 */

public class SoduSourceUrl {
    /// <summary>
    /// 少年文学
    /// </summary>
    private static String Snwx = "www.snwx8.com";
    /// <summary>
    /// 大海中文
    /// </summary>
    public static String Dhzw = "www.dhzw.org";
    /// <summary>
    /// 爱上中文
    /// </summary>
    private static String Aszw = "www.aszw.org";
    /// <summary>
    /// 7度书屋
    /// </summary>
    public static String Qdsw = "www.7dsw.com";
    /// <summary>
    ///云来阁
    /// </summary>
    public static String Ylg = "www.yunlaige.com";
    /// <summary>
    /// 古古
    /// </summary>
    public static String Ggxs = "www.55xs.com";
    /// <summary>
    /// 风云
    /// </summary>
    public static String Fyxs = "www.baoliny.com";
    /// <summary>
    /// 第九中文网
    /// </summary>
    public static String Dijiuzww = "dijiuzww.com";
    /// <summary>
    /// 手牵手小说
    /// </summary>
    public static String Sqsxs = "www.sqsxs.com";
    /// <summary>
    /// 风华居
    /// </summary>
    public static String Fenghuaju = "www.fenghuaju.cc";
    /// <summary>
    ///80小说（暂不处理）
    /// </summary>
    //    String  su80 = "www.su80.org";
    /// <summary>
    ///木鱼哥
    /// </summary>
    public static String Myg = "www.muyuge.com";
    /// <summary>
    /// 乐文
    /// </summary>
    public static String Lww = "www.lwtxt.net";
    /// <summary>
    /// 卓雅居
    /// </summary>
    public static String Zyj = "www.zhuoyaju.com";
    /// <summary>
    /// 81xs
    /// </summary>
    public static String Xs81 = "www.81xsw.com";
    /// <summary>
    /// 大书包
    /// </summary>
    public static String Dsb = "www.dashubao.cc";
    /// <summary>
    /// 漂流地
    /// </summary>
    public static String Pld = "piaoliudi.com";
    /// <summary>
    /// 齐鲁文学
    /// </summary>
    public static String Qlwx = "www.76wx.com";

    ///// <summary>
    /////第七中文（挂了）
    ///// </summary>
    //public  String Dqzw = "www.d7zy.com";
    ///// <summary>
    ///// 第九中文网（挂了）
    ///// </summary>
    //public  String Dijiuzww = "dijiuzww.com";
    ///// <summary>
    ///// 清风小说（挂了）
    ///// </summary>
    //public  String Qfxs = "www.qfxs.cc";
    /// <summary>
    /// 窝窝小说网2（挂了）
    /// </summary>
    //    String  wwxsw2 = "www.biquge120.com";
    ///// <summary>
    ///// 找书网（挂了）
    ///// </summary>
    //public  String Zsw = "www.zhaodaoshu.com";
    ///// <summary>
    ///// 去笔趣阁（挂了）
    ///// </summary>
    //public  String Xbiquge = "www.xbiquge.net";
    /// <summary>
    /// 倚天中文（挂了）
    /// </summary>
    //    String  ytzww = "www.ytzww.com";
    ///// <summary>
    ///// 书路小说（挂了）
    ///// </summary>
    //public  String Shu6 = "www.shu6.cc";
    ///// <summary>
    /////4k中文(挂了）
    ///// </summary>
    //public  String Fourkzw = "www.4kzw.com";
    ///// <summary>
    /////幼狮书盟(挂了)
    ///// </summary>
    //public  String Yssm = "www.youshishumeng.com";
    ///// <summary>
    /////轻语小说（挂了）
    ///// </summary>
    //public  String Qyxs = "www.qingyuxiaoshuo.com";
    ///// <summary>
    ///// 秋水轩（挂了）
    ///// </summary>
    //public  String Qsx = "www.qiushuixuan.cc";
    ///// <summary>
    ///// 风云(挂了)
    ///// </summary>
    //public  String Fyxs = "www.baoliny.com";
    public static boolean checkUrl(String url) {
        boolean result = false;
        try {

            URI uri = new URI(url);
            String host = uri.getHost();
            List<String> values = getPropertyInfoArray();
            if (values == null) {
                return false;
            }

            for (String str : values) {

                if (str.equals(url)) {
                    result = true;
                    break;
                }
            }
            return result;
        } catch (Exception ex) {
            return false;
        }
    }


    public static List<String> getPropertyInfoArray() {

        SoduSourceUrl cls = new SoduSourceUrl();

        List<String> values = new ArrayList<>();

        Field[] field = cls.getClass().getDeclaredFields();
        // 遍历所有属性
        for (int j = 0; j < field.length; j++) {
            try {

                field[j].setAccessible(true); //
                // 获取属性的名字
                String name = field[j].getName();
                String value = (String) field[j].get(cls);

                values.add(value);
                // 获取属性的类型
            } catch (Exception ex) {
                continue;
            }
        }

        return values;
    }


    public static  int getIndex(String url){

        List<String> values=  getPropertyInfoArray();

        return  values.indexOf(url);

    }

    public static String getSnwx() {
        return Snwx;
    }

    public static void setSnwx(String snwx) {
        Snwx = snwx;
    }
}


