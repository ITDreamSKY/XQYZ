package dream.xqyz.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.R;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class PublishActivity extends BaseActivity implements BaseInterface {


    //商品名称
    @ViewInject(R.id.act_publish_titleEt)
    private EditText nameEt;
    //商品详细信息
    @ViewInject(R.id.act_publish_contentEt)
    private EditText contentEt;
    //商品价格
    @ViewInject(R.id.act_publish_moneyEt)
    private EditText moneyEt;

    //图片Lin
    @ViewInject(R.id.act_publish_photoLinearLayout)
    private LinearLayout photoLin;
    //图片Lin2
    @ViewInject(R.id.act_publish_photoLinearLayout2)
    private LinearLayout photoLin2;
    //地址
    @ViewInject(R.id.act_publish_siteTv)
    private TextView addressTv;


    //添加图片
    private ImageView addPhoto;

    //用户缓存图片地址的集合
    private List<String> imgPaths;
    //集合中存储已经添加的ImageView(不包含加号)
    private List<ImageView> imgs;

    private List<Bitmap> bitmaps;

    //百度地图获取来的信息
    private PoiInfo poiInfo;
    //经纬度
    private LatLng latLng;
    //地址信息
    private String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish);
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        poiInfo = (PoiInfo) MyApplication.getData("poiInfo", true);
        if (poiInfo != null) {
            latLng = poiInfo.location;
            address = poiInfo.address;
            //设置可滑动
            addressTv.setMovementMethod(ScrollingMovementMethod.getInstance());
            addressTv.setText(address);
        }
    }

    @Override
    public void initView() {
        ViewUtils.inject(act);
        addPhoto = new ImageView(act);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) photoLin.getLayoutParams();
        addPhoto.setLayoutParams(new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth() / 3 - 40, (getWindowManager().getDefaultDisplay().getWidth() / 3 - 40) / 3 * 2));
        addPhoto.setImageResource(R.drawable.addphoto);
        photoLin.addView(addPhoto);
    }

    @Override
    public void initData() {
        imgPaths = new ArrayList<String>();
        imgs = new ArrayList<ImageView>();
        bitmaps = new ArrayList<Bitmap>();
    }

    @Override
    public void initViewOper() {
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddPhotoDialog();
            }
        });
    }

    private AlertDialog dialog;

    private void showAddPhotoDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(act).setTitle("设置头像").setMessage("请选择头像来源")
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
                            intent.putExtra("outputX", 200);
                            intent.putExtra("outputY", 200);
                            File file = new File(MyApplication.file, "/photo" + imgPaths.size() + ".jpeg");
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
                            File file = new File(MyApplication.file, "/photoCache.jpeg");
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
        File file = new File(MyApplication.file, "/photo" + imgPaths.size() + ".jpeg");
        Log.i("SKY", "验证路径：" + file.toString());
        if (requestCode == 0 || requestCode == 2) {
            if (!file.exists()) {
                toastS("文件不存在");
                return;
            }
            Log.i("IMG", "request 0 || 2 进入");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //添加图片
            addImageList(bitmap);

        } else if (requestCode == 1) {
            File fileCache = new File(MyApplication.file, "/photoCache.jpeg");
            Log.i("SKY", "拍照缓存路径：" + fileCache.toString());
            Log.i("IMG", "request 1 进入");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(fileCache), "image/*"); // 要裁剪的图片URI
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            // aspectX：aspectY 裁剪比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
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

    private void addImageList(final Bitmap bitmap) {

        ImageView imageView = new ImageView(act);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) photoLin.getLayoutParams();
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(params.width / 3 - 10, (params.width / 3 - 10) / 3 * 2));
        Display dis = getWindowManager().getDefaultDisplay();
        imageView.setLayoutParams(new LinearLayout.LayoutParams(dis.getWidth() / 3 - 40, (dis.getWidth() / 3 - 40) / 3 * 2));
        imageView.setImageBitmap(bitmap);


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                for (int i = 0; i < imgs.size(); i++) {
                    if (imgs.get(i) == view) {
                        //标记是哪一行 第一行为true
//                        boolean f = false;
//                        if (i < 3) {
//                            f = true;
//                        }
//                        final boolean flag = f;
                        new AlertDialog.Builder(act).setMessage("是否删除此照片？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                if(flag){
//                                    photoLin.removeView(view);
//                                }else{
//                                    photoLin2.removeView(view);
//                                }
                                bitmaps.remove(imgs.indexOf(view));
                                imgs.remove(view);


                                photoLin.removeAllViews();
                                photoLin2.removeAllViews();
                                for (int j = 0; j < imgs.size(); j++) {
                                    if (j < 3) {
                                        photoLin.addView(imgs.get(j));
                                    } else {
                                        photoLin2.addView(imgs.get(j));
                                    }
                                }
                                if (imgs.size() < 3) {
                                    photoLin.addView(addPhoto);
                                } else {
                                    photoLin2.addView(addPhoto);
                                }
                            }
                        }).setNegativeButton("取消", null).show();
                    }
                }
                return true;
            }
        });
        if (imgs.size() < 3) {
            if (imgs.size() == 2) {
                //特殊情况 再添加图片的话 第一行装满 加号移至第二行
                //移除加号
                photoLin.removeView(addPhoto);
                photoLin.addView(imageView);
                photoLin2.addView(addPhoto);
            } else {
                //移除加号
                photoLin.removeView(addPhoto);
                photoLin.addView(imageView);
                photoLin.addView(addPhoto);
            }
        } else {
            if (imgs.size() == 5) {
                //移除加号
                photoLin2.removeView(addPhoto);
                photoLin2.addView(imageView);
            } else {
                //移除加号
                photoLin2.removeView(addPhoto);
                photoLin2.addView(imageView);
                photoLin2.addView(addPhoto);
            }
        }
        //添加进List
        imgs.add(imageView);
        bitmaps.add(bitmap);
    }

    //点击地图跳转
    @OnClick(R.id.act_publish_siteTv)
    public void onMapClick(View v) {
        startAct(MapActivity.class);
    }

    //点击发布
    @OnClick(R.id.act_publish_submitTv)
    public void onSubmitClick(View v) {
        final String nameStr = nameEt.getText().toString();
        final String contentStr = contentEt.getText().toString();
        final String addressStr = addressTv.getText().toString();
        String moneyStr = moneyEt.getText().toString().trim();
        if (nameStr.equals("")) {
            toastS("请输入商品名称");
            return;
        }
        if (contentStr.equals("")) {
            toastS("请输入商品详细信息");
            return;
        }
        if (addressStr.equals("")) {
            toastS("请选择您的位置");
            return;
        }
        if (moneyStr.equals("")) {
            toastS("请输入商品价格");
            return;
        }
        final Double money;
        try {
            money = Double.parseDouble(moneyStr);
        } catch (Exception e) {
            toastS("价格输入有误");
            return;
        }

        final Commodity bean = new Commodity();
        bean.setUser(MyApplication.user);
        bean.setName(nameStr);
        bean.setContent(contentStr);
        bean.setAddress(addressStr);
        bean.setLocation(latLng);
        bean.setPrice(money);








        final String[] paths = new String[bitmaps.size()];
        for (int i = 0; i < bitmaps.size(); i++) {
            File file = new File(MyApplication.file, "sendGather" + i + ".jpeg");
            try {
                bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            paths[i] = file.getAbsolutePath();
        }


        final ProgressDialog progressDialog = createProgressDialog(null, "正在上传图片 1/" + paths.length, false);
        progressDialog.show();

        //批量上传
        BmobFile.uploadBatch(paths, new UploadBatchListener() {

            //批量上传每成功一次 就会执行一次这个方法 所以需要判断 所有文件上传完毕
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                if (files.size() == paths.length) {
                    progressDialog.setMessage("图片上传成功！活动发布中..");
                    bean.setImgs(files);
                    //上传服务器
                    bean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            progressDialog.dismiss();
                            if (e == null) {
                                toastS("商品发布成功");
                                finish();
                            } else {
                                e.printStackTrace();
                                toastL("商品发布失败，请检查您的网络。");
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                toastL("错误码" + statuscode + ",错误描述：" + errormsg);
                progressDialog.dismiss();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                progressDialog.setMessage("正在上传图片 " + curIndex + "/" + paths.length);
            }
        });



    }

}
