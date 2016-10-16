package dream.xqyz.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Order;
import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/22.
 */
public class SendActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_send_Et)
    private EditText Et;
    private Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_send);
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
        order = (Order) MyApplication.getData("sendOrder", true);
    }

    @Override
    public void initViewOper() {

    }

    //点击返回
    @OnClick(R.id.act_send_backLin)
    private void onBackClick(View v) {
        finish();
    }

    //点击提交
    @OnClick(R.id.act_send_submitTv)
    private void onSubmitClick(View v) {
        String str = Et.getText().toString();
        if (str == null) {
            toastS("请输入单号");
            return;
        }
        Order o = new Order();
        o.setSendNum(str);
        o.update(order.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastS("提交成功");
                    finish();
                }
            }
        });

    }
}
