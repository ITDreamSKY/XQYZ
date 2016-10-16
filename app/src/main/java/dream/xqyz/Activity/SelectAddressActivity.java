package dream.xqyz.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import dream.xqyz.Adapter.AddressListViewAdapter;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Address;
import dream.xqyz.R;


/**
 * Created by SKYMAC on 16/9/7.
 */
public class SelectAddressActivity extends BaseActivity implements BaseInterface {

    public static SelectAddressActivity activity;

    @ViewInject(R.id.act_address_listview)
    private ListView listView;

    private List<Address> addressBeanList;
    public static AddressListViewAdapter addressListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_address);
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (MyApplication.user.getAddressList() != null) {
            addressBeanList = MyApplication.user.getAddressList();
            addressListViewAdapter = new AddressListViewAdapter(addressBeanList, act, true);
            listView.setAdapter(addressListViewAdapter);
        }
    }


    @Override
    public void initView() {
        ViewUtils.inject(act);
        activity = this;
    }

    @Override
    public void initData() {
        if (MyApplication.user.getAddressList() != null) {
            addressBeanList = MyApplication.user.getAddressList();
        } else {
            addressBeanList = new ArrayList<>();
        }
        addressListViewAdapter = new AddressListViewAdapter(addressBeanList, act, true);
    }

    @Override
    public void initViewOper() {
        listView.setAdapter(addressListViewAdapter);
    }

    //添加收货地址
    @OnClick(R.id.act_address_addLin)
    public void onAddClick(View v) {
        startAct(AddressAddActivity.class);
    }

    //点击返回
    @OnClick(R.id.act_address_backLinearLayout)
    public void onBackClick(View v){
        finish();
    }
}
