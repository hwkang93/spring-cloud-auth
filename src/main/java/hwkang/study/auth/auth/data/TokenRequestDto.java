package hwkang.study.auth.auth.data;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDto {

    @NotNull
    private String sessionKey;

    private String crtfckey;

    public UsernamePasswordAuthenticationToken toAuthentication(String tokenName) {
        SimpleGrantedAuthority crtfckeyAuthority = new SimpleGrantedAuthority(crtfckey);
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = Arrays.asList(crtfckeyAuthority);

        return new UsernamePasswordAuthenticationToken(tokenName, null, simpleGrantedAuthorities);
    }

    public void setCrtfckey(String crtfckey) {
        this.crtfckey = crtfckey;
    }
}
