package umcs.medical.medistock.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByLogin(String login);

    // soft delete restore
    @Modifying
    @Query("UPDATE Employee e SET e.active = true WHERE e.id = :id")
    void restore(Long id);

    //ADMIN: widzi WSZYSTKICH
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllIncludingInactive();

    //ADMIN: dla nieaktywnych po ID
    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    Optional<Employee> findByIdIncludingInactive(Long id);

    // ADMIN: szukanie loginów w tym usuniętych
    @Query("SELECT e FROM Employee e WHERE e.login = :login")
    Optional<Employee> findByLoginIncludingInactive(String login);
}
