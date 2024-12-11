package toy.shop.controller.order;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.order.OrderSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.order.OrderService;

import java.io.IOException;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController implements OrderControllerDocs {

    private IamportClient iamportClient;

    private final OrderService orderService;

    @Value("${imp.key}")
    private String impKey;

    @Value("${imp.secret}")
    private String impSecret;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(impKey, impSecret);
    }

    @PostMapping("/payment")
    public ResponseEntity<Response<?>> paymentComplete(Authentication authentication, @RequestBody @Valid OrderSaveRequestDTO parameter) throws IOException {
        String orderNumber = parameter.getOrderNumber();
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            orderService.saveOrder(parameter, userDetails);

            return buildResponse(HttpStatus.CREATED, "결제 성공", orderNumber);
        } catch (RuntimeException e) {
            String token = orderService.getToken(impKey, impSecret);
            orderService.refundRequest(token, orderNumber, e.getMessage());
            return buildResponse(HttpStatus.BAD_REQUEST, "결제 실패 - 환불 진행", orderNumber);
        }
    }

    @PostMapping("/payment/validation/{imp_uid}")
    public IamportResponse<Payment> validateIamport(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());
        return payment;
    }
}
