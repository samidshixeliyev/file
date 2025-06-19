package ao.samid.file.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class FileResponse implements Serializable {
    private Long id;
    private Long carId;
    private String imagePath;
    private String imageName;
    private String contentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
