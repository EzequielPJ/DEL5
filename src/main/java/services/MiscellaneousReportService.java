
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MiscellaneousReportRepository;
import security.LoginService;
import domain.Actor;
import domain.Collaborator;
import domain.MiscellaneousReport;
import domain.Portfolio;

@Service
@Transactional
public class MiscellaneousReportService extends AbstractService {

	@Autowired
	private MiscellaneousReportRepository	repository;

	@Autowired
	private PortfolioService				servicePortfolio;

	@Autowired
	private Validator						validator;


	public void flush() {
		this.repository.flush();
	}

	public Collection<MiscellaneousReport> saveIterable(final Collection<MiscellaneousReport> save) {
		return this.repository.save(save);
	}

	public Portfolio findPortfolioByMiscellaneousReportId(final int id) {
		return this.repository.findPortfolioByMiscellaneousReportId(id);
	}

	public Actor findActorByUserAccountId(final int id) {
		return this.repository.findActorByUserAccountId(id);
	}

	public MiscellaneousReport findOne(final int id) {
		return this.repository.findOne(id);
	}

	public MiscellaneousReport create() {
		MiscellaneousReport result;

		result = new MiscellaneousReport();

		result.setMoment(new Date());
		result.setTitle("");
		result.setText("");
		result.setAttachments(new ArrayList<String>());

		return result;
	}

	public MiscellaneousReport save(final MiscellaneousReport saveTo, final int IdPortfolio) {

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Portfolio p;
		p = this.servicePortfolio.findOne(IdPortfolio);

		Assert.isTrue(p.equals(c.getPortfolio()));

		if (saveTo.getId() != 0) {
			Portfolio aux;
			aux = this.repository.findPortfolioByMiscellaneousReportId(saveTo.getId());
			Assert.isTrue(aux.getId() == p.getId());
		}

		MiscellaneousReport result;

		result = this.repository.save(saveTo);

		Collection<MiscellaneousReport> col;
		col = p.getMiscellaneousReport();

		if (!col.contains(result)) {
			col.add(result);
			p.setMiscellaneousReport(col);
		}

		return result;

	}

	public MiscellaneousReport reconstruct(final MiscellaneousReport reconsTo, final BindingResult binding) {
		MiscellaneousReport result;

		if (reconsTo.getId() == 0)
			result = reconsTo;
		else {
			result = this.repository.findOne(reconsTo.getId());
			result.setMoment(reconsTo.getMoment());
			result.setTitle(reconsTo.getTitle());
			result.setText(reconsTo.getText());
			result.setAttachments(reconsTo.getAttachments());
		}

		this.validator.validate(result, binding);

		if (super.checkScript(result.getAttachments()))
			binding.rejectValue("attachments", "attachments.error");

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void delete(final int id) {
		MiscellaneousReport deleteTo;
		deleteTo = this.repository.findOne(id);

		Portfolio p;
		p = this.repository.findPortfolioByMiscellaneousReportId(id);

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Assert.isTrue(p.getId() == c.getPortfolio().getId());

		Collection<MiscellaneousReport> col;
		col = p.getMiscellaneousReport();

		if (col.contains(deleteTo)) {
			col.remove(deleteTo);
			p.setMiscellaneousReport(col);
			this.repository.delete(id);
		}

	}

	public void deleteAllMiscellaneousReport(final Portfolio p) {
		Collaborator c;
		c = (Collaborator) this.repository.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Assert.isTrue(c.getPortfolio().equals(p), "You don't have permission to do this");

		this.repository.delete(p.getMiscellaneousReport());
	}

	public void delete(final Collection<MiscellaneousReport> stu) {
		this.repository.delete(stu);//Este delete es el delete(Iterable) del repo
	}

}
