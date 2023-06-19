package hwkang.study.auth.security.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geopaas.auth.redis.RedisSessionUserService;
import org.geopaas.auth.redis.SessionUser;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * JWT 를 통한 인증 성공 후 진행되는 후처리 클래스
 *
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final RedisSessionUserService redisSessionUserService;

    /**
     * Authentication 에 포함된 API KEY 값을 Request URI 쿼리스트링에 추가한다.
     *
     * @param webFilterExchange
     * @param authentication
     * @return
     */
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        ServerHttpRequest newRequest = request
                .mutate()
                .uri(
                        createUriWithParam(request, authentication)
                )
                .build();

        return webFilterExchange.getChain().filter(exchange.mutate().request(newRequest).response(response).build());
    }

    private URI createUriWithParam(ServerHttpRequest request, Authentication authentication) {
        String sessionKey = authentication.getName();

        SessionUser sessionUser = redisSessionUserService.findById(sessionKey);
        Object[] authorities = authentication.getAuthorities().toArray();
        String crtfckey = authorities[0].toString();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpRequest(request)
                .queryParam("sessionUserId", sessionUser.getUserId())
                .queryParam("sessionInsttCode", sessionUser.getInsttCode())
                .queryParam("sessionUserSeCode", sessionUser.getUserSeCode())
                .queryParam("crtfckey", crtfckey);

        return uriComponentsBuilder.build(true).toUri();
    }
/*

    private AuthenticationInfo extractAuthenticationInfo(Authentication authentication) {
        Object[] authorities = authentication.getAuthorities().toArray();
        String crtfckey = authorities[0].toString();

        SessionUser sessionUser = redisSessionUserRepository.findById(sessionKey).get();


        String userSeCode = authorities[1].toString();
        String insttCode = authorities[2].toString();

        return new AuthenticationInfo(authentication.getName(), crtfckey, userSeCode, insttCode);
    }*/
}
