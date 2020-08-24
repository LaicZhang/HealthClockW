package priv.gp.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import priv.gp.util.ChaoXingUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author 15932
 */
public class Task implements Runnable {

    @Override
    public void run() {
        List<String> log = null;
        try {
            for (int i = 0; i < 2; i++) {
                ChaoXingUtil.JSON_DATA.setUserList(ChaoXingUtil.readUserList());
                JSONArray userList = ChaoXingUtil.JSON_DATA.getUserList();
                JSONObject jsonObject;
                for (int j = 0; j < userList.size(); j++) {
                    jsonObject = (JSONObject) userList.get(j);
                    jsonObject.put("ok", false);
                }
                log = ChaoXingUtil.autoHealthClock(ChaoXingUtil.JSON_DATA.getUserList());
                if (log.size() > 0) {
                    ChaoXingUtil.writeLog(log);
                    break;
                }
            }
            if (log.size() > 0) {
                ChaoXingUtil.publicUser = (JSONObject) ChaoXingUtil.JSON_DATA.getUserList().get(0);
            }
        } catch (Throwable e) {
        }
    }
}
