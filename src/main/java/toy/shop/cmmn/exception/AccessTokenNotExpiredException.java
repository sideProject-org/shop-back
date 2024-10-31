package toy.shop.cmmn.exception;

public class AccessTokenNotExpiredException extends RuntimeException {

    public AccessTokenNotExpiredException(String message) {
        super(message);
    }
}
