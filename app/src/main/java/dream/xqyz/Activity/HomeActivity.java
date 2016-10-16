package dream.xqyz.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import dream.xqyz.Adapter.HomeActViewPagerAdapter;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseFragment;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Fragment.ClFragment;
import dream.xqyz.Fragment.HomeFragment;
import dream.xqyz.Fragment.MessageFragment;
import dream.xqyz.Fragment.MineFragment;
import dream.xqyz.R;
import dream.xqyz.View.MyImageView;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class HomeActivity extends BaseActivity implements BaseInterface, View.OnClickListener {

    //用来存放按钮对象
    private ImageView[] butImgs;
    //用来存放按钮图片索引 点击状态
    private int[] butOnResIds;
    //未点击状态
    private int[] butOffResIds;

    private LinearLayout[] linearLayouts = new LinearLayout[4];
    //存放LinearLayout的id
    private int[] linResIds;

    //按钮图片
    @ViewInject(R.id.act_home_homeImg)
    private ImageView homeImg;
    @ViewInject(R.id.act_home_clImg)
    private ImageView clImg;
    @ViewInject(R.id.act_home_messageImg)
    private ImageView messageImg;
    @ViewInject(R.id.act_home_mineImg)
    private ImageView mineImg;

    //vp
    @ViewInject(R.id.act_home_vp)
    private ViewPager vp;

    //vp adapter
    private HomeActViewPagerAdapter adapter;

    //数据源fragment
    private List<BaseFragment> fragments = new ArrayList<>();

    //个人页面登录Lin
    @ViewInject(R.id.fragment_mine_userLin)
    private LinearLayout mineUserLin;

    //个人页面昵称
    @ViewInject(R.id.fragment_mine_nickNameTv)
    private TextView mineUserNameTv;
    //个人页面头像
    @ViewInject(R.id.fragment_mine_userIconImg)
    private MyImageView userIconImg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        initView();
        initData();
        initViewOper();
//        if (MyApplication.user != null) {
//            flush();
//        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (MyApplication.user != null) {
            flush();
        } else {
            reFlush();
        }
        HomeFragment.listViewAdapter.notifyDataSetChanged();
        HomeFragment.listView.setAdapter(HomeFragment.listViewAdapter);
    }

    @Override
    public void initView() {
        ViewUtils.inject(act);
    }

    @Override
    public void initData() {
        butImgs = new ImageView[]{homeImg, clImg, messageImg, mineImg};
        butOnResIds = new int[]{R.drawable.home_sel, R.drawable.chaer_sel, R.drawable.xiaoxi_sel, R.drawable.wo_sel};
        butOffResIds = new int[]{R.drawable.home_nor, R.drawable.chaer_nor, R.drawable.xiaoxi_nor, R.drawable.wo_nor};
        linResIds = new int[]{R.id.act_home_homeLin, R.id.act_home_clLin, R.id.act_home_messageLin, R.id.act_home_mineLin};
        for (int i = 0; i < 4; i++) {
            linearLayouts[i] = (LinearLayout) act.findViewById(linResIds[i]);
            linearLayouts[i].setOnClickListener(this);
        }
        fragments.add(new HomeFragment());
        fragments.add(new ClFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MineFragment());
        adapter = new HomeActViewPagerAdapter(getSupportFragmentManager(), fragments);
    }

    @Override
    public void initViewOper() {
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(4);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateButtonImg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        if (MyApplication.user != null) {
//            flush();
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_home_homeLin: {
                updateButtonImg(0);
                vp.setCurrentItem(0);
                break;
            }
            case R.id.act_home_clLin: {
                updateButtonImg(1);
                vp.setCurrentItem(1);
                break;
            }
            case R.id.act_home_messageLin: {
                updateButtonImg(2);
                vp.setCurrentItem(2);
                break;
            }
            case R.id.act_home_mineLin: {
                updateButtonImg(3);
                vp.setCurrentItem(3);
                break;
            }
        }
    }

    //动态更改按钮图片
    public void updateButtonImg(int index) {
        for (int i = 0; i < 4; i++) {
            if (i == index) {
                butImgs[i].setImageResource(butOnResIds[i]);
            } else {
                butImgs[i].setImageResource(butOffResIds[i]);
            }
        }
    }

    //点击发布
    @OnClick(R.id.act_home_publishImg)
    public void onPublishClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆");
            startAct(LoginActivity.class);
            return;
        }
        startAct(PublishActivity.class);
    }

    //点击返回键
    private boolean backFlag = false;
    CountDownTimer timer;

    @Override
    public void onBackPressed() {
        if (backFlag) {
            timer.cancel();
            System.exit(0);
        }
        toastS("再次点击退出~");
        backFlag = true;
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                backFlag = false;
            }
        };
        timer.start();
    }

    //我的页面个人信息Lin
    @OnClick(R.id.fragment_mine_userLin)
    public void onUserClick(View v) {
        startAct(LoginActivity.class);
    }

    //个人界面设置
    @OnClick(R.id.fragment_mine_settingLin)
    public void onSettingClick(View v) {
        if (MyApplication.user != null) {
            startAct(SettingActivity.class);
        } else {
            toastS("请您先登录");
        }
    }

    //钱包
    @OnClick(R.id.fragment_mine_walletLin)
    public void onWalletClick(View v) {
        if (MyApplication.user != null) {
            startAct(WalletActivity.class);
        } else {
            toastS("请您先登录");
        }
    }


    //刷新个人信息
    public void flush() {
        //使Lin不可点击
        mineUserLin.setClickable(false);
        if (MyApplication.user.getNickName() == null) {
            mineUserNameTv.setText("未设置昵称");
        } else {
            mineUserNameTv.setText(MyApplication.user.getNickName());
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
                            //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
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

    //恢复初始视图
    public void reFlush() {
        mineUserLin.setClickable(true);
        userIconImg.setImageResource(R.drawable.touxiang);
        mineUserNameTv.setText("点击登录");
    }
}
