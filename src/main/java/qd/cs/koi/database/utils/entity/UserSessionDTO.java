/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.utils.entity;

import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSessionDTO extends BaseDTO {

    public static final String HEADER_KEY = "UserInfo";
    public static final String HEADER_VALUE_LOGOUT = "logout";
    public static final String HEADER_KEY_USERID = "UserInfo-UserId";

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String studentId;
    private List<String> roles;
    private List<Long> groups;

    public boolean userIdEquals(Long userId) {
        return this.userId != null && this.userId.equals(userId);
    }

    public boolean userIdNotEquals(Long userId) {
        return !userIdEquals(userId);
    }

    public boolean userNotInGroup(Long groupId) {
        return !userInGroup(groupId);
    }

    public boolean userInGroup(Long groupId) {
        return groups.contains(groupId);
    }

    public boolean userInGroups(Collection<Long> groupIdList) {
        return !Collections.disjoint(groups, groupIdList);
    }
}