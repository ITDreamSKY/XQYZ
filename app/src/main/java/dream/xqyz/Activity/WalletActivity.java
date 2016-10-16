package dream.xqyz.Activity;

import android.os.Bundle;
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
 * Created by SKYMAC on 16/9/4.
 */
public class WalletActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_wallet_nickNameTv)
    private TextView nickName;
    @ViewInject(R.id.act_wallet_yueTv)
    private TextView moneyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wallet);
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        flush();
    }

    @Override
    public void initView() {
        ViewUtils.inject(act);
    }

    @Override
    public void initData() {
        String nickNameStr = MyApplication.user.getNickName();
        nickName.setText(nickNameStr);
    }

    @Override
    public void initViewOper() {
        flush();
    }


    @OnClick(R.id.act_wallet_backImg)
    public void onBackClick(View v){
        finish();
    }

    @OnClick(R.id.act_wallet_chongzhiLin)
    public void onChongZhiClick(View v){
        startAct(PayActivity.class);
    }

    private void flush(){
        String money = MyApplication.user.getMoney().toString();
        moneyTv.setText(money);
    }
}
