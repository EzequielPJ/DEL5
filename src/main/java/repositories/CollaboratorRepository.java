
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Collaborator;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer> {

}
