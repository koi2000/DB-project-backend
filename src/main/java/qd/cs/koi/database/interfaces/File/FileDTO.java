/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.interfaces.File;


import lombok.*;
import qd.cs.koi.database.utils.entity.BaseDTO;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileDTO extends BaseDTO {

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Long size;

    private String name;

    private String extensionName;

    private String md5;
}
