package toy.shop.controller.inquiry;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.inquiry.ItemInquiryRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.inquiry.ItemInquiryService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itemInquiries")
public class ItemInquiryController implements ItemInquiryControllerDocs {

    private final ItemInquiryService itemInquiryService;

    @PostMapping
    public ResponseEntity<Response<?>> registerItemInquiry(@RequestBody @Valid ItemInquiryRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemInquiryService.registerItemInquiry(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "상품 문의 등록 성공", result);
    }

    @PutMapping("/{itemInquiryId}")
    public ResponseEntity<Response<?>> updateItemInquiry(
            @PathVariable("itemInquiryId") Long itemInquiryId,
            @RequestBody @Valid ItemInquiryRequestDTO parameter,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = itemInquiryService.updateItemInquiry(parameter, itemInquiryId, userDetails);

        return buildResponse(HttpStatus.OK, "상품 문의 수정 성공", result);
    }

    @DeleteMapping("/{itemInquiryId}")
    public ResponseEntity<Response<?>> deleteItemInquiry(@PathVariable("itemInquiryId") Long itemInquiryId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        itemInquiryService.deleteItemInquiry(itemInquiryId, userDetails);

        return buildResponse(HttpStatus.OK, "상품 문의 삭제 성공", null);
    }
}
