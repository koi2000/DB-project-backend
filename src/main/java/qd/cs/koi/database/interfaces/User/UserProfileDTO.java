package qd.cs.koi.database.interfaces.User;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import qd.cs.koi.database.utils.entity.BaseDTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserProfileDTO extends BaseDTO {

    @Pattern(regexp = "^[A-Za-z0-9_]{4,16}$", message = "用户名必须由英文、数字、'_'构成，且长度为4~16")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Length(max = 30, message = "昵称长度不合法，比如在30位之内")
    private String nickname;

    @Email(message = "邮箱不合法")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Length(min = 11, max = 16, message = "手机号码长度不合法")
    private String phone;

    @Range(min = 0, max = 2, message = "性别不合法, 0.女, 1.男, 2.问号")
    private Integer gender;

    private List<String> roles;

    private int credits;
}
