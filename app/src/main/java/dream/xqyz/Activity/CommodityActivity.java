package dream.xqyz.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.xqyz.Adapter.CommodityActImgListViewAdapter;
import dream.xqyz.Adapter.CommodityActMessageListViewAdapter;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Base.BaseActivity;
import dream.xqyz.Base.BaseInterface;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.Bean.Message;
import dream.xqyz.Bean.User;
import dream.xqyz.R;
import dream.xqyz.View.MyImageView;

/**
 * Created by SKYMAC on 16/9/20.
 */
public class CommodityActivity extends BaseActivity implements BaseInterface {

    //商品信息
    private Commodity commodity;
    //卖家信息
    private User user;
    //商品图片
    @ViewInject(R.id.act_xiangqing_ImgListView)
    private ListView imgListView;
    //商品图片填充
    private CommodityActImgListViewAdapter imgListViewAdapter;
    //卖家头像
    @ViewInject(R.id.act_xiangqing_userImg)
    private MyImageView userMyImgView;
    //卖家昵称
    @ViewInject(R.id.act_xiangqing_nickNameTv)
    private TextView nickNameTv;
    //发布时间
    @ViewInject(R.id.act_xiangqing_timeTv)
    private TextView timeTv;
    //价格
    @ViewInject(R.id.act_xiangqing_moneyTv)
    private TextView priceTv;
    //内容
    @ViewInject(R.id.act_xiangqing_contentTv)
    private TextView contentTv;
    //点赞数
    @ViewInject(R.id.act_xiangqing_goodNumTv)
    private TextView goodNumTv;
    //留言数
    @ViewInject(R.id.act_xiangqing_messageNumTv)
    private TextView messageNumTv;
    //点赞Lin
    @ViewInject(R.id.act_xiangqing_goodLin)
    private LinearLayout goodLin;
    //点赞图标
    @ViewInject(R.id.act_xiangqing_goodImg)
    private ImageView goodImg;
    //下方Lin
    @ViewInject(R.id.act_xiangqing_bottomLin)
    private LinearLayout bottomLin;
    //发送消息Lin
    @ViewInject(R.id.act_xiangqing_sendMessageLin)
    private LinearLayout sendMessageLin;
    //发送消息的输入框
    @ViewInject(R.id.act_xiangqing_sendMessageLin_messageEt)
    private EditText sendMessageEt;
    //留言listview
    @ViewInject(R.id.act_xiangqing_messageListView)
    private ListView messageLv;
    //填充留言的adapter
    private CommodityActMessageListViewAdapter messageListViewAdapter;
    //购买按钮
    @ViewInject(R.id.act_xiangqing_buyTv)
    private TextView buyTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangqing_layout);
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
        commodity = (Commodity) MyApplication.getData("ClickCommodity", true);
        String uId = commodity.getUser().getObjectId();
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", uId);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                user = list.get(0);
                nickNameTv.setText(user.getNickName());

                //二级缓存头像
                final File file = new File(MyApplication.file, "/" + user.getObjectId() + "/" + user.getObjectId() + ".jpeg");
                Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bit == null) {
                    //如果用户设置了头像的话
                    if (user.getUserIcon() != null) {
                        user.getUserIcon().download(file.getAbsoluteFile(), new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    //bit设置不了final
                                    final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
                                    //第二步 设置图片
                                    userMyImgView.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                    }
                } else {
                    userMyImgView.setImageBitmap(bit);
                }
            }
        });
    }

    @Override
    public void initViewOper() {
        timeTv.setText(commodity.getCreatedAt());
        priceTv.setText(commodity.getPrice().toString());
        contentTv.setText(commodity.getContent());
        goodNumTv.setText(commodity.getGoodUserObjectIds().size() + "");
        messageNumTv.setText(commodity.getMessageList().size() + "");

        messageListViewAdapter = new CommodityActMessageListViewAdapter(act, commodity.getMessageList());
        messageLv.setAdapter(messageListViewAdapter);

        int totalHeight2 = 0;
        for (int j = 0, len = messageListViewAdapter.getCount(); j < len; j++) { // listAdapter.getCount()返回数据项的数目
            View listItem = messageListViewAdapter.getView(j, null, messageLv);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight2 += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params2 = messageLv.getLayoutParams();
        params2.height = totalHeight2
                + (messageLv.getDividerHeight() * (messageLv.getCount() - 1) + 200);
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        messageLv.setLayoutParams(params2);


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
                imgListViewAdapter = new CommodityActImgListViewAdapter(act, bitmaps);
                imgListView.setAdapter(imgListViewAdapter);

                int totalHeight = 0;
                for (int j = 0, len = imgListViewAdapter.getCount(); j < len; j++) { // listAdapter.getCount()返回数据项的数目
                    View listItem = imgListViewAdapter.getView(j, null, imgListView);
                    listItem.measure(0, 0); // 计算子项View 的宽高
                    totalHeight += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
                }
                ViewGroup.LayoutParams params = imgListView.getLayoutParams();
                params.height = totalHeight
                        + (imgListView.getDividerHeight() * (imgListView.getCount() - 1) + 50);
                // listView.getDividerHeight()获取子项间分隔符占用的高度
                // params.height最后得到整个ListView完整显示需要的高度
                imgListView.setLayoutParams(params);
            }

        }

        if (MyApplication.user != null) {
            if (commodity.getGoodUserObjectIds().contains(MyApplication.user.getObjectId())) {
                goodImg.setImageResource(R.drawable.xiangqing_zan_sel);
            } else {
                goodImg.setImageResource(R.drawable.xiangqing_zan_nor);
            }

            goodLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!commodity.getGoodUserObjectIds().contains(MyApplication.user.getObjectId())) {
                        commodity.getGoodUserObjectIds().add(MyApplication.user.getObjectId());
                        commodity.update(commodity.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //刷新操作
                                    toastS("点赞成功");
                                    goodNumTv.setText(commodity.getGoodUserObjectIds().size() + "");
                                    goodImg.setImageResource(R.drawable.xiangqing_zan_sel);
                                } else {
                                    toastS("失败");
                                }
                            }
                        });
                    } else {
                        commodity.getGoodUserObjectIds().remove(MyApplication.user.getObjectId());
                        commodity.update(commodity.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //刷新操作
                                    toastS("取消点赞");
                                    goodNumTv.setText(commodity.getGoodUserObjectIds().size() + "");
                                    goodImg.setImageResource(R.drawable.xiangqing_zan_nor);
                                } else {
                                    toastS("失败");
                                }
                            }
                        });
                    }
                }
            });
            if (commodity.getUser().getObjectId().equals(MyApplication.user.getObjectId())) {
                buyTv.setText("我发布的");
                buyTv.setClickable(false);
            }
            if (commodity.getSoldOut()) {
                buyTv.setText("已售出");
                buyTv.setClickable(false);
            }
        } else {
            goodLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toastS("请您先登陆");
                }
            });
        }


    }

    //点击返回
    @OnClick(R.id.act_xiangqing_backImg)
    private void onBackClick(View v) {
        finish();
    }

    //点击消息
    @OnClick(R.id.act_xiangqing_messageLin)
    private void onMessageLinClick(View v) {
        if (MyApplication.user != null) {
            bottomLin.setVisibility(View.INVISIBLE);
            sendMessageLin.setVisibility(View.VISIBLE);
        } else {
            toastS("请您先登陆");
        }
    }


    //取消消息发送
    @OnClick(R.id.act_xiangqing_hiddenKeyBoard)
    private void onHiddenKeyBoardClick(View v) {
        sendMessageLin.setVisibility(View.INVISIBLE);
        bottomLin.setVisibility(View.VISIBLE);
    }

    //发送消息
    @OnClick(R.id.act_xiangqing_sendMessageLin_sendTv)
    private void onSendMessageClick(View v) {
        String messageStr = sendMessageEt.getText().toString();
        Message message = new Message();
        message.setUser(MyApplication.user);
        message.setContent(messageStr);
        //获取时间
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        String time = format.format(date);

        message.setTime(time);

        commodity.getMessageList().add(message);
        commodity.update(commodity.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastS("发送成功");
                    sendMessageEt.setText("");
                    sendMessageLin.setVisibility(View.INVISIBLE);
                    bottomLin.setVisibility(View.VISIBLE);
                    messageNumTv.setText(commodity.getMessageList().size() + "");
                    messageListViewAdapter.notifyDataSetChanged();
                    int totalHeight2 = 0;
                    for (int j = 0, len = messageListViewAdapter.getCount(); j < len; j++) { // listAdapter.getCount()返回数据项的数目
                        View listItem = messageListViewAdapter.getView(j, null, messageLv);
                        listItem.measure(0, 0); // 计算子项View 的宽高
                        totalHeight2 += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
                    }
                    ViewGroup.LayoutParams params2 = messageLv.getLayoutParams();
                    params2.height = totalHeight2
                            + (messageLv.getDividerHeight() * (messageLv.getCount() - 1) + 200);
                    // listView.getDividerHeight()获取子项间分隔符占用的高度
                    // params.height最后得到整个ListView完整显示需要的高度
                    messageLv.setLayoutParams(params2);


                } else {
                    toastS("发送失败");
                }
            }
        });
    }

    //下单
    @OnClick(R.id.act_xiangqing_buyTv)
    private void onBuyClick(View v) {
        if (MyApplication.user == null) {
            toastS("请您先登陆");
            return;
        }
        MyApplication.putData("buy", commodity);
        startAct(SetOrderActivity.class);
        finish();
    }
}
