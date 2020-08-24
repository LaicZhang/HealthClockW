package priv.gp.util;

import com.alibaba.fastjson.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import priv.gp.pojo.JsonData;
import priv.gp.pojo.Task;
import sun.font.TrueTypeFont;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 15932
 */
public class ChaoXingUtil {

    public static final JsonData JSON_DATA = new JsonData();
    public static Connection loginConnection;
    public static Connection postFormConnection;
    public static Connection getFormValueConnection;
    public static JSONObject publicUser;

    public static void readConnectionData() {
        try (JSONReader jsonReader = new JSONReader(new BufferedReader(new InputStreamReader(new FileInputStream(Objects.requireNonNull(ChaoXingUtil.class.getClassLoader().getResource("/")).getPath()
                + "../jpa/connectionData.json"), StandardCharsets.UTF_8)))) {
            jsonReader.startObject();
            if (jsonReader.hasNext()) {
                String connectionData = "connectionData";
                if (connectionData.equals(jsonReader.readString())) {
                    jsonReader.startArray();
                    while (jsonReader.hasNext()) {
                        ChaoXingUtil.JSON_DATA.getConnectionData().add(jsonReader.readObject());
                    }
                    jsonReader.endArray();
                }
            }
            jsonReader.endObject();
        } catch (FileNotFoundException e) {
        }
    }

