package priv.gp.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import priv.gp.util.ChaoXingUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 15932
 */
@WebServlet(name = "add")
public class AddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uname = request.getParameter("uname");
        String password = request.getParameter("password");
        String clazz = request.getParameter("class");
        String location = request.getParameter("location");
        String[] college = request.getParameterValues("college");
        if (!"".equals(uname) && !"".equals(password)) {
            JSONObject jsonObject = new JSONObject(6);
            jsonObject.put("uname", uname);
            jsonObject.put("password", password);
            jsonObject.put("cookie", null);
            jsonObject.put("ok", false);
            if (college == null || clazz == null || location == null) {
                JSONArray jsonArray = ChaoXingUtil.getFormIdValueData(ChaoXingUtil.publicUser);
                if (jsonArray.size() == 3) {
                    if (college == null) {
                        jsonObject.put("college", ((JSONObject) jsonArray.get(0)).getJSONArray("groupValues").getJSONObject(0).getJSONArray("values")
                                .getJSONArray(0).getJSONObject(0).getString("val"));
                    }
                    if (clazz == null) {
                        jsonObject.put("class", ((JSONObject) jsonArray.get(1)).getJSONArray("groupValues").getJSONObject(0).getJSONArray("values")
                                .getJSONArray(0).getJSONObject(0).getString("val"));
                    }
                    if (location == null) {
                        jsonObject.put("location", ((JSONObject) jsonArray.get(2)).getJSONArray("groupValues").getJSONObject(0).getJSONArray("values")
                                .getJSONArray(0).getJSONObject(0).getString("val"));
                    }
                }
            } else {
                jsonObject.put("college", college[0]);
                jsonObject.put("class", clazz);
                jsonObject.put("location", location);
            }
            JSONArray userList = ChaoXingUtil.JSON_DATA.getUserList();
            JSONArray existed = ChaoXingUtil.readUserList();
            int j;
            j = ChaoXingUtil.contains(userList, uname);
            List<String> result = new ArrayList<>(1);
            if (j != -1) {
                ChaoXingUtil.existed(jsonObject, (JSONObject) existed.get(j), result);
            } else {
                j = ChaoXingUtil.contains(existed, uname);
                if (j != -1) {
                    ChaoXingUtil.existed(jsonObject, (JSONObject) existed.get(j), result);
                } else {
                    result.add(ChaoXingUtil.newAdd(jsonObject));
                }
            }
            request.setAttribute("result", result.get(0));
            ChaoXingUtil.writeLog(result);
            request.getRequestDispatcher("/WEB-INF/jpa/result.jsp").forward(request, response);
        } else {
            response.sendRedirect("index.jsp");
        }
    }
}
