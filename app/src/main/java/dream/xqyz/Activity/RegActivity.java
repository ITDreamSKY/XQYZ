package dream.xqyz.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.User;
import dream.xqyz.R;
import dream.xqyz.Receiver.SmsReceiver;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class RegActivity extends BaseActivity implements BaseInterface {

    //手机号码Et
    @ViewInject(R.id.act_reg_phoneNumberEt)
    private EditText phoneNumberEt;
    //短信验证码
    @ViewInject(R.id.act_reg_codeEt)
    private EditText codeEt;
    //密码
    @ViewInject(R.id.act_reg_passEt)
    private EditText passEt;
    //确认密码
    @ViewInject(R.id.act_reg_checkEt)
    private EditText checkEt;
    //获取验证码
    @ViewInject(R.id.act_reg_getCodeTv)
    private TextView getCodeTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_reg);
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

    //返回按钮
    @OnClick(R.id.act_reg_backLin)
    public void onBackClick(View v) {
        finish();
    }


    //获取短信验证码
    private CountDownTimer timer;

    @OnClick(R.id.act_reg_getCodeTv)
    public void onGetCodeClick(View v) {
        String phoneStr = phoneNumberEt.getText().toString().trim();
        if (!phoneStr.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            toastS("手机号格式有误，请重新输入。");
            return;
        }
        //开启短信广播接收
        SmsReceiver.setSmsListener(new SmsReceiver.SmsListener() {
            @Override
            public void toSms(String text) {
                codeEt.setText(text);
            }
        });
        //将获取验证码按钮设置成不可点击状态
        getCodeTv.setClickable(false);
        //设置按钮文字颜色
        getCodeTv.setTextColor(Color.parseColor("#626262"));

        //Bomb 发送验证码
        BmobSMS.requestSMSCode(act, phoneStr, "注册登陆短信验证", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    toastS("验证码已发送");
                } else {
                    toastL("验证码发送失败" + e.getMessage().toString());
                }
            }
        });
        if (timer == null) {
            timer = new CountDownTimer(10000, 1000) {
                //此方法自动在UI线程执行
                @Override
                public void onTick(long l) {
                    getCodeTv.setText((l / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    getCodeTv.setClickable(true);
                    getCodeTv.setTextColor(Color.parseColor("#000000"));
                    getCodeTv.setText("获取验证码");
                }
            };
        }
        timer.start();
    }

    //注册按钮
    @OnClick(R.id.act_reg_regTv)
    public void onRegClick(View v) {
        //获取手机号码
        final String phoneStr = phoneNumberEt.getText().toString().trim();
        //验证手机号码格式
        if (!phoneStr.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            toastS("手机号格式有误，请重新输入。");
            return;
        }
        //获取验证码
        String codeStr = codeEt.getText().toString().trim();
        //获取密码
        final String passwordStr = passEt.getText().toString().trim();
        //验证密码格式
        if (!passwordStr.matches("^[a-zA-Z]\\w{6,15}$")) {
            toastS("请检查密码格式：以字母开头，长度为7-16位。");
            return;
        }
        //获取再次输入的密码
        String checkPasswordStr = checkEt.getText().toString().trim();
        //验证两次输入的密码是否一致
        if (!checkPasswordStr.equals(passwordStr)) {
            toastS("两次密码输入不一致！");
            return;
        }
        //验证手机号与验证码是否匹配
        BmobSMS.verifySmsCode(act, phoneStr, codeStr, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    User user = new User();
                    user.setUsername(phoneStr);
                    user.setMobilePhoneNumber(phoneStr);
                    user.setMobilePhoneNumberVerified(true);
                    user.setPassword(passwordStr);
                    //注册
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User userBean, cn.bmob.v3.exception.BmobException e) {
                            if (e == null) {
                                toastL("恭喜您，注册成功！");
//                                SharedPreferences sharedPreferences = RegActivity.this.getSharedPreferences("User",MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("uname",phoneStr);
//                                editor.putString("upass",passwordStr);
//                                editor.commit();
                                //将返回的对象放到Application中
//                                MyApplication.user = userBean;

                                //关闭当前页面
                                finish();
                                //关闭上一层登陆页面
//                                LoginActivity.act.finish();
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    toastL("验证码有误！请重试！");
                    e.printStackTrace();
                }
            }
        });

    }
}
