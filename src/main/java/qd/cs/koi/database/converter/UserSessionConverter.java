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



import qd.cs.koi.database.entity.UserDO;
import qd.cs.koi.database.utils.entity.UserSessionDTO;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface UserSessionConverter extends BaseConverter<UserDO, UserSessionDTO> {

    default UserSessionDTO to(UserDO userDO) {
        return UserSessionDTO.builder()
                .userId(userDO.getUserId())
                .username(userDO.getUsername())
                .nickname(userDO.getNickname())
                .email(userDO.getEmail())
                .roles(stringToList(userDO.getRoles()))
                .build();
    }

}