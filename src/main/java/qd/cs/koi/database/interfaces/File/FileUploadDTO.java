package qd.cs.koi.database.interfaces.File;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import qd.cs.koi.database.utils.entity.BaseDTO;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileUploadDTO extends BaseDTO {
    @NotNull
    private Long bookId;

    @NotNull
    private MultipartFile file;
}
