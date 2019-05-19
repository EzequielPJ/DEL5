
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
