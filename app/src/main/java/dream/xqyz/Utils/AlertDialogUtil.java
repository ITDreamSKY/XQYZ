package dream.xqyz.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by SKYMAC on 16/8/17.
 */
public class AlertDialogUtil {
    /**
     * 用来获取一个新的AlertDialog
     * @param context
     * @param title
     * @param message
     * @param flag 是否允许用户取消此dialog
     * @param alertListener 用户点击确定或取消后的点击事件
     * @return
     */
    public static AlertDialog createAlertDialog(Context context, CharSequence title, CharSequence message, boolean flag, final AlertListener alertListener){
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertListener.onYClick(dialogInterface,i);
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertListener.onNClick(dialogInterface,i);
            }
        }).create();
        alertDialog.setCancelable(flag);
        return alertDialog;
    }

    public interface AlertListener{
        void onYClick(DialogInterface dialogInterface, int i);
        void onNClick(DialogInterface dialogInterface, int i);
    }
}
