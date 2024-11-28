package toy.shop.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.dto.Response;
import toy.shop.dto.item.ItemRequestDTO;
import toy.shop.dto.item.image.ItemTmpImageResponseDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.item.ItemImageService;
import toy.shop.service.item.ItemService;

import java.util.List;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController implements ItemControllerDocs {

    private final ItemService itemService;
    private final ItemImageService itemImageService;

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

    @PostMapping("/images/tmp")
    public ResponseEntity<Response<?>> saveTmpImage(@RequestParam("files") List<MultipartFile> files) {
        if(files.isEmpty()) {
            return buildResponse(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다.", null);
        }

        List<ItemTmpImageResponseDTO> result = itemImageService.saveTemporaryItemImage(files);

        return buildResponse(HttpStatus.OK, "임시 이미지 업로드 성공", result);
    }
}
