
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PortfolioRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Collaborator;
import domain.MiscellaneousReport;
import domain.Portfolio;
import domain.StudyReport;
import domain.WorkReport;

@Service
@Transactional
public class PortfolioService extends AbstractService {

	@Autowired
	PortfolioRepository	portrepository;


	public Portfolio findOne(final int id) {
		return this.portrepository.findOne(id);
	}

	public Collection<Portfolio> findAll() {
		return this.portrepository.findAll();
	}

	public Portfolio createPortfolio() {
		Collaborator c;
		c = (Collaborator) this.portrepository.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Portfolio portfolio;
		portfolio = new Portfolio();

		portfolio.setTitle("");
		portfolio.setFullName(c.getName() + " " + c.getSurname());
		//portfolio.setMoment(moment);
		portfolio.setAddress(c.getAddress());

		portfolio.setPhone(c.getPhone());

		portfolio.setWorkReport(new ArrayList<WorkReport>());
		portfolio.setStudyReport(new ArrayList<StudyReport>());
		portfolio.setMiscellaneousReport(new ArrayList<MiscellaneousReport>());

		return portfolio;
	}

	public Portfolio save(final Portfolio p) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));
		UserAccount user;
		user = LoginService.getPrincipal();

		Portfolio result;

		result = this.portrepository.save(p);

		return result;
	}
}
