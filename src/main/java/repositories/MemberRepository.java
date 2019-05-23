
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Member m where m.account.id = ?1")
	Member findMemberByUserAccount(int memberId);

}
