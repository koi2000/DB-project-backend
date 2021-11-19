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
@TableName(StorageDOField.TABLE_NAME)
public class StorageDO extends BaseDO {
    @TableId(value = StorageDOField.ID, type = IdType.AUTO)
    private Long storageId;

    @TableField(value = StorageDOField.GMT_CREATE, fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(value = StorageDOField.GMT_MODIFIED, fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(StorageDOField.FEATURES)
    private String features;

    @TableField(StorageDOField.DELETED)
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(StorageDOField.VERSION)
    @Version
    private Integer version;

    @TableField(StorageDOField.BOOKID)
    private Long bookId;

    @TableField(StorageDOField.NUMBER)
    private Integer number;
}
