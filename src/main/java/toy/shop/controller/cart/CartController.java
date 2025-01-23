package toy.shop.controller.cart;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.cart.CartResponseDTO;
import toy.shop.dto.cart.CartSaveRequestDTO;
import toy.shop.dto.cart.CartUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.cart.CartService;

import java.util.List;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController implements CartControllerDocs {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Response<?>> cartList(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<CartResponseDTO> result = cartService.cartList(userDetails);

        return buildResponse(HttpStatus.OK, "장바구니 목록 조회 성공", result);
    }

    @PostMapping
    public ResponseEntity<Response<?>> addCart(@RequestBody @Valid CartSaveRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = cartService.saveCart(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "장바구니 담기 성공", result);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Response<?>> updateCart(@PathVariable("cartId") Long cartId, @RequestBody @Valid CartUpdateRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = cartService.updateCart(cartId, parameter, userDetails);

        return buildResponse(HttpStatus.OK, "장바구니 수량 변경 성공", result);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Response<?>> deleteCart(@PathVariable("cartId") Long cartId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        cartService.deleteCart(cartId, userDetails);

        return buildResponse(HttpStatus.OK, "장바구니 삭제 성공", null);
    }
}
