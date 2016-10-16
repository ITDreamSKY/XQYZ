package dream.xqyz.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.User;
import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/1.
 */
public class SettingNickNameActivity extends BaseActivity implements BaseInterface {

    //用户名
    @ViewInject(R.id.act_setting_nickname_Et)
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settting_nickname);
        initView();
        initData();
        initViewOper();
    }

    @Override
    public void initView() {
        ViewUtils.inject(act);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewOper() {
        if (MyApplication.user.getNickName() != null) {
            et.setText(MyApplication.user.getNickName());
        }
    }

    //修改用户名点击
    @OnClick(R.id.act_setting_nickname_But)
    public void onClick(View v) {
        String text = et.getText().toString().trim();
        User user = new User();
        user.setNickName(text);
        user.update(MyApplication.user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastS("修改成功");

                    //服务端的user刷新了 但是本地的并没有刷新 所有再次登陆一次
//                    SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
//                    String uname = sharedPreferences.getString("uname", "");
//                    String upass = sharedPreferences.getString("upass", "");
//                    User.loginByAccount(uname, upass, new LogInListener<User>() {
//                        @Override
//                        public void done(User userBean, BmobException e) {
//                            if (e == null) {
//                                MyApplication.user = userBean;
//                            } else {
//                            }
//                        }
//                    });
                    MyApplication.user = BmobUser.getCurrentUser(User.class);

                    finish();
                } else {
                    toastS("修改失败" + e.toString());
                }
            }
        });
    }

    @OnClick(R.id.act_setting_nickname_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }
}
