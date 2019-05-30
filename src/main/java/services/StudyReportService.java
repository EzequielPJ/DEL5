
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

import repositories.StudyReportRepository;
import security.LoginService;
import domain.Actor;
import domain.Collaborator;
import domain.Portfolio;
import domain.StudyReport;

@Service
@Transactional
public class StudyReportService extends AbstractService {

	@Autowired
	private StudyReportRepository	repository;

	@Autowired
	private PortfolioService		servicePortfolio;

	@Autowired
	private Validator				validator;


	public void flush() {
		this.repository.flush();
	}

	public Collection<StudyReport> saveIterable(final Collection<StudyReport> save) {
		return this.repository.save(save);
	}

	public Portfolio findPortfolioByStudyReportId(final int id) {
		return this.repository.findPortfolioByStudyReportId(id);
	}

	public Actor findActorByUserAccountId(final int id) {
		return this.repository.findActorByUserAccountId(id);
	}

	public StudyReport findOne(final int id) {
		return this.repository.findOne(id);
	}

	public StudyReport create() {
		StudyReport result;

		result = new StudyReport();

		result.setCourse("");
		result.setStartDate(new Date());
		result.setEndDate(new Date());
		result.setAverage(0.0);
		result.setMoment(new Date());
		result.setTitle("");
		result.setPercentajeCredits(0.0);

		return result;
	}

	public StudyReport save(final StudyReport saveTo, final int IdPortfolio) {

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Portfolio p;
		p = this.servicePortfolio.findOne(IdPortfolio);

		Assert.isTrue(p.getId() == c.getPortfolio().getId());

		if (saveTo.getId() != 0) {
			Portfolio aux;
			aux = this.repository.findPortfolioByStudyReportId(saveTo.getId());
			Assert.isTrue(aux.getId() == p.getId());
		}

		StudyReport result;

		result = this.repository.save(saveTo);

		Collection<StudyReport> col;
		col = p.getStudyReport();

		if (!col.contains(result)) {
			col.add(result);
			p.setStudyReport(col);
		}

		return result;

	}

	public StudyReport reconstruct(final StudyReport reconsTo, final BindingResult binding) {
		StudyReport result;

		if (reconsTo.getId() == 0)
			result = reconsTo;
		else {
			result = this.repository.findOne(reconsTo.getId());
			result.setCourse(reconsTo.getCourse());
			result.setStartDate(reconsTo.getStartDate());
			result.setEndDate(reconsTo.getEndDate());
			result.setAverage(reconsTo.getAverage());
			result.setMoment(reconsTo.getMoment());
			result.setTitle(reconsTo.getTitle());
			result.setPercentajeCredits(reconsTo.getPercentajeCredits());
		}

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void delete(final int id) {
		StudyReport deleteTo;
		deleteTo = this.repository.findOne(id);

		Portfolio p;
		p = this.repository.findPortfolioByStudyReportId(id);

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Assert.isTrue(p.equals(c.getPortfolio()));

		Collection<StudyReport> col;
		col = p.getStudyReport();

		col.remove(deleteTo);

		p.setStudyReport(col);
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

	public void delete(final Collection<StudyReport> stu) {
		this.repository.delete(stu);//Este delete es el delete(Iterable) del repo
	}

}
