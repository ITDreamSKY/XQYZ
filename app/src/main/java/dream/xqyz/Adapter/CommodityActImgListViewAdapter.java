package dream.xqyz.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/20.
 */
public class CommodityActImgListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Bitmap> bitmaps;

    public CommodityActImgListViewAdapter(Context context, List<Bitmap> bitmaps) {
        this.inflater = LayoutInflater.from(context);
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int i) {
        return bitmaps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_xiangqing_imglistview, null);
            vh.img = (ImageView) view.findViewById(R.id.item_xiangqing_imglistview_img);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.img.setImageBitmap(bitmaps.get(position));
        return view;
    }

    class ViewHolder {
        ImageView img;
    }
}
