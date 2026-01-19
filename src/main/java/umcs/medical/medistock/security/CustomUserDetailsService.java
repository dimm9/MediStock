package umcs.medical.medistock.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umcs.medical.medistock.employee.Employee;
import umcs.medical.medistock.employee.EmployeeRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(
                employee.getId(),
                employee.getLogin(),
                employee.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()))
        );
    }
}