package umcs.medical.medistock.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import umcs.medical.medistock.employee.Employee;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByLogin(String login);
}