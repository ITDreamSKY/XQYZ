package dream.xqyz.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import dream.xqyz.Base.BaseFragment;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class HomeActViewPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public HomeActViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
