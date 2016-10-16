package dream.xqyz.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/22.
 */
public class LogisticsActivity extends BaseActivity implements BaseInterface {

    //物流单号
    @ViewInject(R.id.act_logistics_sendNumTv)
    private TextView sendNumTv;
    //物流信息
    @ViewInject(R.id.act_logistics_contentTv)
    private TextView ContentTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_logistics);
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
        String str = (String) MyApplication.getData("sendNum", true);
        sendNumTv.setText(str);
    }

    @Override
    public void initViewOper() {
        //物流信息还没做
    }

    //点击返回
    @OnClick(R.id.act_logistics_backLin)
    private void onBackClick(View v) {
        finish();
    }
}
