package common.loginapiserver.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, Object id) {
        super(message + "를 찾을 수 없습니다. : " + id);
    }
}
