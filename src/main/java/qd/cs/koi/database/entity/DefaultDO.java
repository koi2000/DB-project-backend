package qd.cs.koi.database.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import qd.cs.koi.database.utils.entity.BaseDO;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(DefaultDOField.TABLE_NAME)
public class DefaultDO extends BaseDO {

    @TableId(value = DefaultDOField.ID, type = IdType.AUTO)
    private Long defaultId;

    @TableField(value = DefaultDOField.GMT_CREATE, fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(value = DefaultDOField.GMT_MODIFIED, fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(DefaultDOField.FEATURES)
    private String features;

    @TableField(DefaultDOField.DELETED)
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(DefaultDOField.VERSION)
    @Version
    private Integer version;

    @TableField(DefaultDOField.USERNAME)
    private String userName;

    @TableField(DefaultDOField.TIME)
    private Date time;

    @TableField(DefaultDOField.REASON)
    private String reason;

    @TableField(DefaultDOField.VERSION)
    private Integer price;
}
