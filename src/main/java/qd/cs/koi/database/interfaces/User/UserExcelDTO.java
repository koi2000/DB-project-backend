package qd.cs.koi.database.interfaces.User;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExcelDTO {

    @Pattern(regexp = "^[A-Za-z0-9_]{4,16}$", message = "用户名必须由英文、数字、'_'构成，且长度为4~16")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    @ExcelProperty("用户名")
    private String username;

    @Length(max = 30, message = "昵称长度不合法，比如在30位之内")
    @ApiModelProperty(value = "昵称")
    @ExcelProperty("昵称")
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Length(min = 4, max = 32, message = "密码长度必须在4-32位之间")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    @ExcelProperty("密码")
    private String password;

    @Email(message = "邮箱不合法")
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Length(min = 11, max = 16, message = "手机号码长度不合法")
    @ApiModelProperty(value = "电话")
    @ExcelProperty("电话")
    private String phone;

    @Range(min = 0, max = 2, message = "性别不合法, 0.女, 1.男, 2.问号")
    @ApiModelProperty(value = "性别")
    @ExcelProperty("性别")
    private Integer gender;
}


