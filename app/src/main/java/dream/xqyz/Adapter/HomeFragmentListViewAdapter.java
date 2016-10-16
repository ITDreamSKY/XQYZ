package dream.xqyz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import dream.xqyz.Activity.CommodityActivity;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.Bean.User;
import dream.xqyz.R;
import dream.xqyz.View.MyImageView;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class HomeFragmentListViewAdapter extends BaseAdapter {

    private List<Commodity> commodityList;
    private Context context;
    private LayoutInflater inflater;
    private User user;

    public HomeFragmentListViewAdapter(List<Commodity> commodityList, Context context) {
        this.commodityList = commodityList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commodityList.size();
    }

    @Override
    public Object getItem(int i) {
        return commodityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_dynamic, null);
            vh.userIconMyImgView = (MyImageView) view.findViewById(R.id.item_dynamic_userIconMyImgView);
            vh.nickNameTv = (TextView) view.findViewById(R.id.item_dynamic_nickNameTv);
            vh.timeTv = (TextView) view.findViewById(R.id.item_dynamic_timeTv);
            vh.moneyTv = (TextView) view.findViewById(R.id.item_dynamic_moneyTv);
            vh.contentTv = (TextView) view.findViewById(R.id.item_dynamic_contentTv);
            vh.addressTv = (TextView) view.findViewById(R.id.item_dynamic_addressTv);
            vh.goodTv = (TextView) view.findViewById(R.id.item_dynamic_goodTv);
            vh.messageTv = (TextView) view.findViewById(R.id.item_dynamic_messageTv);
            vh.recyclerView = (RecyclerView) view.findViewById(R.id.item_dynamic_recyclerView);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        final Commodity commodity = commodityList.get(position);


        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", commodity.getUser().getObjectId());
        final ViewHolder finalVh = vh;
        final ViewHolder finalVh1 = vh;
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    user = object.get(0);

                    if (user.getUserIcon() != null) {
                        final File file = new File(MyApplication.file, "/" + user.getObjectId() + ".jpeg");
                        Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
                        if (bit == null) {

                            user.getUserIcon().download(file.getAbsoluteFile(), new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        //bit设置不了final
                                        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
                                        //第二步 设置图片
                                        finalVh.userIconMyImgView.setImageBitmap(bitmap);
                                        //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                                    }
                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                        } else {
                            finalVh1.userIconMyImgView.setImageBitmap(bit);
                        }
                    }
                    if (user.getNickName() != null) {
                        finalVh1.nickNameTv.setText(user.getNickName());
                    } else {
                        finalVh1.nickNameTv.setText("用户" + user.getObjectId());
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });

        DynamicRecyclerAdapter adapter;
        List<BmobFile> imgs = commodity.getImgs();
        final List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            BmobFile bFile = imgs.get(i);

            final File file = new File(MyApplication.file, "/" + commodity.getObjectId() + "/" + bFile.getFilename() + ".jpeg");
            Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bit == null) {

                bFile.download(file.getAbsoluteFile(), new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            bitmaps.add(bitmap);
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            } else {
                bitmaps.add(bit);
            }
            if (i == imgs.size() - 1) {
                //加载完毕
                adapter = new DynamicRecyclerAdapter(context, bitmaps);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                vh.recyclerView.setLayoutManager(linearLayoutManager);
                vh.recyclerView.setAdapter(adapter);
            }

        }
        // commodity.getObjectId /


        vh.timeTv.setText(commodity.getCreatedAt());
        vh.moneyTv.setText(commodity.getPrice().toString());
        vh.contentTv.setText(commodity.getContent().toString());
        vh.addressTv.setText(commodity.getAddress().toString());
        vh.goodTv.setText(commodity.getGoodUserObjectIds().size() + "");
        vh.messageTv.setText(commodity.getMessageList().size() + "");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("test", commodity.toString());
                MyApplication.putData("ClickCommodity", commodity);
                context.startActivity(new Intent(context, CommodityActivity.class));
            }
        });

        return view;
    }

    class ViewHolder {
        MyImageView userIconMyImgView;
        TextView nickNameTv, timeTv, moneyTv, contentTv, addressTv, goodTv, messageTv;
        RecyclerView recyclerView;
    }
}
