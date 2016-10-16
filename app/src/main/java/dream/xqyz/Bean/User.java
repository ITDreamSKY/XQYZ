package dream.xqyz.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/9/17 0017.
 */
public class User extends BmobUser {
    //用户名
    private String userName;
    //密码
    private String passWord;
    //昵称
    private String nickName;
    //头像
    private BmobFile userIcon;
    //余额
    private Double money;
    //收货地址
    private List<Address> addressList;

    public List<Address> getAddressList() {
        if (addressList == null) {
            addressList = new ArrayList<>();
        }
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BmobFile getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(BmobFile userIcon) {
        this.userIcon = userIcon;
    }

    public Double getMoney() {
        if (money == null) {
            money = 0.0;
        }
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}

