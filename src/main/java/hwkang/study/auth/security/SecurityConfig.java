package hwkang.study.auth.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geopaas.auth.jwt.JwtTokenProvider;
import org.geopaas.auth.redis.RedisSessionUserService;
import org.geopaas.auth.security.authentication.JwtTokenAuthenticationWebFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final RedisSessionUserService redisSessionUserService;

    public static final String[] PERMIT_ALL_PATTERNS = {
            "/auth/**",
            "/portal/auth/email/**",
            "/portal/auth/users/**",
            "/portal/users/id/exists",
            "/portal/users/email/exists",
            "/portal/instt/exists"
    };

    @Value("${security.allowOrigins}")
    private List<String> allowOrigins;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .cors()
                    .configurationSource(corsConfigurationSource()).and()
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(authenticationEntryPoint)  //401 예외처리 클래스 등록
                        .accessDeniedHandler(jwtAccessDeniedHandler)        //403 예외처리 클래스 등록
                )
                .formLogin().disable()                                          //로그인 화면 X
                .logout().disable()
                .httpBasic().disable()
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PERMIT_ALL_PATTERNS).permitAll()      // '/auth/**' 로 시작하는 요청은 필터링하지 않는다.
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().authenticated()                      //그 외의 모든 요청은 필터링을 진행한다.
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())   //스프링 시큐리티에서 제공하는 세션 사용 X
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)    //JWT 인증 필터 추가 (/auth/** 이외의 모든 요청에서 인증 진행)
                .build();
    }

    /**
     * JwtTokenAuthenticationWebFilter 를 추가한다.
     * @return
     */
    public AuthenticationWebFilter authenticationWebFilter() {
        JwtTokenAuthenticationWebFilter jwtTokenAuthenticationWebFilter = new JwtTokenAuthenticationWebFilter(jwtTokenProvider, redisSessionUserService);
        jwtTokenAuthenticationWebFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(authenticationEntryPoint));

        return jwtTokenAuthenticationWebFilter;
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
