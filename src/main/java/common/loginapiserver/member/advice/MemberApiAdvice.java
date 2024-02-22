package common.loginapiserver.member.advice;

import common.loginapiserver.member.controller.MemberController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {MemberController.class})
public class MemberApiAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MemberApiExceptionResult methodArgsNotValidException(MethodArgumentNotValidException e) {
        MemberApiExceptionResult exceptionResult = new MemberApiExceptionResult("INVALID_ARGUMENT");
        e.getFieldErrors().stream().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            exceptionResult.getFieldErrorMap().put(fieldName, message);
        });
        return exceptionResult;
    }
    @Data
    private static class MemberApiExceptionResult {
        private String responseMessage;
        private Map<String, String> fieldErrorMap;
        public MemberApiExceptionResult(String responseMessage) {
            this.responseMessage = responseMessage;
            fieldErrorMap = new LinkedHashMap<>();
        }
    }
}
