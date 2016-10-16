package dream.xqyz.Bean;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class Commodity extends BmobObject {
    //发布人
    private User user;
    //商品名称
    private String name;
    //商品详细介绍
    private String content;
    //商品图片
    private List<BmobFile> imgs;
    //发布人位置
    private String address;
    //地址经纬 用来计算距离
    private LatLng location;
    //价格
    private Double price;
    //记录点赞用户的ObjectId
    private List<String> goodUserObjectIds;
    //记录留言
    private List<Message> messageList;
    //记录是否以售出
    private Boolean isSoldOut;

    public Boolean getSoldOut() {
        if (isSoldOut == null) {
            isSoldOut = false;
        }
        return isSoldOut;
    }

    public void setSoldOut(Boolean soldOut) {
        isSoldOut = soldOut;
    }

    public List<Message> getMessageList() {
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<String> getGoodUserObjectIds() {
        if (goodUserObjectIds == null) {
            goodUserObjectIds = new ArrayList<>();
        }
        return goodUserObjectIds;
    }

    public void setGoodUserObjectIds(List<String> goodUserObjectIds) {
        this.goodUserObjectIds = goodUserObjectIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<BmobFile> getImgs() {
        return imgs;
    }

    public void setImgs(List<BmobFile> imgs) {
        this.imgs = imgs;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Double getPrice() {
        if (price == null) {
            price = 0.0;
        }
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "user=" + user +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", imgs=" + imgs +
                ", address='" + address + '\'' +
                ", location=" + location +
                ", price=" + price +
                ", goodUserObjectIds=" + goodUserObjectIds +
                ", messageList=" + messageList +
                '}';
    }
}
