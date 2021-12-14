/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.converter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import qd.cs.koi.database.entity.UserDO;
import qd.cs.koi.database.interfaces.User.UserManageUpdateReqDTO;

import java.util.List;
import java.util.Optional;

@org.mapstruct.Mapper(
        componentModel = "spring",
        imports = {BaseConvertUtils.class}
)
public interface UserManageUpdateConverter extends BaseUserConverter<UserDO, UserManageUpdateReqDTO> {

    @Override
    UserManageUpdateReqDTO to(UserDO source);

    @org.mapstruct.Mapping(
            target = "roles",
            expression = "java( BaseConvertUtils.listToString(target.getRoles()) )"
    )
    UserDO from(UserManageUpdateReqDTO target);
}