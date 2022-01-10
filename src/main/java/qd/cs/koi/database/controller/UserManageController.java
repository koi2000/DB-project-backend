package qd.cs.koi.database.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qd.cs.koi.database.interfaces.User.*;
import qd.cs.koi.database.service.user.UserManageService;
import qd.cs.koi.database.utils.Enums.PermissionEnum;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.ResponseResult;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.ExcelUtil;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private UserManageService userManageService;

    @GetMapping("/list")
    @ResponseBody
    public List<UserDTO> list(UserListReqDTO reqDTO,
                              @UserSession UserSessionDTO userSessionDTO) {
        return userManageService.list(reqDTO);
    }

    @PostMapping("/update")
    @ResponseBody
    public Void update(@RequestBody @Valid UserManageUpdateReqDTO reqDTO,
                       @UserSession UserSessionDTO userSessionDTO) {
        // superadmin 才能改密码+改权限
        if (PermissionEnum.SUPERADMIN.notIn(userSessionDTO)) {
            reqDTO.setPassword(null);
            reqDTO.setRoles(null);
        }
        userManageService.update(reqDTO, userSessionDTO);
        return null;
    }

    @PostMapping("/addUsers")
    @ResponseBody
    public Void addUsers(@RequestBody @Valid List<UserBatchAddDTO> userDTOList,
                         @UserSession UserSessionDTO userSessionDTO) {
        userManageService.addUsers(userDTOList);
        return null;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Void delete(@RequestBody List<String> usernameList,
                       @UserSession UserSessionDTO userSessionDTO) {
        AssertUtils.isTrue(PermissionEnum.SUPERADMIN.in(userSessionDTO), ApiExceptionEnum.USER_NOT_MATCHING);
        userManageService.delete(usernameList);
        return null;
    }

    @GetMapping("/getUser")
    @ResponseBody
    public UserDTO getUser(@RequestParam("UserId") Long userId,
                           @UserSession UserSessionDTO userSessionDTO) {
        //AssertUtils.isTrue(PermissionEnum.SUPERADMIN.in(userSessionDTO), ApiExceptionEnum.USER_NOT_MATCHING);
        return userManageService.getUser(userId);
    }

    @GetMapping("/download/user")
    public void downloadUser(HttpServletResponse response){

        String fileName = "用户";
        String sheetName="用户";
        List<UserDTO> list = userManageService.list(new UserListReqDTO());
        List<UserExcelDTO> userExcelDTOS = list.stream().map(o -> {
            return UserExcelDTO.builder()
                    .username(o.getUsername())
                    .nickname(o.getNickname())
                    .phone(o.getPhone())
                    .gender(o.getGender())
                    .email(o.getEmail())
                    .build();

        }).collect(Collectors.toList());

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            //String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


            ExcelUtil.writeExcel(response,userExcelDTOS,fileName,sheetName,UserExcelDTO.class);
            //response.setHeader("Content-Disposition", "attachment; filename=se.xlsx");
        } catch (Exception e) {
            System.out.println(e.getCause());

        }
    }

    /*下载模板*/
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response){
        String fileName = "用户导入模板";
        String sheetName="用户导入模板";
        List<UserExcelDTO> userExcelList = new ArrayList<>();

        UserExcelDTO userExcelDTO = UserExcelDTO.builder()
                .username("张三")
                .nickname("法外狂徒")
                .password("123456")
                .phone("10086")
                .email("2000@mail.sdu.edu.cn")
                .gender(3)
                .build();
        userExcelList.add(userExcelDTO);

        try {
            //TeacherExcel.class对应你的模板类
            //teacherExcelList模板的例子
            //也可以使用这种方式导出你查询出数据excel文件
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            //String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


            ExcelUtil.writeExcel(response,userExcelList,fileName,sheetName,UserExcelDTO.class);
            //response.setHeader("Content-Disposition", "attachment; filename=se.xlsx");
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    @PostMapping("/importUser")
    public ResponseResult<T> plUpdate(@RequestBody MultipartFile file) throws IOException {
        //String Originalfilename = file.getOriginalFilename();
        // String fileName = file.getName();
        //  System.out.println("orname="+Originalfilename+";"+"filename"+file.getName());
        //TeacherExcel.class对应的是和模板一样的实体类，
        //eduTeacherService对应持久层的接口
        userManageService.loadUser(file);
        return ResponseResult.ok();
    }




}
