package dream.xqyz.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Address;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.Bean.Order;
import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/21.
 */
public class SetOrderActivity extends BaseActivity implements BaseInterface {


    //要购买的商品
    private Commodity commodity;
    //商品名
    @ViewInject(R.id.act_setorder_commodityNameTv)
    private TextView cNameTv;
    //商品价格
    @ViewInject(R.id.act_setorder_paymoneyTv)
    private TextView cPriceTv;
    //卖家地址
    @ViewInject(R.id.act_setorder_commodityAddressTv)
    private TextView cAddressTv;
    //收货人姓名
    @ViewInject(R.id.act_setOrder_address_name)
    private TextView addressNameTv;
    //收货人电话
    @ViewInject(R.id.act_setOrder_address_phone)
    private TextView addressPhoneTv;
    //收货人地址
    @ViewInject(R.id.act_setOrder_address_address)
    private TextView addressAddressTv;
    //收货地址
    private Address address;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setorder);
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        address = (Address) MyApplication.getData("selectAddress", true);
        if (address != null) {
            addressNameTv.setText(address.getName());
            addressPhoneTv.setText(address.getPhone());
            addressAddressTv.setText(address.getAddress() + " " + address.getSite());
        }
    }

    @Override
    public void initView() {
        ViewUtils.inject(act);
    }

    @Override
    public void initData() {
        commodity = (Commodity) MyApplication.getData("buy", true);
    }

    @Override
    public void initViewOper() {
        cNameTv.setText(commodity.getName());
        cPriceTv.setText(commodity.getPrice().toString());
        cAddressTv.setText(commodity.getAddress());
    }

    //点击返回
    @OnClick(R.id.act_setorder_backLinearLayout)
    private void onBackClick(View v) {
        finish();
    }

    //点击地址跳转
    @OnClick(R.id.act_setOrder_addressLin)
    private void onAddressClick(View v) {
        startAct(SelectAddressActivity.class);
    }

    //点击提交订单
    @OnClick(R.id.act_setorder_submitTv)
    private void onBuyClick(View v) {
        if (address == null) {
            toastS("请选择收货地址");
            return;
        }
        Order order = new Order();
        order.setSaleUserId(commodity.getUser().getObjectId());
        order.setBuyUserId(MyApplication.user.getObjectId());
        order.setPay(false);
        order.setReceiver(false);
        order.setAddress(address);
        order.setCommodityId(commodity.getObjectId());
        order.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    commodity.setSoldOut(true);
                    commodity.update(commodity.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                toastS("下单成功");
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}
