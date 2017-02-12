package yy.com.yjboretrofitcsdndemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 公共方法类
 *
 * @author yjbo
 * @time 2017/2/11 13:50
 */
public class CommonUtil {
    /**
     * 构想来源于：https://github.com/Ericsongyl/AndroidToastUtil
     * 此处是仅仅将toast的样式修改了，上面的可以看到有自定义的图片以及显示的时间
     *
     * @author yjbo  @time 2017/2/11 13:57
     */
    public static void toast(Context mcontext, String toastStr, int i) {
        Toast toast = Toast.makeText(mcontext, toastStr, Toast.LENGTH_SHORT);
        toast.setGravity(i, 0, 0);//Gravity.CENTER
        toast.show();
    }

    /***
     * 检查网络
     * @return
     */
    public static boolean checkNet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对像
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info == null || !info.isAvailable()) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 获取提示信息
     * @author yjbo
     * @time 2017/2/12 14:23
     */
    public static String getTipStr(final int netGet, final int nonetGet){
        int cacheTime = setCacheTime;
        String netGetStr = "";
        String nonetGetStr = "";
        switch (netGet) {
            case 0:
                netGetStr = "有网实时更新";
                break;
            case 1:
                netGetStr = "有网时设置缓存时间为" + cacheTime + "s,之后再次获取数据";
                break;
        }
        switch (nonetGet) {
            case 0:
                nonetGetStr = "无网时不设置缓存时间，一直可以获取缓存";
                break;
            case 1:
                nonetGetStr = "无网时设置缓存时间为" + cacheTime + "s,之后不能获取缓存";
                break;
        }
        return "当前是：" + "\n1." + netGetStr + "\n2." + nonetGetStr;
    }
    /**
     * 设置缓存时间
     * @author yjbo
     * @time 2017/2/12 14:25
     */
    public static int  setCacheTime = 60;
}
