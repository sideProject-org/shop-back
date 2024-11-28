package toy.shop.dto.item.image;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemTmpImageResponseDTO {

    private String originalName;
    private String savedPath;
}
