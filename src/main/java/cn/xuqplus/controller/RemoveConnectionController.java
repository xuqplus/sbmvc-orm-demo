package cn.xuqplus.controller;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.List;

@Controller
@RequestMapping("")
public class RemoveConnectionController {
    @Autowired
    SessionFactory sessionFactory;


    @RequestMapping("remove")
    @ResponseBody
    public String remove() {
        /**
         * removeAbandoned会导致耗时查询被关闭
         * 如removeAbandonedTimeout为10秒时下面的查询可能会失败
         * 跟检查间隔和与其他策略共同作用
         */
        String sql = "select current_timestamp() from dual where 0 = (select sleep(2))";
        List list = sessionFactory.openSession().createSQLQuery(sql).list();

        /**
         * 显示关闭连接
         */
        Session session = sessionFactory.openSession();
        Query query = session.createSQLQuery(sql);
        query.list();
        session.flush();
        session.close();
        return list.toString();
    }
}
