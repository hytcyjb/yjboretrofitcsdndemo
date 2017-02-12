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
}
