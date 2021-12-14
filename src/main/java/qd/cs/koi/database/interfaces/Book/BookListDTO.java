package qd.cs.koi.database.interfaces.Book;

import lombok.*;
import qd.cs.koi.database.utils.entity.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookListDTO extends BaseDTO {

    @NotNull
    private Long bookId;

    @NotBlank(message = "书籍名不能为空")
    private String bookName;

    @NotBlank(message = "作者不能为空")
    private String author;

    private List<String> keyWord;

    private String description;

    private String img;

}
