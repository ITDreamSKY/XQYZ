package dream.xqyz.Activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Address;
import dream.xqyz.Bean.User;
import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/7.
 */
public class AddressAddActivity extends BaseActivity implements BaseInterface {
//
//
//    @ViewInject(R.id.act_add_address_viewpager)
//    private ViewPager vp;

//    private AddressViewPagerAdapter addressViewPagerAdapter;

//    private List<BaseFragment> fragments;

    //用户的收货地址列表
    private List<Address> addressBeanList;

    //姓名
    @ViewInject(R.id.act_add_address_nameEt)
    private EditText nameEt;
    private String name;
    //电话
    @ViewInject(R.id.act_add_address_phoneEt)
    private EditText phoneEt;
    private String phone;
    //地址
    @ViewInject(R.id.act_add_address_siteTv)
    private TextView siteTv;
    //描述
    @ViewInject(R.id.act_add_address_siteEt)
    private EditText siteEt;
    private String address;
    private String site;


    //百度地图获取来的信息
    private PoiInfo poiInfo;


    //标记 true为修改 false为添加
    private boolean flag = false;

    //Lin
    @ViewInject(R.id.act_add_address_LinTextView)
    private TextView textView;

    //提交
    @ViewInject(R.id.act_add_address_submitImg)
    private ImageView subImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_address);
        initView();
        initData();
        initViewOper();
    }

    //从定位跳转回来
    @Override
    protected void onRestart() {
        super.onRestart();
        poiInfo = (PoiInfo) MyApplication.getData("poiInfo", true);
        if (poiInfo != null) {
            address = poiInfo.address;
            siteTv.setText(address);
        }
    }


    @Override
    public void initView() {
        ViewUtils.inject(this);
//        fragments = new ArrayList<>();
//        fragments.add(new AddressFragment());
//        addressViewPagerAdapter = new AddressViewPagerAdapter(getSupportFragmentManager(), fragments);
//        vp.setAdapter(addressViewPagerAdapter);
    }

    @Override
    public void initData() {
        if (MyApplication.user.getAddressList() != null) {
            addressBeanList = MyApplication.user.getAddressList();
        } else {
            addressBeanList = new ArrayList<>();
        }
    }

    @Override
    public void initViewOper() {
        //使TextView可滑动
        siteTv.setMovementMethod(ScrollingMovementMethod.getInstance());

        //修改 修改 修改 修改 修改 修改 修改 修改 修改
        final Address addressBean = (Address) MyApplication.getData("EditAddress", true);
        if (addressBean != null) {
            //证明是修改操作
            nameEt.setText(addressBean.getName());
            phoneEt.setText(addressBean.getPhone());
            siteTv.setText(addressBean.getAddress());
            siteEt.setText(addressBean.getSite());

            textView.setText("修改收货地址");

            subImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //修改操作
                    name = nameEt.getText().toString().trim();
                    if (name.equals("")) {
                        toastS("请输入收货人姓名");
                        return;
                    }
                    phone = phoneEt.getText().toString().trim();
                    if (!phone.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
                        toastS("手机号格式有误，请重新输入。");
                        return;
                    }
                    address = addressBean.getAddress();
                    if (address == null) {
                        toastS("请选择您的收货地址。");
                        return;
                    }
                    site = siteEt.getText().toString();

                    Address addr = new Address();
                    addr.setName(name);
                    addr.setPhone(phone);
                    addr.setAddress(address);
                    addr.setSite(site);

                    for (int i = 0; i < addressBeanList.size(); i++) {
                        if (addressBeanList.get(i).equals(addressBean)) {
                            addressBeanList.get(i).setName(addr.getName());
                            addressBeanList.get(i).setPhone(addr.getPhone());
                            addressBeanList.get(i).setAddress(addr.getAddress());
                            addressBeanList.get(i).setSite(addr.getSite());
                        }
                    }

                    User u = new User();
                    u.setAddressList(addressBeanList);

                    u.update(MyApplication.user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toastL("修改数据成功!");
                                MyApplication.user = BmobUser.getCurrentUser(User.class);
//                                AddressActivity.addressListViewAdapter.notifyDataSetChanged();
                                finish();
                            }
                        }
                    });
                }
            });

//            MyApplication.user.getAddressBeanList().remove(addressBean);
        }
    }

    //点击返回
    @OnClick(R.id.act_add_address_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }

    //点击地图
    @OnClick(R.id.act_add_address_gpsLin)
    public void onGPSClick(View v) {
        startAct(MapActivity.class);
    }

    //点击提交
    @OnClick(R.id.act_add_address_submitImg)
    public void onSubmitClick(View v) {
        name = nameEt.getText().toString().trim();
        if (name.equals("")) {
            toastS("请输入收货人姓名");
            return;
        }
        phone = phoneEt.getText().toString().trim();
        if (!phone.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            toastS("手机号格式有误，请重新输入。");
            return;
        }
        if (address == null) {
            toastS("请选择您的收货地址。");
            return;
        }
        site = siteEt.getText().toString();

        Address addressBean = new Address();
        addressBean.setName(name);
        addressBean.setPhone(phone);
        addressBean.setAddress(address);
        addressBean.setSite(site);

        addressBeanList.add(addressBean);

        User u = new User();
        u.setAddressList(addressBeanList);

        u.update(MyApplication.user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastL("添加收货地址成功!");
                    MyApplication.user = BmobUser.getCurrentUser(User.class);
//                    AddressActivity.addressListViewAdapter.notifyDataSetChanged();
                    finish();
                }
            }
        });
    }
}
