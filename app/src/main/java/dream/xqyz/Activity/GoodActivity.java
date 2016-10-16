package dream.xqyz.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.xqyz.Adapter.HomeFragmentListViewAdapter;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.R;
import dream.xqyz.Utils.CommodityUtil;
import dream.xqyz.View.XListView;

/**
 * Created by SKYMAC on 16/9/21.
 */
public class GoodActivity extends BaseActivity implements BaseInterface {

    //xlistview
    @ViewInject(R.id.act_good_xlistview)
    private XListView xListView;
    //adapter
    private HomeFragmentListViewAdapter adapter;
    //数据源
    private List<Commodity> commodityList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_good);
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
        CommodityUtil.findCommodity(5, 1, null, new FindListener<Commodity>() {
            @Override
            public void done(List<Commodity> list, BmobException e) {
                if (e == null) {
                    commodityList = list;
                    adapter = new HomeFragmentListViewAdapter(commodityList, act);
                    xListView.setAdapter(adapter);
                }
            }
        });

        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                xListView.setAdapter(adapter);
                xListView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                CommodityUtil.findCommodity(5, 2, commodityList.size(), new FindListener<Commodity>() {
                    @Override
                    public void done(List<Commodity> list, BmobException e) {
                        list.addAll(0, commodityList);
                        commodityList = list;
                        adapter.notifyDataSetChanged();
                        xListView.setAdapter(adapter);
                        xListView.stopLoadMore();
                    }
                });
            }
        });
    }

    @Override
    public void initViewOper() {

    }

    @OnClick(R.id.act_good_backLin)
    private void onBackClick(View v) {
        finish();
    }
}
