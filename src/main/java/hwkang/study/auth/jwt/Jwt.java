package hwkang.study.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Json Web Token 객체
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jwt {

    /**
     * 인증 위임 방법(OAuth 2.0 에 정의된 타입을 명시해야 한다.)
     *
     *  - API 인증 : Bearer
     *   (Bearer 이란 일반적으로 액세스 토큰을 전송했다고 서버에게 알리는 것)
     *
     */
    private String grantType;

    /**
     * 액세스 토큰
     */
    private String accessToken;

    /**
     * 갱신 토큰
     *
     *  액세스 토큰이 만료된 경우 갱신 토큰을 통해 액세스 토큰을 갱신한다.
     */
    private String refreshToken;

    //만료 일자
    private Long accessTokenExpiresIn;
}
