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
@TableName(MessageDOField.TABLE_NAME)
public class MessageDO extends BaseDO {

    @TableId(value = MessageDOField.ID, type = IdType.NONE)
    private Long id;

    @TableField(value = MessageDOField.GMT_CREATE, fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(value = MessageDOField.GMT_MODIFIED, fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(MessageDOField.FEATURES)
    private String features;

    @TableField(MessageDOField.DELETED)
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(MessageDOField.VERSION)
    @Version
    private Integer version;

    @TableField(MessageDOField.TO)
    private String toUser;

    @TableField(MessageDOField.FROM)
    private String fromUser;

    @TableField(MessageDOField.TEXT)
    private String text;

}
