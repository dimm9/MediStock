package umcs.medical.medistock.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import umcs.medical.medistock.auth.DTO.LoginRequest;
import umcs.medical.medistock.auth.DTO.LoginResponse;
import umcs.medical.medistock.auth.DTO.RegisterRequest;
import umcs.medical.medistock.employee.*;
import umcs.medical.medistock.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository repository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder;
    public LoginResponse register(RegisterRequest request) {

        if (repository.findByLogin(request.getLogin()).isPresent()) {
            throw new RuntimeException("Login already exists");
        }

        Employee employee = Employee.builder()
                .login(request.getLogin())
                .passwordHash(encoder.encode(request.getPassword()))
                .role(EmployeeRole.UNREGISTERED)
                .hospitalId(null)
                .name(null)
                .active(true)
                .build();

        repository.save(employee);

        String token = jwtService.generateToken(employee.getLogin(), employee.getRole().name());

        return LoginResponse.builder()
                .token(token)
                .id(employee.getId())
                .login(employee.getLogin())
                .role(employee.getRole().name())
                .hospitalId(employee.getHospitalId())
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        System.out.println("LOGIN ATTEMPT: " + request.getLogin());
        System.out.println("DB URL: " +
                System.getProperty("spring.datasource.url"));

        Employee employee = repository.findByLogin(request.getLogin())
                .orElseThrow(() -> new RuntimeException("Invalid login or password"));

        if (!encoder.matches(request.getPassword(), employee.getPasswordHash())) {
            throw new RuntimeException("Invalid login or password");
        }

        String token = jwtService.generateToken(employee.getLogin(), employee.getRole().name());

        return LoginResponse.builder()
                .token(token)
                .id(employee.getId())
                .login(employee.getLogin())
                .role(employee.getRole().name())
                .hospitalId(employee.getHospitalId())
                .build();
    }

}