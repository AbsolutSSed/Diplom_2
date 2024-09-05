package user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponses {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private String message;
    private User user;
}
