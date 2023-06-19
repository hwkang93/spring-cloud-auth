package hwkang.study.auth.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * AuthenticationWebFilter 에 주입된 권한 검증 매니저
 *
 * (검증을 따로 진행하지는 않는다.)
 */
@Slf4j
public class JwtTokenAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication var1) {
        return Mono.just(var1);
    }
}
