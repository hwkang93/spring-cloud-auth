package hwkang.study.auth.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geopaas.auth.auth.data.ReissueRequestDto;
import org.geopaas.auth.auth.data.TokenRequestDto;
import org.geopaas.auth.auth.service.AuthService;
import org.geopaas.auth.jwt.Jwt;
import org.geopaas.auth.jwt.JwtTokenProvider;
import org.geopaas.auth.jwt.RefreshToken;
import org.geopaas.auth.jwt.RefreshTokenRepository;
import org.geopaas.auth.redis.RedisSessionUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisSessionUserService sessionUserService;

    /**
     * 토큰 발급
     *   절차
     *     1. 인증 정보를 기반으로 JWT 토큰 생성
     *     2. RefreshToken 저장
     *     3. 토큰 발급
     * @param tokenRequestDto 사용자 정보
     * @return
     */
    @Override
    public Mono<Jwt> generateToken(String tokenName, TokenRequestDto tokenRequestDto) {
        UsernamePasswordAuthenticationToken authentication = tokenRequestDto.toAuthentication(tokenName);
        Jwt jwt = jwtTokenProvider.generate(authentication);
        //refreshTokenRepository.save(createRefreshToken(authentication, jwt.getRefreshToken()));

        return Mono.just(jwt);
    }

    /**
     * 토큰 재발급
     *  절차
     *    1. Refresh Token 검증
     *    2. Access Token 에서 Member ID 가져오기
     *    3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
     *    4. Refresh Token 일치하는지 검사
     *    5. 새로운 토큰 생성
     *    6. 저장소 정보 업데이트
     *    7. 토큰 발급
     *
     * @param reissueRequestDto 토큰 정보
     * @return
     */
    @Override
    public Mono<Jwt> reissue(ReissueRequestDto reissueRequestDto) {
        jwtTokenProvider.validateToken(reissueRequestDto.getRefreshToken());

        Authentication authentication = jwtTokenProvider.getAuthentication(reissueRequestDto.getAccessToken());
        RefreshToken refreshToken = findRefreshToken(authentication);
        validateTokenMatch(reissueRequestDto.getRefreshToken(), refreshToken);

        Jwt jwt = jwtTokenProvider.generate(authentication);
        //refreshTokenRepository.save(refreshToken.updateValue(jwt.getRefreshToken()));

        return Mono.just(jwt);
    }

    private RefreshToken createRefreshToken(Authentication authentication, String refreshTokenValue) {
        return new RefreshToken(authentication.getName(), refreshTokenValue);
    }

    private RefreshToken findRefreshToken(Authentication authentication) {
        return refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
    }


    private void validateTokenMatch(String requestRefreshTokenValue, RefreshToken refreshToken) {
        if (!refreshToken.getValue().equals(requestRefreshTokenValue)) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}
