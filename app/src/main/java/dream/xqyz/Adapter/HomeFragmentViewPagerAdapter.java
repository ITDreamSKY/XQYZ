package dream.xqyz.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dream.xqyz.R;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class HomeFragmentViewPagerAdapter extends PagerAdapter {

    private int[] imgs;
    private Context context;
    private LayoutInflater inflater;

    public HomeFragmentViewPagerAdapter(int[] imgs, Context context) {
        this.imgs = imgs;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_homefragment_vp,null);
        ImageView img = (ImageView) view.findViewById(R.id.item_homefragment_vp_img);
        img.setImageResource(imgs[position]);
        container.addView(view);
        return view;
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
