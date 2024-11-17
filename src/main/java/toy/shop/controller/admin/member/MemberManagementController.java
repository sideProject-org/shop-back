package toy.shop.controller.admin.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.shop.controller.ResponseBuilder;
import toy.shop.dto.Response;
import toy.shop.service.admin.member.MemberManagementService;

import static toy.shop.controller.ResponseBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class MemberManagementController implements MemberManagementControllerDocs {

    private final MemberManagementService memberManagementService;

    @GetMapping("/withdraw/{memberId}")
    public ResponseEntity<Response<?>> withdrawalMember(@PathVariable Long memberId) {
        Long result = memberManagementService.withdrawalMember(memberId);

        return buildResponse(HttpStatus.OK, "회원 탈퇴 성공", result);
    }

    @GetMapping("/restore/{memberId}")
    public ResponseEntity<Response<?>> restoreMember(@PathVariable Long memberId) {
        Long result = memberManagementService.restoreMember(memberId);

        return buildResponse(HttpStatus.OK, "회원 복원 성공", result);
    }
}
