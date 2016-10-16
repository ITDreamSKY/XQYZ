package dream.xqyz.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.User;
import dream.xqyz.R;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class SettingActivity extends BaseActivity implements BaseInterface {

    public static Activity settingAct;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shezhi_layout);
        initView();
        initData();
        initViewOper();
        settingAct = act;
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

    //点击返回
    @OnClick(R.id.act_setting_backLin)
    public void onBackClick(View v) {
        finish();
    }

    //点击昵称
    @OnClick(R.id.act_setting_nicknameLin)
    public void onNickClick(View v) {
        startAct(SettingNickNameActivity.class);
    }

    //点击修改密码
    @OnClick(R.id.act_setting_changePassLin)
    public void onPassWordClick(View v) {
        startAct(SettingPasswordActivity.class);
    }

    //退出登录
    @OnClick(R.id.act_setting_logoutTv)
    public void onLogOutClick(View v) {
        MyApplication.user = null;
        BmobUser.logOut();
        toastL("注销成功");
        finish();
    }

    //点击修改头像
    @OnClick(R.id.act_setting_userIconLin)
    public void onSetUserIconClick(View view) {
        showSetUserIconDialog();
    }

    //上传图片弹出AlertDialog选择
    public void showSetUserIconDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(act).setTitle("设置头像").setMessage("请选择图片来源")
                    .setPositiveButton("相册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            // 设置内容类型
                            intent.setType("image/*");
                            // 剪裁
                            intent.putExtra("crop", "circleCrop");
                            // 裁剪比例
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 250);
                            intent.putExtra("outputY", 250);
                            File file = new File(MyApplication.file, "/userIcon" + ".jpeg");
                            Log.i("SKY", "相册保存路径：" + file.toString());
                            if (file.exists()) {
                                file.delete();
                            }
                            intent.putExtra("output", Uri.fromFile(file.getAbsoluteFile()));
                            startActivityForResult(intent, 0);
                        }
                    }).setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i("IMG", "拍照");
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //拍照缓存的图片不是用来上传的 用来裁剪使用
                            File file = new File(MyApplication.file, "/userIconCache" + ".jpeg");
                            Log.i("SKY", "拍照缓存路径：" + file.toString());
                            if (file.exists()) {
                                file.delete();
                            }
                            intent.putExtra("output", Uri.fromFile(file.getAbsoluteFile()));
                            startActivityForResult(intent, 1);
                        }
                    }).create();
        }
        dialog.show();
    }


    //当返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = new File(MyApplication.file, "/userIcon" + ".jpeg");
        Log.i("SKY", "验证路径：" + file.toString());
        if (requestCode == 0 || requestCode == 2) {
            if (!file.exists()) {
                return;
            }
            Log.i("IMG", "request 0 || 2 进入");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //添加图片
//            HomeActivity.userIconImg.setImageBitmap(bitmap);
            //将图片上传至服务器
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        User u = new User();
                        u.setUserIcon(bmobFile);
                        u.update(MyApplication.user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    toastS("图片上传成功！");
                                    //重新获取用户
                                    User user = BmobUser.getCurrentUser(User.class);
                                    MyApplication.user = user;
                                    File oldFile = new File(MyApplication.file, "/" + MyApplication.user.getObjectId() + "/" + MyApplication.user.getObjectId() + ".jpeg");
                                    if (oldFile.exists()) {
                                        oldFile.delete();
                                    }
                                } else {
                                    toastS("图片上传失败!");
                                }
                            }
                        });

                    } else {
                        toastS("图片上传失败，请重试！");
                    }
                }
            });

        } else if (requestCode == 1) {
            File fileCache = new File(MyApplication.file, "/userIconCache" + ".jpeg");
            Log.i("SKY", "拍照缓存路径：" + fileCache.toString());
            Log.i("IMG", "request 1 进入");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(fileCache), "image/*"); // 要裁剪的图片URI
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            // aspectX：aspectY 裁剪比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 250);
            intent.putExtra("outputY", 250);
            // 输出图片大小 intent.putExtra("outputY", 1024);
            intent.putExtra("return-data", false);
            // 是否以bitmap方式返回，缩略图可设为true，大图一定要设为false，返回URI
            intent.putExtra("noFaceDetection", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            // 输出的图片的URI
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 返回格式 intent.putExtra("scale", true);// 去黑边 intent.putExtra("scaleUpIfNeeded", true);
            // 去黑边
            startActivityForResult(intent, 2); // activity result
        }
    }
}