    public static JSONArray readUserList() {
        JSONArray jsonArray = new JSONArray();
        try (JSONReader jsonReader = new JSONReader(new BufferedReader(new InputStreamReader(new FileInputStream(Objects.requireNonNull(ChaoXingUtil.class.getClassLoader().getResource("/")).getPath()
                + "../jpa/userList.json"), StandardCharsets.UTF_8)))) {
            jsonReader.startArray();
            while (jsonReader.hasNext()) {
                JSONObject jsonObject = new JSONObject(6);
                jsonReader.startObject();
                while (jsonReader.hasNext()) {
                    jsonObject.put(jsonReader.readString(), jsonReader.readObject());
                    jsonObject.put(jsonReader.readString(), jsonReader.readObject());
                    jsonObject.put(jsonReader.readString(), jsonReader.readObject());
                    jsonObject.put(jsonReader.readString(), jsonReader.readObject());
                    jsonObject.put(jsonReader.readString(), jsonReader.readObject());
                    jsonObject.put(jsonReader.readString(), jsonReader.readObject());
                    jsonObject.put(jsonReader.readString(), jsonReader.readObject());
                    jsonArray.add(jsonObject);
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
        } catch (FileNotFoundException e) {
        }
        return jsonArray;
    }

    private static JSONObject getCookie(JSONObject user) {
        loginConnection = Jsoup.connect(((JSONObject) ChaoXingUtil.JSON_DATA.getConnectionData().get(0)).getString("loginURL"));
        loginConnection.headers((Map) ((JSONObject) JSON_DATA.getConnectionData().get(2)).getInnerMap());
        loginConnection.data("t", String.valueOf(((JSONObject) JSON_DATA.getConnectionData().get(0)).getBoolean("t")));
        loginConnection.data("uname", user.getString("uname"));
        loginConnection.data("password", Base64.getEncoder().encodeToString(user.getString("password").getBytes(StandardCharsets.UTF_8)));
        loginConnection.postDataCharset("UTF-8").method(Connection.Method.POST);
        JSONObject cookie = null;
        try {
            Connection.Response response = loginConnection.execute();
            if ((cookie = JSON.parseObject(response.body())).getBoolean("status")) {
                cookie = (JSONObject) JSON.toJSON(response.cookies());
                user.put("cookie", cookie);
                int j;
                if ((j = contains(JSON_DATA.getUserList(), user.getString("uname"))) != -1) {
                    JSON_DATA.getUserList().set(j, user);
                }
            }
            loginConnection = null;
        } catch (IOException e) {
        }
        return cookie;
    }

    public static JSONArray getFormIdValueData(JSONObject user) {
        getFormValueConnection = Jsoup.connect(((JSONObject) JSON_DATA.getConnectionData().get(3)).getString("formDataListURL"));
        getFormValueConnection.headers((Map) ((JSONObject) JSON_DATA.getConnectionData().get(2)).getInnerMap());
        getFormValueConnection.data("cpage", String.valueOf(((JSONObject) (JSON_DATA.getConnectionData().get(3))).getInteger("cpage")));
        getFormValueConnection.data("formId", String.valueOf(((JSONObject) (JSON_DATA.getConnectionData().get(3))).getInteger("formId")));
        getFormValueConnection.postDataCharset("UTF-8").method(Connection.Method.GET);
        postFormConnection.cookies((Map) ((JSONObject) (user.get("cookie"))).getInnerMap());
        JSONArray jsonArray = new JSONArray(3);
        try {
            JSONObject formValue = JSON.parseObject(getFormValueConnection.execute().body()).getJSONObject("formIdValueData");
            if (formValue != null) {
                jsonArray.add(formValue.getJSONObject("13"));
                jsonArray.add(formValue.getJSONObject("1"));
                jsonArray.add(formValue.getJSONObject("3"));
            }
        } catch (IOException e) {
        }
        return jsonArray;
    }

    private static JSONObject postForm(JSONObject user) {
        postFormConnection = Jsoup.connect(((JSONObject) ChaoXingUtil.JSON_DATA.getConnectionData().get(1)).getString("formURL") + ((JSONObject) (user.get("cookie"))).getString("UID"));
        postFormConnection.headers((Map) ((JSONObject) JSON_DATA.getConnectionData().get(2)).getInnerMap());
        postFormConnection.data("formId", String.valueOf(((JSONObject) (JSON_DATA.getConnectionData().get(1))).getInteger("formId")));
        JSONObject result;
        JSONArray formData = (JSONArray) ((JSONObject) (ChaoXingUtil.JSON_DATA.getConnectionData().get(1))).get("formData");
        result = (JSONObject) formData.get(1);
        result = (JSONObject) (result.getJSONArray("fields").get(0));
        result = (JSONObject) (result.getJSONArray("values").get(0));
        result.put("val", user.getString("class"));
        result = (JSONObject) formData.get(3);
        result = (JSONObject) (result.getJSONArray("fields").get(0));
        result = (JSONObject) (result.getJSONArray("values").get(0));
        result.put("val", user.getString("location"));
        result = (JSONObject) formData.get(0);
        result = (JSONObject) (result.getJSONArray("fields").get(0));
        result = (JSONObject) (result.getJSONArray("values").get(0));
        result.put("val", user.getString("college"));
        postFormConnection.data("formData", formData.toJSONString());
        postFormConnection.cookies((Map) ((JSONObject) (user.get("cookie"))).getInnerMap());
        postFormConnection.postDataCharset("UTF-8").method(Connection.Method.POST);
        try {
            result = JSON.parseObject(postFormConnection.execute().body());
            String ok = "每个用户每日填写1次，您的提交已达上限哦~";
            if (ok.equals(result.getString("msg")) || result.getBoolean("success")) {
                user.put("ok", true);
            }
        } catch (IOException e) {
        }
        postFormConnection = null;
        return result;
    }

    public static void writeUserList(JSONArray userList) {
        if (userList.size() > 0) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(Objects.requireNonNull(ChaoXingUtil.class.getClassLoader().getResource("/")).getPath()
                    + "../jpa/userList.json", "rw")) {
                JSONArray existedUserList = readUserList();
                userList.removeAll(existedUserList);
                randomAccessFile.seek(randomAccessFile.length() - 1);
                Iterator<Object> iterator = userList.iterator();
                JSONObject jsonObject;
                while (iterator.hasNext()) {
                    jsonObject = (JSONObject) iterator.next();
                    if (jsonObject.getBoolean("ok")) {
                        randomAccessFile.write(((existedUserList.size() > 0 ? "," : "") + JSON.toJSONString(jsonObject) + " ]").getBytes(StandardCharsets.UTF_8));
                        iterator.remove();
                    } else {
                        JSON_DATA.getUserList().add(jsonObject);
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    public static List<String> autoHealthClock(JSONArray userList) {
        JSONObject cookie;
        JSONObject user;
        String result = null;
        List<String> resultList = new ArrayList<>(userList.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Object o : userList) {
            user = (JSONObject) o;
            if (user.getBoolean("ok")) {
                continue;
            }
            if ((cookie = user.getJSONObject("cookie")) == null) {
                cookie = getCookie(user);
            }
            boolean cookieError;
            do {
                if (Objects.requireNonNull(cookie).getBoolean("status") == null) {
                    cookie = postForm(user);
                    if (cookie == null) {
                        result = "提交出错";
                        cookieError = false;
                    } else {
                        if (user.getBoolean("ok") || cookie.getBoolean("success")) {
                            result = cookie.getString("msg");
                            cookieError = false;
                        } else {
                            cookieError = true;
                        }
                    }
                } else {
                    result = cookie.getString("msg2");
                    cookieError = false;
                }
            } while (cookieError);
            resultList.add("用户：" + user.getString("uname") + "，结果：" + result + "---时间："
                    + simpleDateFormat.format(new Date(System.currentTimeMillis())));
        }
        writeUserList(userList);
        return resultList;
    }

    public static String newAdd(JSONObject user) {
        List<String> result = null;
        JSONArray jsonArray = new JSONArray(1);
        jsonArray.add(user);
        for (int i = 0; i < 2; i++) {
            result = autoHealthClock(jsonArray);
            if (result.size() > 0) {
                break;
            }
        }
        return result.get(0);
    }

    public static void writeLog(List<String> log) {
        if (log.size() > 0) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Objects.requireNonNull(Task.class.getClassLoader().getResource("/")).getPath() + "../jpa/post.log"), true), StandardCharsets.UTF_8))) {
                for (String s : log) {
                    bufferedWriter.write(s + System.lineSeparator());
                }
            } catch (IOException e) {
            }
        }
    }

    public static int contains(JSONArray userList, String uname) {
        int j = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (((JSONObject) userList.get(i)).getString("uname").equals(uname)) {
                j = i;
                break;
            }
        }
        return j;
    }

    public static void existed(JSONObject jsonObject, JSONObject existed, List<String> result) {
        if (!jsonObject.getString("password").equals(existed.getString("password"))) {
            result.add(ChaoXingUtil.newAdd(jsonObject));
        } else {
            if (!jsonObject.getString("class").equals(existed.getString("class"))
                    || !jsonObject.getString("location").equals(existed.getString("location"))
                    || !jsonObject.getString("college").equals(existed.getString("college"))) {
                jsonObject.put("cookie", existed.getJSONObject("cookie"));
                result.add(ChaoXingUtil.newAdd(jsonObject));
            } else {
                result.add("用户：" + jsonObject.getString("uname") + "，一模一样的信息已经登记!");
            }
        }
    }
}
