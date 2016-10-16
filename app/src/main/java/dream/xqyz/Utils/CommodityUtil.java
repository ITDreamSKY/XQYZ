package dream.xqyz.Utils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import dream.xqyz.Application.MyApplication;
import dream.xqyz.Bean.Commodity;

/**
 * Created by SKYMAC on 16/9/4.
 */
public class CommodityUtil {
    /**
     * type 为0 查询所有 data null
     * type 为1 查询赞过的 data null
     * type 为2 查询赞过的加载更多 data 忽略条数
     * type 为3 查询我发布的 data null
     * type 为4 查询我发布的加载更多 data 忽略条数
     * type 为5 查询我购买的 data null
     */

    public static void findCommodity(int limit, int type, Object data, final FindListener<Commodity> listener) {
        BmobQuery<Commodity> query = new BmobQuery<>();
        query.setLimit(limit);
        //添加查询条件
        switch (type) {
            case 0: {
                query.order("-createdAt");
                query.findObjects(listener);
            }
            break;
            case 1: {
                query.order("-createdAt");
                query.addWhereContains("goodUserObjectIds", MyApplication.user.getObjectId());
                query.findObjects(listener);
            }
            break;
            case 2: {
                query.order("-createdAt");
                query.addWhereContains("goodUserObjectIds", MyApplication.user.getObjectId());
                query.setSkip((Integer) data);
                query.findObjects(listener);
            }
            break;
            case 3: {
                query.order("-createdAt");
                query.addWhereContains("user", MyApplication.user.getObjectId());
                query.findObjects(listener);
            }
            break;
            case 4: {
                query.order("-createdAt");
                query.addWhereContains("user", MyApplication.user.getObjectId());
                query.setSkip((Integer) data);
                query.findObjects(listener);
            }
            break;
        }
    }
}