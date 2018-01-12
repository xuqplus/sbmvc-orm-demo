package cn.xuqplus.controller.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Controller
@RequestMapping("")
public class ConcurrentQueryController {

    private static Logger logger = LoggerFactory.getLogger(ConcurrentQueryController.class);

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    DataSource dataSource;

    @RequestMapping("session")
    @ResponseBody
    public String session() throws InterruptedException {
        //key:task的sql语句,value:语句的查询结果
        final Queue<Map<String, List>> result = new ConcurrentLinkedQueue<>();
        final Queue<String> tasks = new ConcurrentLinkedQueue() {{
            for (int i = 0; i < 4000; i++) {
                add("select current_timestamp() from dual where 0 = (select sleep(1))");
            }
        }};
        Queue<Session> resources = new ConcurrentLinkedQueue();
        Map<Session, Thread> threadMap = new HashMap<>();
        final int[] maxSessionSize = {200};
        while (!tasks.isEmpty()) {
            //分发所有task
            while (!tasks.isEmpty()) {
                //可能无法获取数据库连接
                if (maxSessionSize[0] <= 0) {
                    int t = resources.size();
                    for (Session session : resources) {
                        threadMap.get(session).join();
                    }
                    List<Thread> threads = new ArrayList<>(t);
                    for (int i = 0; i < t; i++) {
                        Thread thread = new Thread(() -> {
                            Session session = resources.poll();
                            session.createSQLQuery("select 1").list();
                            //若上一句无异常,则可放回资源池
                            resources.add(session);
                            maxSessionSize[0] = resources.size();
                        });
                        thread.start();
                        threads.add(thread);
                    }
                    for (Thread thread : threads) {
                        thread.join();
                    }
                    if (resources.isEmpty()) {
                        return null;
                    }
                }
                Session session = resources.size() < maxSessionSize[0] ? sessionFactory.openSession() : resources.poll();
                //此session放到队列最后
                resources.add(session);
                //让session所处的线程执行完毕,并删除线程
                if (null != threadMap.get(session)) {
                    threadMap.get(session).join();
                    threadMap.put(session, null);
                }
                final String sql = tasks.poll();
                final Thread thread1 = new Thread(() -> {
                    try {
                        List list = session.createSQLQuery(sql).list();
                        result.add(new HashMap(1) {{
                            put(sql, list);
                        }});
                    } catch (Exception e) {
                        //任务处理失败, 将任务放回队列中
                        tasks.add(sql);
                        //减小maxSessionSize值, 因为无法获取这么多连接
                        maxSessionSize[0] /= 2;
                        e.printStackTrace();
                    }
                });
                thread1.start();
                //session与thread绑定
                threadMap.put(session, thread1);
            }
            //加入所有线程
            while (!resources.isEmpty()) {
                Session session = resources.poll();
                Thread thread = threadMap.get(session);
                thread.join();
                threadMap.remove(session);
                session.close();//session需要显式关闭,会回到连接池
            }
        }
        //处理查询结果
        result.forEach(map -> map.entrySet().forEach(e ->
                logger.info("{}:{}", e.getKey(), e.getValue())
        ));
        return "result " + result.size() + "<br>" + result;
    }

    @RequestMapping("query")
    @ResponseBody
    public String query() throws InterruptedException {
        Session session = sessionFactory.openSession();
        List list = new ArrayList();
        list.add(session.createSQLQuery("select current_timestamp() from dual where 0 = (select sleep(1))").list());
        list.add(session.createSQLQuery("select current_timestamp() from dual where 0 = (select sleep(1))").list());
        list.add(session.createSQLQuery("select current_timestamp() from dual where 0 = (select sleep(1))").list());
        Thread thread = new Thread(() -> {
            list.add(session.createSQLQuery("select 1 from dual where 0 = (select sleep(1))").list());
            list.add(session.createSQLQuery("select 1 from dual where 0 = (select sleep(1))").list());
            list.add(session.createSQLQuery("select 1 from dual where 0 = (select sleep(1))").list());
        });
        thread.start();
        thread.join();
        return list.toString();
    }
}
