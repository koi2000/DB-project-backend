/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.utils.Enums;


import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import qd.cs.koi.database.utils.entity.UserSessionDTO;

import java.util.List;

@AllArgsConstructor
public enum DefaultEnum {

    OUTOFTIME("outoftime"),

    BROWKEN("browken"),
    ;

    public String name;

}
