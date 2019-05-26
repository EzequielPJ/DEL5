
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("select s from Student s where s.account.id = ?1")
	Student findStudentByUserAccount(int studentId);
}
