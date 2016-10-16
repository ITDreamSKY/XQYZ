package dream.xqyz.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Bean.Message;
import dream.xqyz.Bean.User;
import dream.xqyz.R;
import dream.xqyz.View.MyImageView;

/**
 * Created by SKYMAC on 16/9/21.
 */
public class CommodityActMessageListViewAdapter extends BaseAdapter {

    private List<Message> messageList;
    private LayoutInflater inflater;

    public CommodityActMessageListViewAdapter(Context context, List<Message> messageList) {
        this.inflater = LayoutInflater.from(context);
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_message, null);
            vh.userImg = (MyImageView) view.findViewById(R.id.item_message_userImg);
            vh.nickNameTv = (TextView) view.findViewById(R.id.item_message_nickNameTv);
            vh.contentTv = (TextView) view.findViewById(R.id.item_message_contentTv);
            vh.timeTv = (TextView) view.findViewById(R.id.item_message_timeTv);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        Message message = messageList.get(position);

        String uId = message.getUser().getObjectId();

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", uId);
        final ViewHolder finalVh = vh;
        final ViewHolder finalVh1 = vh;
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    User user = object.get(0);

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
                                        finalVh.userImg.setImageBitmap(bitmap);
                                        //第三部 缓存到内存中
//                    cache.put(str32, bitmap);
                                    }
                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                        } else {
                            finalVh1.userImg.setImageBitmap(bit);
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

        vh.contentTv.setText(message.getContent());
        vh.timeTv.setText(message.getTime());
        return view;
    }

    class ViewHolder {
        MyImageView userImg;
        TextView nickNameTv, contentTv, timeTv;
    }
}
