package qd.cs.koi.database.interfaces.Book;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookExcelDTO {

    @ApiModelProperty(value = "书籍名称，不能为空")
    @ExcelProperty("书籍名称，不能为空")
    private String bookname;

    @ApiModelProperty(value = "作者，不能为空")
    @ExcelProperty("作者，不能为空")
    private String author;

    @ApiModelProperty(value = "书籍描述")
    @ExcelProperty("书籍描述")
    private String description;

    @ApiModelProperty(value = "关键词，以逗号或空格分隔")
    @ExcelProperty("关键词，以逗号或空格分隔")
    private String keyWord;

    @ApiModelProperty(value = "书籍分类，以逗号或者空格分隔")
    @ExcelProperty("书籍分类，已逗号或者空格分隔")
    private String classification;

    @ApiModelProperty(value = "价格")
    @ExcelProperty("价格")
    private Integer price;

//    @ApiModelProperty(value = "数量")
//    @ExcelProperty("数量")
//    @Min(0)
//    private Integer number;
}
