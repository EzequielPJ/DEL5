
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

}
