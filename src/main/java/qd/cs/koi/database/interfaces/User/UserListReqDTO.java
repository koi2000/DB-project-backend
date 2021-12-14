/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.interfaces.User;


import lombok.*;
import qd.cs.koi.database.utils.entity.BaseDTO;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserListReqDTO extends BaseDTO {
    private int pageNow;
    private int pageSize;

    private String username;
    private String nickname;
    private String studentId;
    private String phone;
    private String email;
    private String sduId;

    private String searchKey;
}