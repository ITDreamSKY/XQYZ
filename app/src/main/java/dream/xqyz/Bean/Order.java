package dream.xqyz.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by SKYMAC on 16/9/21.
 */
public class Order extends BmobObject {
    //卖家ID
    private String saleUserId;
    //买家ID
    private String buyUserId;
    //支付状态
    private Boolean isPay;
    //收货信息
    private Address address;
    //商品ID
    private String commodityId;
    //运单号码 同时用来判断是否已发货
    private String sendNum;
    //是否已收货
    private Boolean isReceiver;
    //是否被取消
    private Boolean isCancel;
    //卖家是否已收款
    private Boolean isSaleUserGetMoney;

    public Boolean getSaleUserGetMoney() {
        if(isSaleUserGetMoney==null){
            isSaleUserGetMoney = false;
        }
        return isSaleUserGetMoney;
    }

    public void setSaleUserGetMoney(Boolean saleUserGetMoney) {
        isSaleUserGetMoney = saleUserGetMoney;
    }

    public Boolean getCancel() {
        if (isCancel == null) {
            isCancel = false;
        }
        return isCancel;
    }

    public void setCancel(Boolean cancel) {
        isCancel = cancel;
    }

    public Boolean getReceiver() {
        if (isReceiver == null) {
            isReceiver = false;
        }
        return isReceiver;
    }

    public void setReceiver(Boolean receiver) {
        isReceiver = receiver;
    }

    public String getSaleUserId() {
        return saleUserId;
    }

    public void setSaleUserId(String saleUserId) {
        this.saleUserId = saleUserId;
    }

    public String getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(String buyUserId) {
        this.buyUserId = buyUserId;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getSendNum() {
        return sendNum;
    }

    public void setSendNum(String sendNum) {
        this.sendNum = sendNum;
    }

    @Override
    public String toString() {
        return "Order{" +
                "saleUserId='" + saleUserId + '\'' +
                ", buyUserId='" + buyUserId + '\'' +
                ", isPay=" + isPay +
                ", address=" + address +
                ", commodityId='" + commodityId + '\'' +
                ", sendNum='" + sendNum + '\'' +
                ", isReceiver=" + isReceiver +
                '}';
    }
}
