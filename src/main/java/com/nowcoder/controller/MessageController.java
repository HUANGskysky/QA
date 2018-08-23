package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.SensitiveService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by Huangsky on 2018/8/13.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    SensitiveService sensitiveService;

    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try {
            if (hostHolder.getUser()==null){
                return WendaUtil.getJSONString(999,"未登录");
            }
            User user = userService.selectByName(toName);
            if (user == null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }

            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(sensitiveService.filter(content));
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);


        }catch (Exception e){
            logger.error("发送消息失败"+e.getMessage());
            return WendaUtil.getJSONString(1,"发信失败");
        }


    }

    @RequestMapping(path = {"msg/list"},method = {RequestMethod.GET})
    public String conversationList(Model model){
        try {
            if (hostHolder.getUser()==null){
                return "redirect:/reglogin";
            }
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            List<ViewObject> conversations = new ArrayList<>();
            for (Message message:conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                int targetId= message.getFromId() == localUserId ?message.getToId():message.getFromId();
                vo.set("user",userService.getUser(targetId));
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);

        }catch (Exception e){
            logger.error("获取站内信列表失败:"+e.getMessage());
        }

        return "letter";

    }

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId){
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for (Message message:messageList){
                ViewObject vo = new ViewObject();
                message.setHasRead(1);
                vo.set("message",message);
                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取详情失败："+e.getMessage());
        }


        return "letterDetail";
    }



}
