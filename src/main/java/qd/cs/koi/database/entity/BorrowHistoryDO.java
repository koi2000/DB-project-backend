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
@TableName(BorrowHistoryDOField.TABLE_NAME)
public class BorrowHistoryDO extends BaseDO {
    @TableId(value = BorrowHistoryDOField.ID, type = IdType.AUTO)
    private Long borrowHistoryId;

    @TableField(value = BorrowHistoryDOField.GMT_CREATE, fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(value = BorrowHistoryDOField.GMT_MODIFIED, fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(BorrowHistoryDOField.FEATURES)
    private String features;

    @TableField(BorrowHistoryDOField.DELETED)
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(BorrowHistoryDOField.VERSION)
    @Version
    private Integer version;

    @TableField(BorrowHistoryDOField.USERNAME)
    private String username;

    @TableField(BorrowHistoryDOField.GMT_START)
    private Date start;

    @TableField(BorrowHistoryDOField.GMT_FAKEEND)
    private Date shouldTime;

    @TableField(BorrowHistoryDOField.GMT_REALEND)
    private Date realTime;

    @TableField(BorrowHistoryDOField.BOOKID)
    private Long bookId;
}
