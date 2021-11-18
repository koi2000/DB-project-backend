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
@TableName(BookDOField.TABLE_NAME)
public class BookDO extends BaseDO {
    @TableId(value = BookDOField.ID, type = IdType.AUTO)
    private Long bookId;

    @TableField(value = BookDOField.GMT_CREATE, fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(value = BookDOField.GMT_MODIFIED, fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(BookDOField.FEATURES)
    private String features;

    @TableField(BookDOField.DELETED)
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(BookDOField.VERSION)
    @Version
    private Integer version;

    @TableField(BookDOField.BOOKNAME)
    private String bookName;

    @TableField(BookDOField.AUTHOR)
    private String author;

    @TableField(BookDOField.DESCRIPTION)
    private String description;

    @TableField(BookDOField.CLASSIFICATION)
    private String classification;

    @TableField(BookDOField.KEYWORD)
    private String keyWord;

    @TableField(BookDOField.IMG)
    private String img;

    @TableField(BookDOField.PRICE)
    private int price;
}
