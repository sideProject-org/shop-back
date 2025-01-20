package toy.shop.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.domain.item.ItemImage;
import toy.shop.domain.item.Wish;
import toy.shop.domain.member.Member;
import toy.shop.dto.item.ItemListResponseDTO;
import toy.shop.dto.item.WishSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.item.ItemImageRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.item.WishRepository;
import toy.shop.repository.member.MemberRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

    /**
     * 현재 사용자의 위시리스트를 조회하여 ItemListResponseDTO 객체 리스트로 변환합니다.
     * 각 DTO는 아이템의 상세 정보와 해당 아이템의 이미지 경로 리스트를 포함합니다.
     *
     * <p>이 메서드는 다음과 같은 작업을 수행합니다:</p>
     * <ol>
     *     <li>사용자의 userId를 기반으로 위시리스트를 조회합니다.</li>
     *     <li>위시리스트에서 아이템의 ID를 추출합니다.</li>
     *     <li>추출된 아이템 ID를 기준으로 해당 아이템의 이미지를 한 번의 쿼리로 조회합니다 (N+1 문제 방지).</li>
     *     <li>아이템 ID를 키로, 이미지 경로 리스트를 값으로 하는 Map을 생성합니다.</li>
     *     <li>위시리스트 데이터를 순회하며 각 Wish를 ItemListResponseDTO로 변환하고 리스트로 반환합니다.</li>
     * </ol>
     *
     * @param userDetails 현재 사용자의 인증 정보를 포함한 {@code UserDetailsImpl} 객체
     * @return 아이템 상세 정보와 이미지 경로 리스트를 포함하는 {@code ItemListResponseDTO} 리스트
     */
    public List<ItemListResponseDTO> wishList(UserDetailsImpl userDetails) {
        // Wish 리스트 조회
        List<Wish> wishList = wishRepository.findByMember_Id(userDetails.getUserId());

        // Item IDs 추출
        List<Long> itemIds = wishList.stream()
                .map(wish -> wish.getItem().getId())
                .collect(Collectors.toList());

        // ItemImages 조회 (한 번에 로드)
        List<ItemImage> itemImages = itemImageRepository.findFirstImageByItemIds(itemIds);

        // Item ID를 Key로, 이미지 경로 리스트를 Value로 하는 Map 생성
        Map<Long, String> itemImagesMap = itemImages.stream()
                .collect(Collectors.toMap(
                        itemImage -> itemImage.getItem().getId(),
                        ItemImage::getImagePath
                ));

        // Wish 리스트를 ItemListResponseDTO로 변환
        return wishList.stream()
                .map(wish -> ItemListResponseDTO.builder()
                        .id(wish.getItem().getId())
                        .name(wish.getItem().getName())
                        .price(wish.getItem().getPrice())
                        .sale(wish.getItem().getSale())
                        .itemImage(itemImagesMap.getOrDefault(wish.getItem().getId(), null))
                        .build()
                ).collect(Collectors.toList());
    }

    /**
     * 관심 상품 등록 메서드.
     *
     * 주어진 사용자와 상품 정보를 기반으로 관심 상품(Wish)을 등록합니다.
     * 등록된 관심 상품의 고유 ID를 반환합니다.
     *
     * @param parameter    관심 상품 등록 요청 데이터를 담고 있는 {@link WishSaveRequestDTO}.
     * @param userDetails  현재 로그인한 사용자의 정보를 담고 있는 {@link UserDetailsImpl}.
     * @return             등록된 관심 상품의 고유 ID.
     * @throws UsernameNotFoundException   사용자가 존재하지 않을 경우 발생.
     * @throws NotFoundException           상품이 존재하지 않을 경우 발생.
     */
    public Long registerWish(WishSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        Item item = itemRepository.findById(parameter.getItemId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

        Wish wish = Wish.builder()
                .item(item)
                .member(member)
                .build();

        Wish savedWish = wishRepository.save(wish);

        return savedWish.getId();
    }

    /**
     * 관심 상품 삭제 메서드.
     *
     * 주어진 관심 상품 ID와 사용자 정보를 기반으로 관심 상품(Wish)을 삭제합니다.
     *
     * @param wishId       삭제할 관심 상품의 고유 ID.
     * @param userDetails  현재 로그인한 사용자의 정보를 담고 있는 {@link UserDetailsImpl}.
     * @throws NotFoundException   관심 상품이 존재하지 않거나 해당 사용자의 관심 상품이 아닐 경우 발생.
     */
    public void deleteWish(Long wishId, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        Wish wish = wishRepository.findByIdAndMember(wishId, member)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 찜 상품입니다."));

        if (!wish.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("로그인된 사용자의 찜 상품이 아닙니다.");
        }

        wishRepository.delete(wish);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }
}
