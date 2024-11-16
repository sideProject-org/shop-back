package toy.shop.dto.oauth;

public interface OAuth2Response {

    // 제공자
    String getProvider();

    // 제공자에서 발급해주는 아이디(번호)
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 이름
    String getName();

    // 프로필 사진 url
    String getProfileUrl();
}
