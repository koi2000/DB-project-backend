package qd.cs.koi.database.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qd.cs.koi.database.interfaces.User.UserDTO;
import qd.cs.koi.database.service.user.UserExtensionService;
import qd.cs.koi.database.service.user.UserService;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.ResponseResult;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.EncrypDES;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    EncrypDES encrypDES;

    @Autowired
    UserService userService;

    @Autowired
    private UserExtensionService userExtensionService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseResult<UserSessionDTO> register(HttpServletResponse response,
                                                   @Valid @RequestBody UserDTO userDTO) throws Exception {
        userDTO.setRoles(null);
        UserSessionDTO userSessionDTO = this.userService.register(userDTO);
        //UserSessionDTO userSessionDTO = this.userService.test();
        writeSessionToHeader(response, userSessionDTO);
        return ResponseResult.ok(userSessionDTO);
    }


    @GetMapping("/login")
    @ResponseBody
    public ResponseResult<UserSessionDTO> login(HttpServletResponse response,
                                                @RequestParam("userName") String username,
                                                @RequestParam("passWord") String password
    ) throws Exception {

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            // 登录校验
            UserSessionDTO userSessionDTO = this.userService.login(username, password);

            writeSessionToHeader(response, userSessionDTO);
            return ResponseResult.ok(userSessionDTO);
        }
        return ResponseResult.error();
    }


    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpServletResponse response) throws UnsupportedEncodingException {
        //String encodeCookie = URLEncoder.encode(null, "utf-8");
        //Cookie cookie = new Cookie(UserSessionDTO.HEADER_KEY,null);
        //response.addCookie(cookie);
    }

    private void writeSessionToHeader(@NotNull HttpServletResponse response,
                                      @Nullable UserSessionDTO userSessionDTO) throws Exception {

        if (userSessionDTO != null) {
            //response.setHeader(UserSessionDTO.HEADER_KEY, JSON.toJSONString(userSessionDTO));
            //对返回的信息进行加密
            String encrypt = encrypDES.encrypt(JSON.toJSONString(userSessionDTO));

            String encodeCookie = URLEncoder.encode(encrypt, "utf-8");
            //
            response.setHeader(UserSessionDTO.HEADER_KEY,encodeCookie);
            //Cookie cookie = new Cookie(UserSessionDTO.HEADER_KEY,encodeCookie);
            //response.addCookie(cookie);
            //设置允许该返回头被查看到
            response.setHeader("Access-Control-Expose-Headers", "UserInfo");
        }
    }

    @GetMapping("/session")
    public String nnsa(HttpServletResponse response,
                       @UserSession UserSessionDTO userSessionDTO) throws UnsupportedEncodingException {
        return JSON.toJSONString(userSessionDTO);
    }

}
