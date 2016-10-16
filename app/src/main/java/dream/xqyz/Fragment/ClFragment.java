package dream.xqyz.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

import dream.xqyz.Base.BaseFragment;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.R;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class ClFragment extends BaseFragment implements BaseInterface {
    @Override
    protected void init() {
        initView();
        initData();
        initViewOper();
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.chalou_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}
