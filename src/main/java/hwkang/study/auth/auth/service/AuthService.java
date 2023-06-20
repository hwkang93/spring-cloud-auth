package hwkang.study.auth.auth.service;

import org.geopaas.auth.auth.data.ReissueRequestDto;
import org.geopaas.auth.auth.data.TokenRequestDto;
import org.geopaas.auth.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * 권한 관련 인터페이스
 */
public interface AuthService {

    /**
     * 토큰 발급
     *   절차
     *     1. 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
     *     2. 인증 정보를 기반으로 JWT 토큰 생성
     *     3. RefreshToken 저장
     *     4. 토큰 발급
     * @param userRequestDto 사용자 정보
     * @return
     */
    Mono<Jwt> generateToken(String tokenName, TokenRequestDto userRequestDto);

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
    Mono<Jwt> reissue(ReissueRequestDto reissueRequestDto);
}
