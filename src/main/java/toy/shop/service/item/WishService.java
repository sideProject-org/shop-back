package toy.shop.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.domain.item.Wish;
import toy.shop.domain.member.Member;
import toy.shop.dto.item.WishSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.item.WishRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class WishService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

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
