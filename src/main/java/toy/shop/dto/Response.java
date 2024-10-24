package toy.shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "공통 Response 데이터")
public class Response<T> {

    @Schema(description = "Http status 값")
    private int status;

    @Schema(description = "Message")
    private String message;

    @Schema(description = "데이터")
    private T data;
}
