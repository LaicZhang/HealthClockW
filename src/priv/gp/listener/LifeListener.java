package priv.gp.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import priv.gp.pojo.Task;
import priv.gp.util.ChaoXingUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 15932
 */
@WebListener()
public class LifeListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    private ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1);

    public LifeListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ChaoXingUtil.readConnectionData();
        scheduled.scheduleWithFixedDelay(new Task(), 0, 1, TimeUnit.DAYS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        boolean done = false;
        scheduled.shutdown();
        do {
            try {
                done = scheduled.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
            }
        } while (!done);
        ChaoXingUtil.writeUserList(ChaoXingUtil.JSON_DATA.getUserList());
    }
}
