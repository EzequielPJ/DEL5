
package services;

import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.WorkReportRepository;
import security.LoginService;
import domain.Actor;
import domain.Collaborator;
import domain.Portfolio;
import domain.WorkReport;

@Service
@Transactional
public class WorkReportService extends AbstractService {

	@Autowired
	private WorkReportRepository	repository;

	@Autowired
	private PortfolioService		servicePortfolio;

	@Autowired
	private Validator				validator;


	public void flush() {
		this.repository.flush();
	}

	public Collection<WorkReport> saveIterable(final Collection<WorkReport> save) {
		return this.repository.save(save);
	}

	public Portfolio findPortfolioByWorkReportId(final int id) {
		return this.repository.findPortfolioByWorkReportId(id);
	}

	public Actor findActorByUserAccountId(final int id) {
		return this.repository.findActorByUserAccountId(id);
	}

	public WorkReport findOne(final int id) {
		return this.repository.findOne(id);
	}

	public WorkReport create() {
		WorkReport result;

		result = new WorkReport();

		result.setTitle("");
		result.setStartDate(new Date());
		result.setEndDate(new Date());
		result.setBusinessName("");
		result.setMoment(new Date());
		result.setText("");

		return result;
	}

	public WorkReport save(final WorkReport saveTo, final int IdPortfolio) {

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Portfolio p;
		p = this.servicePortfolio.findOne(IdPortfolio);

		Assert.isTrue(p.equals(c.getPortfolio()));

		WorkReport result;

		result = this.repository.save(saveTo);

		Collection<WorkReport> col;
		col = p.getWorkReport();

		if (!col.contains(result)) {
			col.add(result);
			p.setWorkReport(col);
		}

		return result;

	}

	public WorkReport reconstruct(final WorkReport reconsTo, final BindingResult binding) {
		WorkReport result;
		if (reconsTo.getId() == 0)
			result = reconsTo;
		else {
			result = this.repository.findOne(reconsTo.getId());
			result.setStartDate(reconsTo.getStartDate());
			result.setEndDate(reconsTo.getEndDate());
			result.setBusinessName(reconsTo.getBusinessName());
			result.setMoment(reconsTo.getMoment());
			result.setTitle(reconsTo.getTitle());
			result.setText(reconsTo.getText());
		}

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void delete(final int id) {
		WorkReport deleteTo;
		deleteTo = this.repository.findOne(id);

		Portfolio p;
		p = this.repository.findPortfolioByWorkReportId(id);

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Assert.isTrue(p.equals(c.getPortfolio()));

		Collection<WorkReport> col;
		col = p.getWorkReport();

		col.remove(deleteTo);

		p.setWorkReport(col);
	}

	public void deleteAllWorkReport(final Portfolio p) {
		Collaborator c;
		c = (Collaborator) this.repository.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Assert.isTrue(c.getPortfolio().equals(p), "You don't have permission to do this");

		this.repository.delete(p.getWorkReport());
	}

	public boolean checkDates(final Date begin, final Date end) {
		boolean res = true;
		if (begin == null)
			res = false;
		if (end == null)
			res = true;
		if (begin == null && end == null)
			res = false;
		if (begin != null && end != null)
			res = begin.before(end) && end.after(begin);
		return res;
	}

	public void delete(final Collection<WorkReport> wr) {
		this.repository.delete(wr);//Este delete es el delete(Iterable) del repo
	}
}
