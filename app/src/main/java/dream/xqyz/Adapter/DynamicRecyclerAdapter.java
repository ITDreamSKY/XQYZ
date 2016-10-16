package dream.xqyz.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import dream.xqyz.R;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class DynamicRecyclerAdapter extends RecyclerView.Adapter<DynamicRecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Bitmap> bitmaps;

    public DynamicRecyclerAdapter(Context context, List<Bitmap> bitmaps) {
        inflater = LayoutInflater.from(context);
        this.bitmaps = bitmaps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_recycler, null);
        ViewHolder vh = new ViewHolder(view);
        vh.img = (ImageView) view.findViewById(R.id.item_recycler_img);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.img.setImageBitmap(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        ImageView img;
    }
}
