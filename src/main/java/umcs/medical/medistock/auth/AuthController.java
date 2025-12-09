package umcs.medical.medistock.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umcs.medical.medistock.auth.DTO.LoginRequest;
import umcs.medical.medistock.auth.DTO.LoginResponse;
import umcs.medical.medistock.auth.DTO.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}