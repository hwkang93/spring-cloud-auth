package hwkang.study.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml 에 정의된 프로퍼티 값
 */
@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtProperties {

    private String secretKey;

    //private String targetApiKey;

    private long accessTokenExpiration;

    private long refreshTokenExpiration;
}
