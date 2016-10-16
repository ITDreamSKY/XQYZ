package dream.xqyz.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class Message extends BmobObject {
    private User user;
    private String content;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
