package cn.xuqplus.controller.user;

import cn.xuqplus.dao.user.UserDAO;
import cn.xuqplus.entity.user.AbstractUser;
import cn.xuqplus.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserDAO userDAO;


    @GetMapping("find")
    @ResponseBody
    public List<User> find() {
        return userDAO.findAll();
    }

    @GetMapping("save")
    @ResponseBody
    public User save(ModelAndView mav) {
        User user = new User();
        user.setId(1L);
        user.setName("xqq");
        user.setUsername("xqq");
        user.setPassword("123456");
        user.setSex(AbstractUser.Sex.male);
        userDAO.save(user);
        return user;
    }
}
