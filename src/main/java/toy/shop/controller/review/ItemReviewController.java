package toy.shop.controller.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.review.ItemReviewResponseDTO;
import toy.shop.dto.review.ItemReviewSaveRequestDTO;
import toy.shop.dto.review.ItemReviewUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.review.ItemReviewService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itemReviews")
public class ItemReviewController implements ItemReviewControllerDocs {

    private final ItemReviewService itemReviewService;

    @GetMapping("/{itemId}")
    public ResponseEntity<Response<?>> itemReviewList(@PathVariable("itemId") Long itemId, @PageableDefault(size = 10) Pageable pageable) {
        Page<ItemReviewResponseDTO> result = itemReviewService.itemReviewList(itemId, pageable);

        return buildResponse(HttpStatus.OK, "상품 후기 목록 조회 성공", result);
    }

    @PostMapping
    public ResponseEntity<Response<?>> registerItemReview(@ModelAttribute @Valid ItemReviewSaveRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemReviewService.registerItemReview(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "상품 후기 등록 성공", result);
    }

    @PutMapping
    public ResponseEntity<Response<?>> updateItemReview(@ModelAttribute @Valid ItemReviewUpdateRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemReviewService.updateItemReview(parameter, userDetails);

        return buildResponse(HttpStatus.OK, "상품 후기 수정 성공", result);
    }

    @DeleteMapping("/{itemReviewId}")
    public ResponseEntity<Response<?>> deleteItemReview(@PathVariable("itemReviewId") Long itemReviewId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        itemReviewService.deleteItemReview(itemReviewId, userDetails);

        return buildResponse(HttpStatus.OK, "상품 후기 삭제 성공", null);
    }
}
