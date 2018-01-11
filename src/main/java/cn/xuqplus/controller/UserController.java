package cn.xuqplus.controller;

import cn.xuqplus.entity.User;
import cn.xuqplus.entity.User.Sex;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  SessionFactory sessionFactory;

  @GetMapping("find")
  public String find(ModelAndView mav) {
    User user = new User();
    user.setId(1L);
    user.setName("xqq");
    user.setPwd("123");
    user.setSex(Sex.male);
    sessionFactory.openSession().save(user);
    return "find";
  }
}
