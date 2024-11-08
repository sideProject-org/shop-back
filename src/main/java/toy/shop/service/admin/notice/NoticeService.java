package toy.shop.service.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.member.Member;
import toy.shop.domain.notice.Notice;
import toy.shop.dto.admin.notice.SaveNoticeRequestDTO;
import toy.shop.repository.admin.notice.NoticeRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;

    private final NoticeImageService noticeImageService;

    /**
     * 공지사항을 저장하는 메서드입니다. 주어진 요청 정보를 기반으로 공지사항을 생성하고, 관련된 임시 이미지 URL을 메인 URL로 변환합니다.
     * 변환된 공지사항 내용과 회원 정보를 사용하여 공지사항을 저장하고, 임시 이미지 파일을 메인 저장소로 이동시킨 후
     * 이미지 정보를 데이터베이스에 저장합니다.
     *
     * @param parameter 공지사항 저장 요청 정보를 담고 있는 {@link SaveNoticeRequestDTO} 객체.
     *                  이 객체는 작성자 ID, 제목, 내용, 및 임시 이미지 URL 목록을 포함합니다.
     * @return 저장된 공지사항의 ID
     * @throws UsernameNotFoundException 제공된 사용자 ID가 존재하지 않는 경우 발생합니다.
     */
    @Transactional
    public Long saveNotice(SaveNoticeRequestDTO parameter) {
        Member member = memberRepository.findById(parameter.getMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 아이디입니다."));

        String updatedContent = noticeImageService.convertTemporaryUrlsToMainUrls(parameter.getContent());

        // Notice 저장
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
     * 공지사항 ID와 회원 이메일을 기반으로 공지사항 및 연관된 이미지 데이터를 삭제하는 메서드입니다.
     *
     * 이 메서드는 먼저 회원 이메일을 사용하여 회원 정보를 조회하고, 공지사항 ID로 공지사항을 조회합니다.
     * 요청한 회원이 해당 공지사항의 작성자인지 확인하며, 작성자가 아닌 경우 인증 예외가 발생합니다.
     * 검증이 완료되면 공지사항을 삭제하고, 관련된 이미지 데이터를 데이터베이스와 파일 시스템에서 삭제합니다.
     *
     * @param noticeId 삭제할 공지사항의 ID (null이 아니어야 함)
     * @param memberEmail 요청한 회원의 이메일 (null이 아니어야 함)
     * @throws UsernameNotFoundException 주어진 이메일에 해당하는 회원이 존재하지 않을 경우 발생합니다.
     * @throws NotFoundException 공지사항이 존재하지 않을 경우 발생합니다.
     * @throws BadCredentialsException 공지사항 작성자가 아닌 경우 발생합니다.
     */
    @Transactional
    public void deleteNotice(Long noticeId, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지사항이 존재하지 않습니다."));

        if (!notice.getMember().getId().equals(member.getId())) {
            throw new BadCredentialsException("해당 공지사항 작성자가 아닙니다.");
        }

        noticeImageService.deleteNoticeImageDB(noticeId);
        noticeRepository.delete(notice);
    }
}
