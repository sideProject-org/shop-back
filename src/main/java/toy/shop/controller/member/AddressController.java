package toy.shop.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toy.shop.dto.Response;
import toy.shop.dto.member.address.AddressSaveRequestDTO;
import toy.shop.dto.member.address.AddressUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.service.member.AddressService;

import static toy.shop.controller.ResponseBuilder.buildResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController implements AddressControllerDocs{

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<Response<?>> saveAddress(@RequestBody @Valid AddressSaveRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = addressService.saveAddress(parameter, userDetails);

        return buildResponse(HttpStatus.CREATED, "배송지 등록 성공", result);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Response<?>> updateAddress(@PathVariable Long addressId, @RequestBody @Valid AddressUpdateRequestDTO parameter, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long result = addressService.updateAddress(addressId, parameter, userDetails);

        return buildResponse(HttpStatus.OK, "배송지 수정 성공", result);
    }
}
