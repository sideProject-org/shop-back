package toy.shop.controller.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.review.ItemReviewSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.review.ItemReviewService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itemReviews")
public class ItemReviewController implements ItemReviewControllerDocs {

    private final ItemReviewService itemReviewService;

    @PostMapping
    public ResponseEntity<Response<?>> registerItemReview(@ModelAttribute @Valid ItemReviewSaveRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemReviewService.registerItemReview(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "상품 후기 등록 성공", result);
    }
}
