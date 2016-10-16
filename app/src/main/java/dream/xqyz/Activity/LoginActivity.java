package dream.xqyz.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.User;
import dream.xqyz.R;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class LoginActivity extends BaseActivity implements BaseInterface {

    //用户名
    @ViewInject(R.id.act_login_userNameET)
    EditText userNameEt;
    //密码
    @ViewInject(R.id.act_login_passWordET)
    EditText passWordEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
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

    }

    //点击登录
    @OnClick(R.id.act_login_loginTv)
    public void onLoginClick(View v) {
        //获取用户名
        String userNameStr = userNameEt.getText().toString();
        //获取密码
        String passWordStr = passWordEt.getText().toString();
        if (userNameStr.length() < 1) {
            toastS("请输入用户名");
            return;
        }
        if (passWordStr.length() < 1) {
            toastS("请输入密码");
            return;
        }
        User.loginByAccount(userNameStr, passWordStr, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    MyApplication.user = user;
                    toastS("登录成功");
                    finish();
                }
            }
        });
    }

    //点击跳转注册
    @OnClick(R.id.act_login_regLin)
    public void onRegClick(View v){
        startAct(RegActivity.class);
    }

    //返回
    @OnClick(R.id.act_login_backLin)
    public void onBackClick(View v) {
        finish();
    }
}
