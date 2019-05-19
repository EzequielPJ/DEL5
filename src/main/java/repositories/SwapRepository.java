
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Swap;

@Repository
public interface SwapRepository extends JpaRepository<Swap, Integer> {

}
