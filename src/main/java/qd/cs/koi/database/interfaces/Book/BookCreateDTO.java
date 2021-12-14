package qd.cs.koi.database.interfaces.Book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;
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
public class BookCreateDTO extends BaseDTO {

    private Long bookId;

    @NotBlank(message = "书籍名不能为空")
    private String bookName;

    @NotBlank(message = "作者不能为空")
    private String author;

    private String description;

    private List<String> keyWord;

    private List<String> classification;

    private int price;

    private int number;

    private MultipartFile image;
}
