package hwkang.study.auth.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.geopaas.auth.jwt.JwtTokenProvider;
import org.geopaas.auth.redis.RedisSessionUserService;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
public class JwtTokenAuthenticationWebFilter extends AuthenticationWebFilter {

    private final JwtTokenAuthenticationConverter jwtTokenAuthenticationConverter;
    private final JwtTokenAuthenticationSuccessHandler jwtTokenAuthenticationSuccessHandler;

    public JwtTokenAuthenticationWebFilter(JwtTokenProvider jwtTokenProvider, RedisSessionUserService redisSessionUserService) {
        super(new JwtTokenAuthenticationManager());
        jwtTokenAuthenticationSuccessHandler = new JwtTokenAuthenticationSuccessHandler(redisSessionUserService);
        jwtTokenAuthenticationConverter = new JwtTokenAuthenticationConverter();
        jwtTokenAuthenticationConverter.setJwtTokenProvider(jwtTokenProvider);
        super.setServerAuthenticationConverter(jwtTokenAuthenticationConverter);
        super.setAuthenticationSuccessHandler(jwtTokenAuthenticationSuccessHandler);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.debug("{} Jwt Filter [ {} ] {}", exchange.getLogPrefix(), exchange.getRequest().getMethodValue(), exchange.getRequest().getURI());

        return super.filter(exchange, chain);
    }
}
