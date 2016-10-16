package dream.xqyz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.xqyz.Activity.LogisticsActivity;
import dream.xqyz.Activity.PayActivity;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.Bean.Order;
import dream.xqyz.Bean.User;
import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/21.
 */
public class MyBuyXListViewAdapter extends BaseAdapter {

    private List<Order> orderList;
    private LayoutInflater inflater;
    private Context context;

    public MyBuyXListViewAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_mybuy, null);
            vh.img = (ImageView) view.findViewById(R.id.item_mybuy_img);
            vh.commodityNameTv = (TextView) view.findViewById(R.id.item_mybuy_nameTv);
            vh.commodityPriceTv = (TextView) view.findViewById(R.id.item_mybuy_priceTv);
            vh.orderStateTv = (TextView) view.findViewById(R.id.item_mybuy_stateTv);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        final Order order = orderList.get(position);

        final String commodityId = order.getCommodityId();
        BmobQuery<Commodity> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", commodityId);
        final ViewHolder finalVh = vh;
        query.findObjects(new FindListener<Commodity>() {
            @Override
            public void done(List<Commodity> list, BmobException e) {
                if (e == null) {
                    Commodity commodity = list.get(0);
                    finalVh.commodityNameTv.setText(commodity.getName());
                    finalVh.commodityPriceTv.setText(commodity.getPrice().toString());
                    final String path = MyApplication.file + "/" + commodityId + ".jpeg";
                    final File file = new File(path);
                    commodity.getImgs().get(0).download(file, new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(path);
                                finalVh.img.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            }
        });
        //从这往下是各种。。。
        if (order.getCancel()) {
            vh.orderStateTv.setText("订单已被取消");
        } else if (order.getReceiver()) {
            //已收货
            vh.orderStateTv.setText("订单已完成");
        } else if (order.getSendNum() != null) {
            //卖家以发货
            vh.orderStateTv.setText("卖家已发货");
            LinearLayout lin = (LinearLayout) view.findViewById(R.id.item_mybuy_bottomLin2);
            lin.setVisibility(View.VISIBLE);
            TextView chaxunTV = (TextView) view.findViewById(R.id.item_mybuy_chaxunTv);
            chaxunTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转查询
                    MyApplication.putData("sendNum", order.getSendNum());
                    context.startActivity(new Intent(context, LogisticsActivity.class));
                }
            });
            TextView receiveTv = (TextView) view.findViewById(R.id.item_mybuy_receiveTv);
            final ViewHolder finalVh2 = vh;
            receiveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order.setReceiver(true);
                    order.update(order.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            BmobQuery<User> q = new BmobQuery<User>();
                            q.addWhereEqualTo("objectId", order.getSaleUserId());
                            q.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(context, "确认收货成功", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });

        } else if (order.getPay()) {
            //已付款 并且卖家未发货，如果已发货则被上层拦截
            vh.orderStateTv.setText("已支付，等待卖家发货");

        } else {
            //未支付状态
            vh.orderStateTv.setText("未支付");

            final LinearLayout lin = (LinearLayout) view.findViewById(R.id.item_mybuy_bottomLin1);
            lin.setVisibility(View.VISIBLE);
            final TextView zhifuTv = (TextView) view.findViewById(R.id.item_mybuy_zhifuTv);
            final ViewHolder finalVh1 = vh;
            zhifuTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double price = Double.parseDouble(finalVh1.commodityPriceTv.getText().toString());
                    if (MyApplication.user.getMoney() < price) {
                        Toast.makeText(context, "您的余额不足，请先充值！", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, PayActivity.class));
                    } else {
                        MyApplication.user.setMoney(MyApplication.user.getMoney() - price);
                        MyApplication.user.update(MyApplication.user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //支付成功
                                    MyApplication.user = User.getCurrentUser(User.class);
                                    order.setPay(true);
                                    order.update(order.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(context, "支付成功！", Toast.LENGTH_LONG).show();
                                                lin.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                                } else {

                                }
                            }
                        });
                    }
                }
            });
            TextView quxiaoTv = (TextView) view.findViewById(R.id.item_mybuy_quxiaoTv);
            final ViewHolder finalVh3 = vh;
            quxiaoTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //取消订单
                    order.setCancel(true);
                    order.update(order.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {

                                        Commodity c = new Commodity();
                                        c.setSoldOut(false);
                                        c.update(commodityId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(context, "取消订单成功", Toast.LENGTH_LONG).show();
                                                    lin.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });

                                    }
                                }
                            }

                    );
                }
            });
        }

        return view;
    }

    class ViewHolder {
        private ImageView img;
        private TextView commodityNameTv, commodityPriceTv, orderStateTv;
    }
}
