package hwkang.study.auth.security;

import lombok.Getter;

/**
 * 401, 403 예외에 대한 응답 객체
 */
@Getter
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
