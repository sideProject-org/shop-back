package toy.shop.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.item.ItemRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.item.ItemService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController implements ItemControllerDocs {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Response<?>> saveItem(@ModelAttribute @Valid ItemRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemService.saveItem(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "상품 등록 성공", result);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Response<?>> updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute @Valid ItemRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemService.updateItem(itemId, parameter, userDetails);

        return buildResponse(HttpStatus.OK, "상품 수정 성공", result);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Response<?>> deleteItem(@PathVariable("itemId") Long itemId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        itemService.deleteItem(itemId, userDetails);

        return buildResponse(HttpStatus.OK, "상품 삭제 성공", null);
    }
}
