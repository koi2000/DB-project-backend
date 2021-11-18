package qd.cs.koi.database.service.user;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qd.cs.koi.database.converter.UserConverter;
import qd.cs.koi.database.converter.UserSessionConverter;
import qd.cs.koi.database.dao.UserDao;
import qd.cs.koi.database.entity.UserDO;
import qd.cs.koi.database.interfaces.User.UserDTO;
import qd.cs.koi.database.utils.Enums.PermissionEnum;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.CodecUtils;
import qd.cs.koi.database.utils.web.ApiException;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserConverter userConverter;

    @Autowired
    UserDao userDao;

    @Autowired
    UserSessionConverter userSessionConverter;

    @Transactional
    public UserSessionDTO login(String username, String password) throws ApiException {

        UserDO userDO = null;
        try {
            userDO = verifyAndGetDO(username, password);

            //List<Long> groupIdList = groupService.userIdToGroupIdList(userDO.getUserId());
            return userSessionConverter.to(userDO);
        } catch (ApiException e) {
            throw e;
        }
    }

    @Transactional
    public UserSessionDTO register(UserDTO userDTO) {
        UserDO userDO = userConverter.from(userDTO);
        userDO.setSalt(CodecUtils.generateSalt());
        userDO.setPassword(CodecUtils.md5Hex(userDO.getPassword(), userDO.getSalt()));
        userDO.setRoles(PermissionEnum.USER.name);
        if (StringUtils.isBlank(userDO.getNickname())) {
            userDO.setNickname(userDO.getUsername());
        }


        try {
            AssertUtils.isTrue(userDao.save(userDO), ApiExceptionEnum.CONTEST_NOT_BEGIN);
        }
        catch (DuplicateKeyException e) {
            throw new ApiException(ApiExceptionEnum.USER_EXIST);
        }catch (Exception e){
            throw new ApiException(ApiExceptionEnum.UNKNOWN_ERROR);
        }

        return loginWithWritingSession(userDO);
    }


    public UserSessionDTO test(){
        userDao.save(UserDO.builder().build());
        return null;
    }

    /**
     * 登录并写 session
     */
    private UserSessionDTO loginWithWritingSession(UserDO userDO) {
        UserSessionDTO userSessionDTO = UserSessionDTO.builder()
                .username(userDO.getUsername())
                .build();

        return userSessionDTO;
    }


    public @NotNull UserDO verifyAndGetDO(String username, String password) throws ApiException {
        // 查找对应用户后验证密码
        LambdaQueryChainWrapper<UserDO> query = userDao.lambdaQuery();
        if (username.indexOf('@') != -1) {
            query.eq(UserDO::getEmail, username);
        } else {
            query.eq(UserDO::getUsername, username);
        }
        UserDO userDO = query.one();
        AssertUtils.notNull(userDO, ApiExceptionEnum.USER_NOT_FOUND);
        AssertUtils.isTrue(userDO.getPassword().equals(CodecUtils.md5Hex(password, userDO.getSalt())), ApiExceptionEnum.PASSWORD_NOT_MATCHING);
        return userDO;
    }
}
