package qd.cs.koi.database.interfaces;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import qd.cs.koi.database.utils.annations.ApiResponseBody;

@Data
public class TeacherExcel {

    @ApiModelProperty(value = "讲师姓名")
    @ExcelProperty("讲师姓名")
    private String name;

    @ApiModelProperty(value = "讲师简介")
    @ExcelProperty("讲师简介")
    private String intro;

    @ApiModelProperty(value = "讲师资历，一句话讲明讲师")
    @ExcelProperty("讲师资历，一句话讲明讲师")
    private String career;
}
