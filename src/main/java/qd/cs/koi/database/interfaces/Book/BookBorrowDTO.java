package qd.cs.koi.database.interfaces.Book;

import lombok.*;
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
    Date start;

    @NotNull
    Date shouldTime;

    @NotNull
    Long bookId;
}

