package hwkang.study.auth.security.authentication;

import hwkang.study.auth.jwt.JwtTokenProvider;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *  JWT 인증을 위한 Converter 클래스
 *      Request 객체에서 Authentication 객체를 추출한다.
 */
@RequiredArgsConstructor
public class JwtTokenAuthenticationConverter implements ServerAuthenticationConverter {

    private JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_PREFIX = "Bearer ";

    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        Assert.notNull(jwtTokenProvider, "JwtTokenProvider is required for JwtTokenAuthenticationConverter to work.");
        ServerHttpRequest request = serverWebExchange.getRequest();

        return extractToken(request)
                .filter(jwtTokenProvider::isValid)
                .map(jwtTokenProvider::getAuthentication);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private Mono<String> extractToken(ServerHttpRequest request) {
        Assert.notNull(jwtTokenProvider, "JwtTokenProvider is required for JwtTokenAuthenticationConverter to work.");
        try {
            String bearerToken = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                return Mono.just(bearerToken.substring(BEARER_PREFIX.length()));
            }
        } catch (IndexOutOfBoundsException e) {
            return Mono.empty();
        }

        return Mono.empty();
    }
}
