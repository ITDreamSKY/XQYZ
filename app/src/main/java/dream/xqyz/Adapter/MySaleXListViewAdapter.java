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
import dream.xqyz.Activity.SendActivity;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Bean.Commodity;
import dream.xqyz.Bean.Order;
import dream.xqyz.Bean.User;
import dream.xqyz.R;

/**
 * Created by SKYMAC on 16/9/21.
 */
public class MySaleXListViewAdapter extends BaseAdapter {

    private List<Order> orderList;
    private LayoutInflater inflater;
    private Context context;

    public MySaleXListViewAdapter(Context context, List<Order> orderList) {
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
            view = inflater.inflate(R.layout.item_mysale, null);
            vh.img = (ImageView) view.findViewById(R.id.item_mysale_img);
            vh.commodityNameTv = (TextView) view.findViewById(R.id.item_mysale_nameTv);
            vh.commodityPriceTv = (TextView) view.findViewById(R.id.item_mysale_priceTv);
            vh.orderStateTv = (TextView) view.findViewById(R.id.item_mysale_stateTv);
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
        } else if (order.getSaleUserGetMoney()) {
            vh.orderStateTv.setText("订单完成，已收款");
        } else if (order.getReceiver()) {
            //已收货
            vh.orderStateTv.setText("买家已确认，请收款");
            final LinearLayout lin = (LinearLayout) view.findViewById(R.id.item_mysale_bottomLin3);
            lin.setVisibility(View.VISIBLE);
            TextView getTv = (TextView) view.findViewById(R.id.item_mysale_getMoneyTv);
            final ViewHolder finalVh1 = vh;
            getTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order.setSaleUserGetMoney(true);
                    order.update(order.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                MyApplication.user.setMoney(MyApplication.user.getMoney() + Double.parseDouble(finalVh1.commodityPriceTv.getText().toString()));
                                MyApplication.user.update(MyApplication.user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(context, "收款成功", Toast.LENGTH_LONG).show();
                                            lin.setVisibility(View.INVISIBLE);
                                            MyApplication.user = User.getCurrentUser(User.class);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else if (order.getSendNum() != null) {
            //卖家以发货
            vh.orderStateTv.setText("等待买家确认");

        } else if (order.getPay()) {
            //买家已付款 准备发货
            vh.orderStateTv.setText("买家已付款，请发货");
            LinearLayout lin = (LinearLayout) view.findViewById(R.id.item_mysale_bottomLin2);
            lin.setVisibility(View.VISIBLE);
            final TextView sendTv = (TextView) view.findViewById(R.id.item_mysale_sendTv);
            sendTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转发货Activity
                    MyApplication.putData("sendOrder", order);
                    context.startActivity(new Intent(context, SendActivity.class));
//                    sendTv.setClickable(false);
                }
            });

        } else {
            //等待买家付款
            vh.orderStateTv.setText("等待买家付款");
            final LinearLayout lin = (LinearLayout) view.findViewById(R.id.item_mysale_bottomLin1);
            lin.setVisibility(View.VISIBLE);
            final TextView quxiaoTv = (TextView) view.findViewById(R.id.item_mysale_quxiaoTv);
            quxiaoTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                            Toast.makeText(context, "订单已取消，商品已经重新上架", Toast.LENGTH_LONG).show();
                                            lin.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }
                        }
                    });
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
