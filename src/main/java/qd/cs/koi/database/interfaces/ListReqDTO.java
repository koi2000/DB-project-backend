package qd.cs.koi.database.interfaces;

import lombok.*;
import qd.cs.koi.database.utils.entity.BaseDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListReqDTO extends BaseDTO {
    private int pageNow;
    private int pageSize;
    private String sortBy;
    private Boolean ascending = false;
    private String type;
}
