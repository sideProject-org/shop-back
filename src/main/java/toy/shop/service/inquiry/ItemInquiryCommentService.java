package toy.shop.service.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.inquiry.ItemInquiry;
import toy.shop.domain.inquiry.ItemInquiryComment;
import toy.shop.domain.member.Member;
import toy.shop.dto.inquiry.ItemInquiryCommentResponseDTO;
import toy.shop.dto.inquiry.ItemInquiryCommentSaveRequestDTO;
import toy.shop.dto.inquiry.ItemInquiryCommentUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.inquiry.ItemInquiryCommentRepository;
import toy.shop.repository.inquiry.ItemInquiryRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class ItemInquiryCommentService {

    private final ItemInquiryRepository itemInquiryRepository;
    private final ItemInquiryCommentRepository itemInquiryCommentRepository;
    private final MemberRepository memberRepository;

    public ItemInquiryCommentResponseDTO itemInquiryComment(Long itemInquiryId) {
        itemInquiryCommentRepository.findByItemInquiry_Id(itemInquiryId)
                .orElseThrow(() -> new NotFoundException("해당 문의의 답변이 존재하지 않습니다."));
    }

    /**
     * 상품 문의에 대한 댓글을 등록하고 해당 문의를 답변 완료 상태로 변경합니다.
     *
     * @param parameter    댓글 등록에 필요한 요청 데이터를 담고 있는 DTO
     * @param userDetails  현재 로그인한 사용자 정보를 담고 있는 객체
     * @return 등록된 댓글의 ID
     * @throws NotFoundException     존재하지 않는 상품 문의일 경우 발생
     * @throws AccessDeniedException 현재 사용자가 해당 상품의 판매자가 아닐 경우 발생
     * @throws ConflictException     이미 답변 완료된 문의일 경우 발생
     */
    @Transactional
    public Long registerComment(ItemInquiryCommentSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        ItemInquiry itemInquiry = itemInquiryRepository.findById(parameter.getItemInquiryId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품 문의입니다."));

        if (!itemInquiry.getItem().getMember().equals(member)) {
            throw new AccessDeniedException("해당 상품의 판매자가 아닙니다.");
        }

        if (itemInquiry.getAnswerStatus() == '1') {
            throw new ConflictException("이미 답변 완료된 문의입니다.");
        }

        ItemInquiryComment itemInquiryComment = ItemInquiryComment.builder()
                .member(member)
                .itemInquiry(itemInquiry)
                .content(parameter.getContent())
                .build();
        ItemInquiryComment savedItemInquiryComment = itemInquiryCommentRepository.save(itemInquiryComment);

        itemInquiry.updateAnswer('1');

        return savedItemInquiryComment.getId();
    }

    /**
     * 상품 문의에 대한 댓글을 수정합니다.
     *
     * @param parameter    댓글 수정을 위한 요청 데이터를 담고 있는 DTO
     * @param commentId    수정할 댓글의 ID
     * @param userDetails  현재 로그인한 사용자 정보를 담고 있는 객체
     * @return 수정된 댓글의 ID
     * @throws NotFoundException     존재하지 않는 댓글일 경우 발생
     * @throws AccessDeniedException 현재 사용자가 해당 댓글의 작성자가 아닐 경우 발생
     */
    @Transactional
    public Long updateComment(ItemInquiryCommentUpdateRequestDTO parameter, Long commentId, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        ItemInquiryComment itemInquiryComment = itemInquiryCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품 문의 답변입니다."));

        if (!itemInquiryComment.getMember().equals(member)) {
            throw new AccessDeniedException("로그인된 사용자의 답변이 아닙니다.");
        }

        itemInquiryComment.updateContent(parameter.getContent());

        return itemInquiryComment.getId();
    }

    @Transactional
    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        ItemInquiryComment itemInquiryComment = itemInquiryCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품 문의 답변입니다."));

        if (!itemInquiryComment.getMember().equals(member)) {
            throw new AccessDeniedException("로그인된 사용자의 답변이 아닙니다.");
        }

        itemInquiryCommentRepository.delete(itemInquiryComment);
        itemInquiryRepository.findById(itemInquiryComment.getItemInquiry().getId())
                .ifPresent(itemInquiry -> {itemInquiry.updateAnswer('0');});
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }
}
