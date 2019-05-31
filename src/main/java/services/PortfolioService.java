
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PortfolioRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Collaborator;
import domain.MiscellaneousReport;
import domain.Portfolio;
import domain.StudyReport;
import domain.WorkReport;

@Service
@Transactional
public class PortfolioService extends AbstractService {

	@Autowired
	PortfolioRepository					portrepository;

	@Autowired
	private Validator					validator;

	@Autowired
	private StudyReportService			study;

	@Autowired
	private WorkReportService			work;

	@Autowired
	private MiscellaneousReportService	misc;


	public Portfolio findOne(final int id) {
		return this.portrepository.findOne(id);
	}

	public Collection<Portfolio> findAll() {
		return this.portrepository.findAll();
	}

	public List<Portfolio> findPortfolioByActor(final int id) {
		return this.portrepository.findPortfolioByActor(id);
	}
	public Actor findActorByUserAccountId(final int id) {
		return this.portrepository.findActorByUserAccountId(id);
	}

	public Portfolio create() {
		Collaborator c;
		c = (Collaborator) this.portrepository.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Portfolio portfolio;
		portfolio = new Portfolio();

		portfolio.setTitle("");
		portfolio.setFullName(c.getName() + " " + c.getSurname());
		portfolio.setMoment(new Date());
		portfolio.setAddress(c.getAddress());
		portfolio.setPhone(c.getPhone());
		//
		portfolio.setWorkReport(new ArrayList<WorkReport>());
		portfolio.setStudyReport(new ArrayList<StudyReport>());
		portfolio.setMiscellaneousReport(new ArrayList<MiscellaneousReport>());

		return portfolio;
	}

	public void delete(final int id) {
		Collaborator c;
		c = (Collaborator) this.portrepository.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Portfolio p;
		p = this.portrepository.findOne(id);

		Assert.isTrue(c.getPortfolio().equals(p), "You don't have permission to do this");

		this.portrepository.delete(id);
	}

	public Portfolio save(final Portfolio p) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));

		Portfolio saved = null;

		Collaborator c;
		c = (Collaborator) this.portrepository.findActorByUserAccountId(LoginService.getPrincipal().getId());

		if (p.getId() == 0)
			c.setPortfolio(p);
		else
			Assert.isTrue(c.getPortfolio().equals(p), "You don't have permission to do this");

		saved = this.portrepository.save(p);
		c.setPortfolio(saved);

		return saved;
	}

	public Portfolio reconstruct(final Portfolio p, final BindingResult binding) {
		Portfolio result;
		if (p.getId() == 0)
			result = p;
		else {
			result = this.portrepository.findOne(p.getId());
			result.setPhone(p.getPhone());
			result.setTitle(p.getTitle());
			result.setFullName(p.getFullName());
			result.setMoment(p.getMoment());
			result.setAddress(p.getAddress());
			result.setStudyReport(p.getStudyReport());
			result.setMiscellaneousReport(p.getMiscellaneousReport());
			result.setWorkReport(p.getWorkReport());
		}

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void flush() {
		this.portrepository.flush();
	}

	public void delete(final Collection<Portfolio> p) {
		this.portrepository.delete(p);

	}
}
