package toy.shop.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.domain.member.Member;
import toy.shop.domain.order.Order;
import toy.shop.domain.review.ItemReview;
import toy.shop.domain.review.ItemReviewImage;
import toy.shop.dto.review.ItemReviewSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.MemberRepository;
import toy.shop.repository.order.OrderRepository;
import toy.shop.repository.review.ItemReviewImageRepository;
import toy.shop.repository.review.ItemReviewRepository;
import toy.shop.service.FileService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemReviewService {

    private final ItemReviewRepository itemReviewRepository;
    private final ItemReviewImageRepository itemReviewImageRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    private final FileService fileService;

    @Value("${path.reviewImage}")
    private String location;

    private final String resourceHandlerReviewURL = "/images/reviewImage/";

    /**
     * 구매한 상품에 대한 리뷰를 등록합니다.
     *
     * <p>이 메서드는 다음 작업을 수행합니다:
     * <ul>
     *     <li>제공된 사용자 정보를 기반으로 사용자를 검증합니다.</li>
     *     <li>사용자가 해당 상품을 구매했는지 확인합니다.</li>
     *     <li>리뷰 대상 상품의 존재 여부를 확인합니다.</li>
     *     <li>상품 리뷰 정보를 저장합니다.</li>
     *     <li>리뷰에 첨부된 이미지를 저장합니다.</li>
     * </ul>
     *
     * @param parameter 상품 리뷰 등록 요청 데이터 객체. 제목, 내용, 평점, 첨부 이미지 등을 포함합니다.
     * @param userDetails 현재 로그인한 사용자의 정보를 포함한 객체.
     * @return 저장된 상품 리뷰의 ID.
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 발생합니다.
     * @throws AccessDeniedException 사용자가 해당 상품을 구매하지 않았을 경우 발생합니다.
     * @throws NotFoundException 리뷰 대상 상품이 존재하지 않을 경우 발생합니다.
     * @throws RuntimeException 이미지 업로드에 실패했을 경우 발생합니다.
     */
    @Transactional
    public Long registerItemReview(ItemReviewSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        // 1. 사용자 검증
        Member member = getMember(userDetails.getUserId());

        // 2. 상품 구매 확인
        verifyItemPurchase(member.getId(), parameter.getItemId());

        // 3. 상품 검증
        Item item = getItem(parameter.getItemId());

        // 4. 상품 후기 저장
        ItemReview itemReview = saveItemReview(member, item, parameter);

        // 5. 상품 후기 이미지 저장
        saveItemReviewImages(parameter.getItemReviewImages(), itemReview);

        return itemReview.getId();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));
    }

    private void verifyItemPurchase(Long memberId, Long itemId) {
        List<Order> orders = orderRepository.findOrdersByMemberId(memberId);
        boolean isBuy = orders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .anyMatch(orderDetail -> orderDetail.getItem().getId().equals(itemId));

        if (!isBuy) {
            throw new AccessDeniedException("상품을 구매하지 않은 사용자입니다.");
        }
    }

    private ItemReview saveItemReview(Member member, Item item, ItemReviewSaveRequestDTO parameter) {
        ItemReview itemReview = ItemReview.builder()
                .member(member)
                .item(item)
                .title(parameter.getTitle())
                .content(parameter.getContent())
                .rate(parameter.getRate())
                .build();

        return itemReviewRepository.save(itemReview);
    }

    private void saveItemReviewImages(List<MultipartFile> itemReviewImages, ItemReview itemReview) {
        for (MultipartFile file : itemReviewImages) {
            try {
                String imgName = fileService.uploadFile(location, file.getOriginalFilename(), file.getBytes());
                String imgUrl = resourceHandlerReviewURL + imgName;

                ItemReviewImage reviewImage = ItemReviewImage.builder()
                        .itemReview(itemReview)
                        .imagePath(imgUrl)
                        .build();

                itemReviewImageRepository.save(reviewImage);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드에 실패하였습니다.", e);
            }
        }
    }
}
