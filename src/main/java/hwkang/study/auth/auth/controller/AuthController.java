package hwkang.study.auth.auth.controller;

import lombok.RequiredArgsConstructor;
import org.geopaas.auth.auth.data.ReissueRequestDto;
import org.geopaas.auth.auth.data.TokenRequestDto;
import org.geopaas.auth.auth.service.AuthService;
import org.geopaas.auth.jwt.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.targetApiKey}")
    private String mappickApiKey;

    private final AuthService authService;

    /**
     * API 키가 있는 경우의 토큰 발급
     *
     * @param exchange
     * @param tokenRequestDto
     * @return
     */
    @CrossOrigin
    @PostMapping("/generate")
    public Mono<ResponseEntity<Jwt>> generateToken(ServerWebExchange exchange, @RequestBody TokenRequestDto tokenRequestDto) {
        //String userIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String sessionKey = tokenRequestDto.getSessionKey();

        return authService.generateToken(sessionKey, tokenRequestDto)
                .map(jwt -> ResponseEntity.ok(jwt))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    /**
     * API 키가 없는 경우 yaml 에 정의된 디폴트 값(${jwt.targetApiKey})으로 토큰 발행
     * @param exchange
     * @param tokenRequestDto
     * @return
     */
    @CrossOrigin
    @PostMapping("/generate/mappick")
    public Mono<ResponseEntity<Jwt>> generateMappickToken(ServerWebExchange exchange, @RequestBody TokenRequestDto tokenRequestDto) {
        //String userIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String sessionKey = tokenRequestDto.getSessionKey();

        tokenRequestDto.setCrtfckey(mappickApiKey);

        return authService.generateToken(sessionKey, tokenRequestDto)
                .map(jwt -> ResponseEntity.ok(jwt))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @PostMapping("/reissue")
    public Mono<ResponseEntity<Jwt>> reissueToken(@RequestBody ReissueRequestDto reissueRequestDto) {

        return authService.reissue(reissueRequestDto)
                .map(jwt -> ResponseEntity.ok(jwt))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .onErrorResume(throwable -> Mono.error(throwable));
    }


}
