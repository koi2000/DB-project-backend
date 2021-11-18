package qd.cs.koi.database.interfaces.Book;

import lombok.*;
import qd.cs.koi.database.utils.entity.BaseDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookListReqDTO extends BaseDTO {
    private int pageNow;
    private int pageSize;
    private String sortBy;
    private Boolean ascending = false;
}
