package toy.shop.service.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.member.Member;
import toy.shop.domain.notice.Notice;
import toy.shop.domain.notice.NoticeComments;
import toy.shop.dto.admin.notice.comment.NoticeCommentRequestDTO;
import toy.shop.dto.admin.notice.comment.NoticeCommentResponseDTO;
import toy.shop.dto.member.MemberDetailResponseDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.admin.notice.NoticeCommentRepository;
import toy.shop.repository.admin.notice.NoticeRepository;
import toy.shop.repository.member.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeCommentService {

    private final NoticeRepository noticeRepository;
    private final NoticeCommentRepository noticeCommentRepository;
    private final MemberRepository memberRepository;

    /**
     * 주어진 공지사항 ID에 해당하는 댓글 목록을 조회하고, DTO 형태로 변환하여 반환합니다.
     *
     * @param noticeId 댓글을 조회할 공지사항의 ID
     * @return 공지사항 ID에 해당하는 댓글 목록을 {@link NoticeCommentResponseDTO} 형태로 반환
     * @throws NotFoundException 공지사항이 존재하지 않거나, 해당 공지사항에 댓글이 없는 경우 발생하는 예외
     */
    public List<NoticeCommentResponseDTO> noticeCommentList(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지사항이 존재하지 않습니다."));

        List<NoticeComments> comments = noticeCommentRepository.findByNoticeId(notice.getId());
        if (comments.isEmpty()) {
            throw new NotFoundException("댓글이 존재하지 않습니다.");
        }

        List<NoticeCommentResponseDTO> result = comments.stream()
                .map(comment -> NoticeCommentResponseDTO.builder()
                        .id(comment.getId())
                        .comment(comment.getComment())
                        .member(
                                MemberDetailResponseDTO.builder()
                                        .id(comment.getMember().getId())
                                        .email(comment.getMember().getEmail())
                                        .nickName(comment.getMember().getNickName())
                                        .role(comment.getMember().getRole())
                                        .build()
                        )
                        .build())
                .collect(Collectors.toList());

        return result;
    }

    /**
     * 주어진 공지사항 ID와 사용자 정보를 기반으로 댓글을 저장합니다.
     *
     * @param parameter 댓글 작성에 필요한 정보가 담긴 {@link NoticeCommentRequestDTO} 객체
     * @param noticeId 댓글을 작성할 공지사항의 ID
     * @param userDetails 현재 로그인한 사용자의 정보를 담고 있는 {@link UserDetailsImpl} 객체
     * @return 생성된 댓글의 ID
     * @throws NotFoundException 공지사항이 존재하지 않을 경우 발생하는 예외
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 발생하는 예외
     */
    public Long saveNoticeComment(NoticeCommentRequestDTO parameter, Long noticeId, UserDetailsImpl userDetails) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지사항이 존재하지 않습니다."));

        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 아이디입니다."));

        NoticeComments result = NoticeComments.builder()
                .notice(notice)
                .member(member)
                .comment(parameter.getComment())
                .build();

        noticeCommentRepository.save(result);

        return result.getId();
    }
}
