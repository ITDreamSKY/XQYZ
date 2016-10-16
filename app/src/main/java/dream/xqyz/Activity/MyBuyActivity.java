package dream.xqyz.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.xqyz.Adapter.MyBuyXListViewAdapter;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Order;
import dream.xqyz.R;
import dream.xqyz.Utils.OrderUtil;
import dream.xqyz.View.XListView;

/**
 * Created by SKYMAC on 16/9/21.
 */
public class MyBuyActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_mybuy_xlistview)
    private XListView xListView;
    //数据源
    private List<Order> orderList;
    //适配器
    private MyBuyXListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mybuy);
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
        xListView.setAdapter(adapter);
    }

    @Override
    public void initView() {
        ViewUtils.inject(act);
    }

    @Override
    public void initData() {
        orderList = new ArrayList<>();
        OrderUtil.findOrder(5, 1, null, new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    orderList = list;
                    if (orderList.size() > 0) {
                        adapter = new MyBuyXListViewAdapter(act, orderList);
                        xListView.setAdapter(adapter);

                        xListView.setEnabled(true);
                        xListView.setPullLoadEnable(true);
                        xListView.setSaveEnabled(true);
                        xListView.setPullRefreshEnable(true);
                        xListView.setXListViewListener(new XListView.IXListViewListener() {
                            @Override
                            public void onRefresh() {
                                OrderUtil.findOrder(orderList.size(), 1, null, new FindListener<Order>() {
                                    @Override
                                    public void done(List<Order> list, BmobException e) {
                                        if(e==null){
                                            orderList = list;
                                            adapter.notifyDataSetChanged();
                                            xListView.setAdapter(adapter);
                                            xListView.stopRefresh();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onLoadMore() {
                                OrderUtil.findOrder(5, 2, orderList.size(), new FindListener<Order>() {
                                    @Override
                                    public void done(List<Order> list, BmobException e) {
                                        if (e == null) {
                                            list.addAll(0, orderList);
                                            orderList = list;
                                            adapter.notifyDataSetChanged();
                                            xListView.setAdapter(adapter);
                                            xListView.stopLoadMore();
                                        }
                                    }
                                });
                            }
                        });
                    }
                } else {
                    toastL(e.toString());
                }
            }
        });
    }

    @Override
    public void initViewOper() {

    }

    //点击返回
    @OnClick(R.id.act_mybuy_backLin)
    private void onBackClick(View v) {
        finish();
    }
}
