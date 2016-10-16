package dream.xqyz.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.User;
import dream.xqyz.R;
import dream.xqyz.Utils.AlertDialogUtil;
import dream.xqyz.Utils.NetUtil;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class InitActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.init_img)
    private ImageView img;
    private Animation anim;
    private boolean isNet = false;
    private AlertDialog alertDialog;
    private static ProgressDialog progressDialog = null;
    private static Boolean FLAG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_init);
        //第一步就开始获取本地用户名密码 并且执行登陆验证
//        getLoginInfo();

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
        //加载动画
        anim = AnimationUtils.loadAnimation(act, R.anim.act_init_alpha);
        //设置动画监听器
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                isNet = NetUtil.isNet(act);
            }

            //当动画执行结束
            @Override
            public void onAnimationEnd(Animation animation) {
                if (isNet) {
                    MyApplication.user = BmobUser.getCurrentUser(User.class);
                    startAct(HomeActivity.class);
                    finish();
                    ///////
//                    getLoginInfo();
                    /**
                     * 8月28日优化 获取完登陆信息之后 再刷新列表 把这行放到getLoginInfo里了
                     */
//                    XYApplication.findGathers();
                } else {
                    showAlertDialog();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 8.26 把跳转的地方都进行了优化 现在是主页动态数据 加载完毕后 在回调监听里 进行跳转
     */
    @Override
    public void initViewOper() {
        img.startAnimation(anim);
        //监听数据是否下载完毕
//        MyApplication.setFindListener(new XYApplication.findGatherListener() {
//            @Override
//            public void findFinish() {
//                //数据查询完毕
////                startActivity(HomeActivity.class);
////                finish();
//            }
//        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FLAG = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (FLAG) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
//        if (progressDialog == null) {
//            progressDialog = createProgressDialog(null, "获取网络中......", false);
//        }else{
//            progressDialog.setMessage("获取网络中......");
//        }
//        progressDialog.show();
            if (progressDialog == null) {
                progressDialog = createProgressDialog(null, "获取网络中......", false);
                progressDialog.show();
            } else {
                progressDialog.show();
            }
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 84; i++) {
                        if (i > 20) {
                            isNet = NetUtil.isNet(act);
                            if (isNet) {
                                break;
                            }
                        }
                        String text = "获取网络中";
                        switch (i % 6) {
                            case 0:
                                text += ".";
                                break;
                            case 1:
                                text += "..";
                                break;
                            case 2:
                                text += "...";
                                break;
                            case 3:
                                text += "....";
                                break;
                            case 4:
                                text += ".....";
                                break;
                            case 5:
                                text += "......";
                                break;
                        }
//                    logI(i%6+"","tag");
                        final String text2 = text;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage(text2);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            isNet = NetUtil.isNet(act);
                            if (isNet) {
//                                startActivity(HomeActivity.class);
//                                finish();
                                /////////////
//                                getLoginInfo();
                                /**
                                 * 8月28日 注释掉了 防止加载完图片之前就跳转
                                 */
//                                XYApplication.findGathers();
                                MyApplication.user = BmobUser.getCurrentUser(User.class);
                                startAct(HomeActivity.class);
                                finish();
                            } else {
                                showAlertDialog();
                            }
                        }
                    });
                }
            }.start();
        }

    }

    //没有网络时弹出提示框
    private void showAlertDialog() {
        if (alertDialog == null) {
            alertDialog = AlertDialogUtil.createAlertDialog(act, "警告", "无法访问网络，请检查您的网络设置。", false, new AlertDialogUtil.AlertListener() {
                @Override
                public void onYClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }

                @Override
                public void onNClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
        }
        alertDialog.show();
    }

    //从本地获取数据 验证登陆信息
    public void getLoginInfo() {
        MyApplication.user = BmobUser.getCurrentUser(User.class);
    }

    //屏蔽返回按键
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
