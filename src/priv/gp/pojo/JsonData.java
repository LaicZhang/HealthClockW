package priv.gp.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 15932
 */
public class JsonData {

    private JSONArray userList = new JSONArray();
    private JSONArray connectionData = new JSONArray();

    public JsonData() {
    }

    public JSONArray getUserList() {
        return userList;
    }

    public void setUserList(JSONArray userList) {
        this.userList = userList;
    }

    public JSONArray getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(JSONArray connectionData) {
        this.connectionData = connectionData;
    }
}
