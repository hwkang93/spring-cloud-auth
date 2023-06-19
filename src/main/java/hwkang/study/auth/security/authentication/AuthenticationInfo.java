package hwkang.study.auth.security.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationInfo {
    private String sessionKey;
    private String crtfckey;
}
