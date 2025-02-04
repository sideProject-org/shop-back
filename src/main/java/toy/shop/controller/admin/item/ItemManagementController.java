package toy.shop.controller.admin.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.dto.Response;
import toy.shop.service.admin.item.ItemManagementService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/items")
public class ItemManagementController implements ItemManagementControllerDocs {

    private final ItemManagementService itemManagementService;

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Response<?>> deleteItem(@PathVariable("itemId") Long itemId) {
        itemManagementService.deleteItem(itemId);

        return buildResponse(HttpStatus.OK, "상품 삭제 성공", null);
    }
}
