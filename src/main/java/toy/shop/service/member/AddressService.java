package toy.shop.service.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.member.Address;
import toy.shop.domain.member.Member;
import toy.shop.dto.member.address.AddressSaveRequestDTO;
import toy.shop.dto.member.address.AddressUpdateRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.member.AddressRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    /**
     * 사용자의 주소 정보를 저장합니다.
     *
     * @param parameter 주소 저장 요청 데이터를 담고 있는 DTO 객체
     * @param userDetails 현재 인증된 사용자의 정보를 담고 있는 객체
     * @return 저장된 주소의 ID
     * @throws UsernameNotFoundException 사용자 정보가 존재하지 않을 경우 발생
     */
    public Long saveAddress(AddressSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        Address address = Address.builder()
                .name(parameter.getName())
                .addr(parameter.getAddr())
                .addrDetail(parameter.getAddrDetail())
                .phone(parameter.getPhone())
                .zipCode(parameter.getZipCode())
                .request(parameter.getRequest())
                .member(member)
                .build();

        Address savedAddress = addressRepository.save(address);

        return savedAddress.getId();
    }

    /**
     * 사용자의 배송지 정보를 업데이트합니다.
     *
     * @param addressId 업데이트할 배송지의 ID
     * @param parameter 배송지 업데이트 요청 데이터를 담고 있는 DTO 객체
     * @param userDetails 현재 인증된 사용자의 정보를 담고 있는 객체
     * @return 업데이트된 배송지의 ID
     * @throws UsernameNotFoundException 사용자 정보가 존재하지 않을 경우 발생
     * @throws NotFoundException 배송지 정보가 존재하지 않을 경우 발생
     * @throws AccessDeniedException 로그인된 사용자의 배송지가 아닐 경우 발생
     */
    @Transactional
    public Long updateAddress(Long addressId, AddressUpdateRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 배송지입니다."));

        if (!address.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("로그인 된 회원의 배송지가 아닙니다.");
        }

        address.updateAddress(parameter);

        return address.getId();
    }
}
