package qd.cs.koi.database.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qd.cs.koi.database.interfaces.User.UserDTO;
import qd.cs.koi.database.interfaces.User.UserListReqDTO;
import qd.cs.koi.database.interfaces.User.UserProfileDTO;
import qd.cs.koi.database.interfaces.User.UserUpdateDTO;
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
        UserSessionDTO userSessionDTO = userService.register(userDTO);
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

        UserSessionDTO userSessionDTO=null;
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            // 登录校验
            userSessionDTO = this.userService.login(username, password);

            writeSessionToHeader(response, userSessionDTO);
        }
        return ResponseResult.ok(userSessionDTO);
        //return ResponseResult.error();
    }

    //TODO:完成搜索接口
    @GetMapping("/search")
    @ResponseBody
    public void search(UserListReqDTO reqDTO) {
        userService.search(reqDTO);
    }

    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpServletResponse response) throws UnsupportedEncodingException {
        //String encodeCookie = URLEncoder.encode(null, "utf-8");
        //Cookie cookie = new Cookie(UserSessionDTO.HEADER_KEY,null);
        //response.addCookie(cookie);
    }

    @GetMapping("/getProfile")
    @ResponseBody
    public ResponseResult<UserProfileDTO> getProfile(@UserSession UserSessionDTO userSessionDTO){
        UserProfileDTO profile = userService.getProfile(userSessionDTO);
        return ResponseResult.ok(profile);
    }


    @GetMapping("/session")
    @ResponseBody
    public String session(HttpServletResponse response,
                       @UserSession UserSessionDTO userSessionDTO) throws UnsupportedEncodingException {
        return JSON.toJSONString(userSessionDTO);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseResult<Long> update(@UserSession UserSessionDTO userSessionDTO,
                                       @RequestBody UserUpdateDTO userUpdateDTO){
        //不允许用户修改自己的身份
        userUpdateDTO.setRoles(null);
        Long update = userService.update(userSessionDTO, userUpdateDTO);
        return ResponseResult.ok(update);
    }

    @GetMapping("/isManage")
    @ResponseBody
    public boolean isManage(@UserSession UserSessionDTO userSessionDTO){
        return userSessionDTO != null && userSessionDTO.userIsAdmin();
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



    /*
    @PostMapping("plUpdate")
    public ResponseResult pleUpdate(@RequestParam("filename") MultipartFile file) throws IOException {
        //String Originalfilename = file.getOriginalFilename();
        // String fileName = file.getName();
        //  System.out.println("orname="+Originalfilename+";"+"filename"+file.getName());

        // 获取文件全名
        String fileName = file.getOriginalFilename();
        //设置文件路径
        String templatePath = "G:/excel/";
        File dest0 = new File(templatePath);
        File dest = new File(dest0, fileName);
        //文件上传-覆盖
        try {
            // 检测是否存在目录
            if (!dest0.getParentFile().exists()) {
                dest0.getParentFile().mkdirs();
                //检测文件是否存在
            }
            if (!dest.exists()) {
                dest.mkdirs();
            }
            file.transferTo(dest);
        } catch (Exception e) {
            return ResponseResult.error();
        }

        String finameUrl = templatePath+fileName;
        ExcelReader excelReader = null;
        try {
            //TeacherExcel.class对应的是和模板一样的实体类，
            //eduTeacherService对应持久层的接口
            excelReader = EasyExcel.read(finameUrl, TeacherExcel.class, new UserDataListener(eduTeacherService)).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {

            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
        return ResponseResult.ok();
    }*/


}
