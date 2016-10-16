package dream.xqyz.Utils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Bean.Order;

/**
 * Created by SKYMAC on 16/9/21.
 * <p>
 * type 1   我购买的  data null
 * tyoe 2   我购买的加载更多  data 忽略条目
 * type 3   我卖出的  data null
 * type 4   我卖出的加载更多  data 忽略条目
 */
public class OrderUtil {
    public static void findOrder(int limit, int type, Object data, final FindListener<Order> listener) {
        BmobQuery<Order> query = new BmobQuery<>();
        query.setLimit(limit);
        //添加查询条件
        switch (type) {
            case 1: {
                query.order("-createdAt");
                query.addWhereEqualTo("buyUserId", MyApplication.user.getObjectId());
                query.findObjects(listener);
                break;
            }
            case 2: {
                query.order("-createdAt");
                query.addWhereEqualTo("buyUserId", MyApplication.user.getObjectId());
                query.setSkip((Integer) data);
                query.findObjects(listener);
                break;
            }
            case 3: {
                query.order("-createdAt");
                query.addWhereEqualTo("saleUserId", MyApplication.user.getObjectId());
                query.findObjects(listener);
                break;
            }
            case 4: {
                query.order("-createdAt");
                query.addWhereEqualTo("saleUserId", MyApplication.user.getObjectId());
                query.setSkip((Integer) data);
                query.findObjects(listener);
                break;
            }
        }
    }
}
