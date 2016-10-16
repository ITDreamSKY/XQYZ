package dream.xqyz.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;

import java.io.File;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import dream.xqyz.Activity.MyBuyActivity;
import dream.xqyz.Activity.GoodActivity;
import dream.xqyz.Activity.MyPublishActivity;
import dream.xqyz.Activity.MySaleActivity;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseFragment;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.R;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class MineFragment extends BaseFragment implements BaseInterface {

    private ImageView userIconImg;
    private TextView nickNameTv;
    private LinearLayout userLin;
    //我赞过的
    private LinearLayout goodLin;
    //我发布的
    private LinearLayout publishLin;
    //我购买的
    private LinearLayout myBuyLin;
    //我售出的
    private LinearLayout mySaleLin;

    @Override
    protected void init() {
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.my_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nickNameTv = (TextView) view.findViewById(R.id.fragment_mine_nickNameTv);
        userIconImg = (ImageView) view.findViewById(R.id.fragment_mine_userIconImg);
        userLin = (LinearLayout) view.findViewById(R.id.fragment_mine_userLin);
        if (MyApplication.user != null) {
            userLin.setClickable(false);
            if (MyApplication.user.getNickName() == null) {
                nickNameTv.setText("未设置昵称");
            } else {
                nickNameTv.setText(MyApplication.user.getNickName());
            }
            userIconImg.setClickable(true);

            //二级缓存头像
            final File file = new File(MyApplication.file, "/" + MyApplication.user.getObjectId() + "/" + MyApplication.user.getObjectId() + ".jpeg");
            Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bit == null) {
                //如果用户设置了头像的话
                if (MyApplication.user.getUserIcon() != null) {
                    MyApplication.user.getUserIcon().download(file.getAbsoluteFile(), new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //bit设置不了final
                                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
                                //第二步 设置图片
                                userIconImg.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            } else {
                userIconImg.setImageBitmap(bit);
            }
        }
        goodLin = (LinearLayout) view.findViewById(R.id.fragment_mine_goodLin);
        goodLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.user == null) {
                    toastS("请您先登陆");
                    return;
                }
                startAct(GoodActivity.class);
            }
        });
        publishLin = (LinearLayout) view.findViewById(R.id.fragment_mine_publishLin);
        publishLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.user == null) {
                    toastS("请您先登陆");
                    return;
                }
                startAct(MyPublishActivity.class);
            }
        });
        myBuyLin = (LinearLayout) view.findViewById(R.id.fragment_mine_mybuyLin);
        myBuyLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.user == null) {
                    toastS("请您先登陆");
                    return;
                }
                startAct(MyBuyActivity.class);
            }
        });
        mySaleLin = (LinearLayout) view.findViewById(R.id.fragment_mine_mysaleLin);
        mySaleLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.user == null) {
                    toastS("请您先登陆");
                    return;
                }
                startAct(MySaleActivity.class);
            }
        });
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
}
