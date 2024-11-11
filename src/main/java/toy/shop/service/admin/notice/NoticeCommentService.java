package toy.shop.service.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.notice.Notice;
import toy.shop.domain.notice.NoticeComments;
import toy.shop.dto.admin.notice.comment.NoticeCommentResponseDTO;
import toy.shop.dto.member.MemberDetailResponseDTO;
import toy.shop.repository.admin.notice.NoticeCommentRepository;
import toy.shop.repository.admin.notice.NoticeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeCommentService {

    private final NoticeRepository noticeRepository;
    private final NoticeCommentRepository noticeCommentRepository;

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
}
