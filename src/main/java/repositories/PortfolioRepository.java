package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

}
