package toy.shop.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.item.ItemListResponseDTO;
import toy.shop.dto.item.WishSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.item.WishService;

import java.util.List;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
public class WishController implements WishControllerDocs {

    private final WishService wishService;

    @GetMapping
    public ResponseEntity<Response<?>> wishList(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<ItemListResponseDTO> result = wishService.wishList(userDetails);

        return buildResponse(HttpStatus.OK, "찜 목록 조회 성공", result);
    }

    @PostMapping
    public ResponseEntity<Response<?>> registerWish(@RequestBody @Valid WishSaveRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = wishService.registerWish(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "찜 등록 성공", result);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Response<?>> deleteWish(@PathVariable("wishId") Long wishId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        wishService.deleteWish(wishId, userDetails);

        return buildResponse(HttpStatus.OK, "찜 삭제 성공", null);
    }
}
