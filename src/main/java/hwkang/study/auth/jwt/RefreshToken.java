package hwkang.study.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
//@RedisHash("refreshToken")
public class RefreshToken {

    //@Id
    private String key;

    private String value;

    public RefreshToken updateValue(String token) {
        //this.value = value;
        return this;
    }
}
