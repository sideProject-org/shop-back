package toy.shop.controller.cart;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.dto.Response;
import toy.shop.dto.cart.CartSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.cart.CartService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController implements CartControllerDocs {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Response<?>> addCart(@RequestBody @Valid CartSaveRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = cartService.saveCart(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "장바구니 담기 성공", result);
    }
}
