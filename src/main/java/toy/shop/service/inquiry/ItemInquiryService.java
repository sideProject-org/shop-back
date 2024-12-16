package toy.shop.service.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.inquiry.ItemInquiry;
import toy.shop.domain.item.Item;
import toy.shop.domain.member.Member;
import toy.shop.dto.inquiry.ItemInquiryRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.inquiry.ItemInquiryRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class ItemInquiryService {

    private final ItemRepository itemRepository;
    private final ItemInquiryRepository itemInquiryRepository;
    private final MemberRepository memberRepository;

    /**
     * 상품 문의를 등록합니다.
     *
     * @param parameter 상품 ID, 문의 제목과 내용을 담은 {@link ItemInquiryRequestDTO}
     * @param userDetails 현재 로그인한 사용자 정보 {@link UserDetailsImpl}
     * @return 생성된 상품 문의의 ID
     */
    public Long registerItemInquiry(ItemInquiryRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        Item item = getItem(parameter.getItemId());

        ItemInquiry itemInquiry = ItemInquiry.builder()
                .item(item)
                .member(member)
                .title(parameter.getTitle())
                .content(parameter.getContent())
                .build();
        itemInquiryRepository.save(itemInquiry);

        return itemInquiry.getId();
    }

    /**
     * 상품 문의를 수정합니다.
     *
     * @param parameter 수정할 제목과 내용을 담은 {@link ItemInquiryRequestDTO}
     * @param itemInquiryId 수정할 상품 문의의 ID
     * @param userDetails 현재 로그인한 사용자 정보 {@link UserDetailsImpl}
     * @return 수정된 상품 문의의 ID
     * @throws AccessDeniedException 로그인한 사용자가 작성한 문의가 아닐 경우
     */
    @Transactional
    public Long updateItemInquiry(ItemInquiryRequestDTO parameter, Long itemInquiryId, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        ItemInquiry itemInquiry = getItemInquiry(itemInquiryId);

        if (!itemInquiry.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("로그인된 사용자의 문의글이 아닙니다.");
        }

        itemInquiry.updateInquiry(parameter.getTitle(), parameter.getContent());

        return itemInquiry.getId();
    }

    /**
     * 상품 문의를 삭제합니다.
     *
     * @param itemInquiryId 삭제할 상품 문의의 ID
     * @param userDetails 현재 로그인한 사용자 정보 {@link UserDetailsImpl}
     * @throws AccessDeniedException 로그인한 사용자가 작성한 문의가 아닐 경우
     */
    public void deleteItemInquiry(Long itemInquiryId, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        ItemInquiry itemInquiry = getItemInquiry(itemInquiryId);

        if (!itemInquiry.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("로그인된 사용자의 문의글이 아닙니다.");
        }

        itemInquiryRepository.delete(itemInquiry);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));
    }

    private ItemInquiry getItemInquiry(Long itemInquiryId) {
        return itemInquiryRepository.findById(itemInquiryId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 문의글입니다."));
    }
}
