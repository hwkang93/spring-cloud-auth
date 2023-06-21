package hwkang.study.auth.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Data
public class SessionUser {

    private String userId;

    private String userName;

    private String userSeCode;

    private String insttCode;

    public String getUserId() {
        if(StringUtils.hasLength(userId)) {
            return userId;
        }
        return "!GUEST";
    }

    public String getUserSeCode() {
        if(StringUtils.hasLength(userSeCode)) {
            return userSeCode;
        }
        return "!GUEST";
    }

    public String getInsttCode() {
        if(StringUtils.hasLength(insttCode)) {
            return insttCode;
        }
        return "!GUEST";
    }
}
