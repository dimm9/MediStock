package umcs.medical.medistock.auth.DTO;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String login;
    private String role;
}