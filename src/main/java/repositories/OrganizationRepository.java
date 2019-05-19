
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

}
