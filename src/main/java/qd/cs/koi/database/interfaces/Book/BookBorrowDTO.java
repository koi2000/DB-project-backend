package qd.cs.koi.database.interfaces.Book;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import qd.cs.koi.database.utils.entity.BaseDTO;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookBorrowDTO extends BaseDTO {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    Date start;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    Date shouldTime;

    @NotNull
    Long bookId;
}

