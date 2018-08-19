package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Huangsky on 2018/8/19.
 */
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandler(EventModel eventModel) {
        //xxx进行判断，发现这个用户登录异常
        Map<String,Object> map = new HashMap<>();
        map.put("username", eventModel.getExts("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExts("email"),"登录IP异常","mails/login_exception",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
