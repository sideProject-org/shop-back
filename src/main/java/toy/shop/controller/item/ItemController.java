package toy.shop.controller.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.dto.Response;
import toy.shop.dto.item.ItemSaveRequestDTO;
import toy.shop.service.item.ItemService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController implements ItemControllerDocs {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Response<?>> saveItem(@ModelAttribute @Valid ItemSaveRequestDTO parameter) {
        Long result = itemService.saveItem(parameter);

        return buildResponse(HttpStatus.CREATED, "상품 등록 성공", result);
    }
}
