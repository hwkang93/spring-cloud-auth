package hwkang.study.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *  401 예외 처리 클래스
 */
@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException ae) {
        ServerHttpResponse response = serverWebExchange.getResponse();

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                ErrorResponse errorResponseDto = new ErrorResponse(ae.getMessage());
                byte[] errorResponse = objectMapper.writeValueAsBytes(errorResponseDto);

                return bufferFactory.wrap(errorResponse);
            } catch (Exception exception) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
