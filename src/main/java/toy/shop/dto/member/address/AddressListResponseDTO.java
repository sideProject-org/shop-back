package toy.shop.dto.member.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressListResponseDTO {

    private Long id;
    private String name;
    private String addr;
    private String addrDetail;
    private String addrNickName;
    private String phone;
    private String request;
    private char defaultType;
}
