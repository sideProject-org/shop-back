package toy.shop.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.domain.member.Address;
import toy.shop.domain.member.Member;
import toy.shop.dto.member.address.AddressSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.member.AddressRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

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
}
