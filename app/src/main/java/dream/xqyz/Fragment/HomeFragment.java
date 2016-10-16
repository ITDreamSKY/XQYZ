package dream.xqyz.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.xqyz.Adapter.HomeFragmentListViewAdapter;
import dream.xqyz.Adapter.HomeFragmentViewPagerAdapter;
import dream.xqyz.Base.BaseFragment;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.R;
import dream.xqyz.Utils.CommodityUtil;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class HomeFragment extends BaseFragment implements BaseInterface {

    //广告栏
    private ViewPager vp;
    //广告数据源
    private int[] vpImgs;
    //广告adapter
    private HomeFragmentViewPagerAdapter adapter;
    //存放豆豆
    private List<ImageView> dImgViews;
    //timer
    private CountDownTimer timer;
    //用来加载动态的listview
    public static ListView listView;
    //数据源
    private List<Commodity> commodityList;
    //填充listview的adapter
    public static HomeFragmentListViewAdapter listViewAdapter;
    //刷新
    private ImageView flushImg;

    @Override
    protected void init() {
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.fragment_home_listview);
        vp = (ViewPager) view.findViewById(R.id.fragment_home_vp);
        vpImgs = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};
        dImgViews = new ArrayList<>();
        dImgViews.add(((ImageView) view.findViewById(R.id.fragment_home_aImg)));
        dImgViews.add(((ImageView) view.findViewById(R.id.fragment_home_bImg)));
        dImgViews.add(((ImageView) view.findViewById(R.id.fragment_home_cImg)));
        dImgViews.add(((ImageView) view.findViewById(R.id.fragment_home_dImg)));
        adapter = new HomeFragmentViewPagerAdapter(vpImgs, act);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(4);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                flushD(vp.getCurrentItem());
            }
        });
        if (timer == null) {
            timer = new CountDownTimer(10000000, 5000) {
                @Override
                public void onTick(long l) {
                    if (vp.getCurrentItem() == 0) {
                        vp.setCurrentItem(1);
                    } else if (vp.getCurrentItem() == 1) {
                        vp.setCurrentItem(2);
                    } else if (vp.getCurrentItem() == 2) {
                        vp.setCurrentItem(3);
                    } else if (vp.getCurrentItem() == 3) {
                        vp.setCurrentItem(0);
                    }
                }

                @Override
                public void onFinish() {

                }
            };
        }
        timer.start();
        CommodityUtil.findCommodity(20, 0, null, new FindListener<Commodity>() {
            @Override
            public void done(List<Commodity> list, BmobException e) {
                if (e == null) {
                    commodityList = list;

                    listViewAdapter = new HomeFragmentListViewAdapter(commodityList, act);
                    listView.setAdapter(listViewAdapter);

                    int totalHeight = 0;
                    for (int i = 0, len = listViewAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
                        View listItem = listViewAdapter.getView(i, null, listView);
                        listItem.measure(0, 0); // 计算子项View 的宽高
                        totalHeight += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
                    }
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = totalHeight
                            + (listView.getDividerHeight() * (listView.getCount() - 1) + 200);
                    // listView.getDividerHeight()获取子项间分隔符占用的高度
                    // params.height最后得到整个ListView完整显示需要的高度
                    listView.setLayoutParams(params);

                } else {
                    toastL(e.toString());
                }
            }
        });

        flushImg = (ImageView) view.findViewById(R.id.fragment_home_flushImg);
        flushImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommodityUtil.findCommodity(20, 0, null, new FindListener<Commodity>() {
                    @Override
                    public void done(List<Commodity> list, BmobException e) {
                        if (e == null) {
                            commodityList = list;

                            listViewAdapter = new HomeFragmentListViewAdapter(commodityList, act);
                            listView.setAdapter(listViewAdapter);

                            int totalHeight = 0;
                            for (int i = 0, len = listViewAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
                                View listItem = listViewAdapter.getView(i, null, listView);
                                listItem.measure(0, 0); // 计算子项View 的宽高
                                totalHeight += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
                            }
                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                            params.height = totalHeight
                                    + (listView.getDividerHeight() * (listView.getCount() - 1) + 200);
                            // listView.getDividerHeight()获取子项间分隔符占用的高度
                            // params.height最后得到整个ListView完整显示需要的高度
                            listView.setLayoutParams(params);

                        } else {
                            toastL(e.toString());
                        }
                    }
                });

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

    //刷新豆豆的方法
    private void flushD(int position) {
        for (int i = 0; i < 4; i++) {
            if (i == position) {
                dImgViews.get(i).setImageResource(R.drawable.dian_sel);
            } else {
                dImgViews.get(i).setImageResource(R.drawable.dian_nor);
            }
        }
    }
}
