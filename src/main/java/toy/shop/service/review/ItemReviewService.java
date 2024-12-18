package toy.shop.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.domain.member.Member;
import toy.shop.dto.review.ItemReviewSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.member.MemberRepository;
import toy.shop.repository.review.ItemReviewRepository;

@Service
@RequiredArgsConstructor
public class ItemReviewService {

    private final ItemReviewRepository itemReviewRepository;
    private final MemberRepository memberRepository;

    public Long registerItemReview(ItemReviewSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        // TODO:
        // 로그인된 유저가 해당 상품을 구매했어야 함
        Member member = getMember(userDetails.getUserId());

    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }
}
