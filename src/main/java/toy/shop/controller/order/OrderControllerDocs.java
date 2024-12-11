package toy.shop.controller.order;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import toy.shop.dto.Response;
import toy.shop.dto.order.OrderSaveRequestDTO;

import java.io.IOException;

@Tag(name = "주문 API", description = "주문 기능에 대한 API")
public interface OrderControllerDocs {

    @Operation(summary = "상품 결제", description = "authentication 정보와 request 정보를 통해 주문")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "결제 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "결제 성공",
                        "data": "주문 번호"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "결제 실패 - 여러 이유", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 201,
                        "message": "결제 실패 - 환불 진행",
                        "data": "주문 번호"
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "결제 실패 - 배송지 및 상품 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 401,
                        "message": "존재하지 않는 배송지입니다. or 존재하지 않는 상품입니다.",
                        "data": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "결제 실패 - 사용자 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                        "status": 404,
                        "message": "존재하지 않는 사용자입니다.",
                        "data": null
                    }
                    """)))
    })
    ResponseEntity<Response<?>> paymentComplete(Authentication authentication, OrderSaveRequestDTO parameter) throws IOException;

    @Operation(summary = "결제 상세 내역 조회", description = "가맹점 식별코드를 통해 결제 상세 내역을 조회")
    IamportResponse<Payment> validateIamport(String imp_uid) throws IamportResponseException, IOException;
}
