package hwkang.study.auth.auth.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReissueRequestDto {
    private String accessToken;
    private String refreshToken;
}
