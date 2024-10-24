package toy.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import toy.shop.dto.Response;
import toy.shop.dto.member.SignupRequestDTO;

@Tag(name = "공통 기능 API", description = "공통 기능 로직에 관한 API")
public interface CmmnDocs {

    @Operation(summary = "회원가입", description = "Request 정보를 통해 회원가입")
    ResponseEntity<Response<?>> joinMember(SignupRequestDTO parameter);
}
