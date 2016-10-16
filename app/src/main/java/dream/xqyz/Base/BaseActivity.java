package dream.xqyz.Base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by SKYMAC on 16/9/17.
 */
public class BaseActivity extends FragmentActivity {

    public BaseActivity act;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        act = null;
    }

    public void toastS(String text) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }

    public void toastL(String text) {
        Toast.makeText(act, text, Toast.LENGTH_LONG).show();
    }

    public ProgressDialog showProgressDialog(String title, String message, boolean isCancelable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
        progressDialog.show();
        return null;
    }

    public void dismissProgressdialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void startAct(Class<?> cls) {
        startActivity(new Intent(act, cls));
    }

    public ProgressDialog createProgressDialog(String title, String message, boolean flag) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(flag);
//        progressDialog.show();
        return progressDialog;
    }
}
