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



import org.apache.commons.lang3.builder.ToStringBuilder;
import qd.cs.koi.database.utils.web.NoNullFieldStringStyle;

import java.io.Serializable;

/**
 * 所有模型的基类
 */
public class BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, new NoNullFieldStringStyle());
    }
}