package qd.cs.koi.database.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qd.cs.koi.database.entity.MessageDO;
import qd.cs.koi.database.service.message.MessageService;
import qd.cs.koi.database.utils.annations.ApiResponseBody;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.UserSessionDTO;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MessageService messageService;

    @GetMapping("/history")
    @ResponseBody
    public List<MessageDO> getChatHistory(@UserSession UserSessionDTO userSessionDTO,
                                          @RequestParam("chatUser") String chatUser) {
        return messageService.queryMessage(userSessionDTO.getNickname(), chatUser);
    }

    @GetMapping("/getChatUser")
    @ResponseBody
    public String getChatUser(@UserSession UserSessionDTO userSessionDTO) {
        String key = userSessionDTO.getUsername();
        return redisTemplate.boundValueOps(key).get();
    }

    @GetMapping("/setChatUser")
    @ResponseBody
    public void setChatUser(@UserSession UserSessionDTO userSessionDTO,
                            @RequestParam("chatUser") String chatUser) {
        String key = userSessionDTO.getUsername();
        redisTemplate.opsForValue().set(key, chatUser);
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);
        //redisTemplate.boundValueOps(key).set(chatUser,30, TimeUnit.MINUTES);
    }

    @GetMapping("/saveMessage")
    @ResponseBody
    public void setChatMessage(@UserSession UserSessionDTO userSessionDTO,
                               @RequestParam("chatUser") String chatUser) {
        String key = userSessionDTO.getUsername() + "-" + chatUser;

    }

    @GetMapping("/getMessage")
    @ResponseBody
    public void getMessage() {

    }
}
