/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.service.user;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import qd.cs.koi.database.converter.UserBatchAddConverter;
import qd.cs.koi.database.converter.UserConverter;
import qd.cs.koi.database.converter.UserManageUpdateConverter;
import qd.cs.koi.database.dao.UserDao;
import qd.cs.koi.database.entity.UserDO;
import qd.cs.koi.database.entity.UserDOField;
import qd.cs.koi.database.interfaces.User.*;
import qd.cs.koi.database.utils.Enums.PermissionEnum;
import qd.cs.koi.database.utils.entity.ResponseResult;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.CodecUtils;
import qd.cs.koi.database.utils.util.DemoDataListener;
import qd.cs.koi.database.utils.web.ApiException;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserManageService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserManageUpdateConverter userManageUpdateConverter;

    @Autowired
    private UserBatchAddConverter userBatchAddConverter;


    public List<UserDTO> list(UserListReqDTO reqDTO) {
        LambdaQueryChainWrapper<UserDO> query = userDao.lambdaQuery();

        Optional.of(reqDTO).map(UserListReqDTO::getUsername).ifPresent(username -> {
            query.like(UserDO::getUsername, username);
        });


        Optional.of(reqDTO).map(UserListReqDTO::getPhone).ifPresent(phone -> {
            query.like(UserDO::getPhone, phone);
        });

        Optional.of(reqDTO).map(UserListReqDTO::getEmail).ifPresent(email -> {
            query.like(UserDO::getEmail, email);
        });


        // searchKey
        Optional.of(reqDTO).map(UserListReqDTO::getSearchKey).filter(StringUtils::isNotBlank).ifPresent(searchKey -> {
            query.or(o1 -> o1.like(UserDO::getUsername, searchKey))
                 .or(o1 -> o1.like(UserDO::getNickname, searchKey))
                 .or(o1 -> o1.like(UserDO::getPhone, searchKey))
                 .or(o1 -> o1.like(UserDO::getEmail, searchKey));

        });
        // 管理员尽量排前面
        query.last("ORDER BY LENGTH(" + UserDOField.ROLES + ") DESC, " + UserDOField.ID);

        //Page<UserDO> pageResult = query.page(new Page<>(reqDTO.getPageNow(), reqDTO.getPageSize()));
        List<UserDO> list = query.list();
        return userConverter.to(list);
    }

    public void update(UserManageUpdateReqDTO userDTO, UserSessionDTO userSessionDTO) {
        //只有超管可以修改用户身份
        if(!userSessionDTO.userIsSuperAdmin()){
            List<String>roles = new ArrayList<>();
            roles.add(PermissionEnum.USER.name);
            userDTO.setRoles(roles);
        }
        UserDO userDO = userManageUpdateConverter.from(userDTO);
        Optional.of(userDO).map(UserDO::getPassword).ifPresent(password -> {
            userDO.setSalt(CodecUtils.generateSalt());
            userDO.setPassword(CodecUtils.md5Hex(password, userDO.getSalt()));
        });

        LambdaUpdateChainWrapper<UserDO> updater = userDao.lambdaUpdate();
        // email=null 不修改, email="" 即清空, 这个语义需要特别注意
        if (userDTO.getEmail() != null && StringUtils.isBlank(userDTO.getEmail())) {
            updater.set(UserDO::getEmail, null);
            userDO.setEmail(null);
        }

        updater.eq(UserDO::getUsername, userDO.getUsername()).update(userDO);

        // 打一下操作日志
        userDTO.setPassword(null);

        AssertUtils.isTrue(userDao.updateById(userDO), ApiExceptionEnum.UNKNOWN_ERROR);
        log.info("manager: {} update user info: {}", userSessionDTO.getUsername(), userDTO);
    }

    @Transactional
    public void addUsers(List<UserBatchAddDTO> userDTOList) {
        List<UserDO> userDOList = userBatchAddConverter.from(userDTOList);

        userDOList.forEach(userDO -> {
            userDO.setSalt(CodecUtils.generateSalt());
            userDO.setPassword(CodecUtils.md5Hex(userDO.getPassword(), userDO.getSalt()));
            userDO.setRoles(PermissionEnum.USER.name);
            if (userDO.getNickname() == null) {
                userDO.setNickname(userDO.getUsername());
            }
        });

        userDao.saveBatch(userDOList);
    }

    public void delete(List<String> usernameList) {
        userDao.lambdaUpdate().in(UserDO::getUsername, usernameList).remove();
    }

    public UserDTO getUser(Long userId) {
        UserDO userDO = userDao.getById(userId);
        return userConverter.to(userDO);
    }

    public void loadUser(MultipartFile file) throws IOException {
        try {
            //文件输入流
            UserDao userDao = new UserDao();
            //调用方法进行读取
            EasyExcel.read(file.getInputStream(), UserExcelDTO.class,new DemoDataListener(userDao)).sheet().doRead();
        }catch(Exception e){
            throw new ApiException(ApiExceptionEnum.UNKNOWN_ERROR);
        }

    }
}