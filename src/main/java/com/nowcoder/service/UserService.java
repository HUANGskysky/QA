package com.nowcoder.service;

import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public Map<String,String> register(String username,String password){
        Map<String,String> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已经被注册");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));

        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

        user.setPassword(WendaUtil.MD5(password+user.getSalt()));

        userDAO.addUser(user);

        return map;
    }

}
