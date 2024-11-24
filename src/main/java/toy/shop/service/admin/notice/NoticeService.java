package toy.shop.service.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.member.Member;
import toy.shop.domain.notice.Notice;
import toy.shop.dto.admin.notice.NoticeDetailResponseDTO;
import toy.shop.dto.admin.notice.NoticeListResponseDTO;
import toy.shop.dto.admin.notice.NoticeSaveRequestDTO;
import toy.shop.dto.admin.notice.NoticeUpdateRequestDTO;
import toy.shop.dto.member.MemberDetailResponseDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.admin.notice.NoticeRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;

    private final NoticeImageService noticeImageService;

    /**
     * 공지사항 목록을 페이징 처리하여 조회합니다.
     *
     * @param pageable 페이징 정보를 포함하는 {@link Pageable} 객체
     * @return 공지사항 목록을 포함하는 {@link Page} 객체, 각 공지사항은 {@link NoticeListResponseDTO}로 변환됨
     * @throws NotFoundException 공지사항 목록이 비어 있는 경우 예외 발생
     */
    public Page<NoticeListResponseDTO> noticeList(Pageable pageable) {
        Page<Notice> list = noticeRepository.findAllWithMember(pageable);

        if (list.getContent().isEmpty()) {
            throw new NotFoundException("공지사항이 존재하지 않습니다.");
        }

        Page<NoticeListResponseDTO> result = list.map(notice -> NoticeListResponseDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .viewCnt(notice.getViewCnt())
                .member(
                        MemberDetailResponseDTO.builder()
                                .id(notice.getMember().getId())
                                .email(notice.getMember().getEmail())
                                .nickName(notice.getMember().getNickName())
                                .role(notice.getMember().getRole())
                                .build()
                )
                .build());

        return result;
    }

    /**
     * 주어진 공지사항 ID를 기반으로 공지사항의 상세 정보를 조회합니다.
     *
     * @param noticeId 조회할 공지사항의 ID
     * @return 공지사항의 상세 정보를 포함하는 {@link NoticeDetailResponseDTO} 객체
     * @throws NotFoundException 공지사항이 존재하지 않을 경우 발생하는 예외
     */
    public NoticeDetailResponseDTO noticeDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NotFoundException("공지사항이 존재하지 않습니다."));
        return NoticeDetailResponseDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCnt(notice.getViewCnt())
                .member(
                        MemberDetailResponseDTO.builder()
                                .id(notice.getMember().getId())
                                .email(notice.getMember().getEmail())
                                .nickName(notice.getMember().getNickName())
                                .role(notice.getMember().getRole())
                                .build()
                )
                .build();
    }

    /**
     * 공지사항을 저장하는 메서드입니다. 주어진 요청 정보를 기반으로 공지사항을 생성하고, 관련된 임시 이미지 URL을 메인 URL로 변환합니다.
     * 변환된 공지사항 내용과 회원 정보를 사용하여 공지사항을 저장하고, 임시 이미지 파일을 메인 저장소로 이동시킨 후
     * 이미지 정보를 데이터베이스에 저장합니다.
     *
     * @param parameter 공지사항 저장 요청 정보를 담고 있는 {@link NoticeSaveRequestDTO} 객체.
     *                  이 객체는 작성자 ID, 제목, 내용, 및 임시 이미지 URL 목록을 포함합니다.
     * @return 저장된 공지사항의 ID
     * @throws UsernameNotFoundException 제공된 사용자 ID가 존재하지 않는 경우 발생합니다.
     */
    @Transactional
    public Long saveNotice(NoticeSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 아이디입니다."));

        String updatedContent = noticeImageService.convertTemporaryUrlsToMainUrls(parameter.getContent());
        Notice notice = Notice.builder()
                .member(member)
                .title(parameter.getTitle())
                .content(updatedContent)
                .build();

        Notice savedNotice = noticeRepository.save(notice);

        noticeImageService.moveTemporaryImagesToMain(savedNotice, parameter.getTempImageUrls());

        return savedNotice.getId();
    }

    /**
     * 공지사항을 수정하는 메서드입니다.
     *
     * @param parameter  공지사항 수정을 위한 요청 데이터(DTO)입니다. 수정할 제목, 내용, 임시 이미지 URL 등이 포함됩니다.
     * @param userDetails 현재 인증된 사용자의 정보를 포함하는 객체입니다.
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 발생합니다.
     * @throws NotFoundException 공지사항이 존재하지 않을 경우 발생합니다.
     * @throws AccessDeniedException 공지사항 작성자가 아닌 사용자가 수정을 시도할 경우 발생합니다.
     *
     * <p>이 메서드는 다음과 같은 작업을 수행합니다:
     * <ul>
     *   <li>사용자 ID를 기반으로 사용자를 조회하고, 존재하지 않으면 예외를 발생시킵니다.</li>
     *   <li>공지사항 ID를 기반으로 공지사항을 조회하고, 존재하지 않으면 예외를 발생시킵니다.</li>
     *   <li>조회된 공지사항의 작성자와 현재 사용자가 일치하는지 검증합니다. 일치하지 않으면 접근 거부 예외를 발생시킵니다.</li>
     *   <li>공지사항의 내용을 업데이트하기 위해 임시 URL을 정식 URL로 변환합니다.</li>
     *   <li>공지사항의 제목과 내용을 업데이트합니다.</li>
     *   <li>임시 이미지를 정식 이미지로 이동합니다.</li>
     * </ul>
     * <p>
     * 메서드는 트랜잭션이 활성화된 상태에서 실행되며, 트랜잭션이 종료될 때 변경 사항이 커밋됩니다.
     */
    @Transactional
    public void updateNotice(NoticeUpdateRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 아이디입니다."));

        Notice notice = noticeRepository.findById(parameter.getNoticeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 공지사항 아이디입니다."));

        if (!notice.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("공지사항을 작성자가 아닙니다.");
        }

        String updatedContent = noticeImageService.convertTemporaryUrlsToMainUrls(parameter.getContent());

        notice.updateNotice(parameter.getTitle(), updatedContent);

        noticeImageService.moveTemporaryImagesToMain(notice, parameter.getTempImageUrls());
    }


    /**
     * 공지사항을 삭제하는 메서드입니다. 공지사항 ID를 기반으로 공지사항을 조회하고,
     * 해당 공지사항이 요청한 사용자의 작성인지 확인합니다. 작성자가 아닌 경우 예외를 발생시킵니다.
     * 공지사항에 연결된 이미지 정보를 삭제한 후, 공지사항을 데이터베이스에서 삭제합니다.
     *
     * @param noticeId 삭제할 공지사항의 ID
     * @param member 요청을 수행하는 사용자 정보가 담긴 {@link UserDetailsImpl} 객체.
     *               사용자의 ID를 통해 작성자 여부를 확인합니다.
     * @throws NotFoundException 주어진 ID에 해당하는 공지사항이 존재하지 않는 경우 발생합니다.
     * @throws BadCredentialsException 요청한 사용자가 공지사항의 작성자가 아닌 경우 발생합니다.
     */
    @Transactional
    public void deleteNotice(Long noticeId, UserDetailsImpl member) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지사항이 존재하지 않습니다."));

        if (!notice.getMember().getId().equals(member.getUserId())) {
            throw new BadCredentialsException("해당 공지사항 작성자가 아닙니다.");
        }

        noticeImageService.deleteNoticeImageDB(noticeId);
        noticeRepository.delete(notice);
    }

    /**
     * 공지사항의 조회 수를 증가시키는 메서드입니다.
     * 주어진 공지사항 ID를 기반으로 공지사항을 조회하고, 조회 수를 1 증가시킵니다.
     * 변경된 조회 수를 반환합니다.
     *
     * @param noticeId 조회 수를 증가시킬 공지사항의 ID
     * @return 증가된 조회 수
     * @throws NotFoundException 주어진 ID에 해당하는 공지사항이 존재하지 않는 경우 발생합니다.
     */
    @Transactional
    public long addNoticeViewCnt(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지사항이 존재하지 않습니다."));

        notice.addViewCnt(notice.getViewCnt() + 1);

        return notice.getViewCnt();
    }

}
