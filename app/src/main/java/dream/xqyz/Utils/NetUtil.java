package dream.xqyz.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/9/17 0017.
 */
public class NetUtil {
    public static boolean isNet(Context context){
        //获取网络链接管理器
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络对象
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info==null){
            return false;
        }
        //返回当前连接状态
        return info.isConnected();
    }
}
