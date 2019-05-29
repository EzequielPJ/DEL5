
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import services.MiscellaneousReportService;
import services.PortfolioService;
import domain.Collaborator;
import domain.MiscellaneousReport;
import domain.Portfolio;

@Controller
@RequestMapping("/miscellaneousReport")
public class MiscellaneousReportController extends BasicController {

	@Autowired
	private PortfolioService			servicePortfolio;

	@Autowired
	private MiscellaneousReportService	service;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));

		return super.create(this.service.create(), "miscellaneousReport/edit", "miscellaneousReport/edit.do", "/portfolio/list.do");

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Portfolio portfolio;
		portfolio = c.getPortfolio();

		MiscellaneousReport miscellaneousReport;
		miscellaneousReport = this.service.findOne(id);

		Collection<MiscellaneousReport> col;
		col = portfolio.getMiscellaneousReport();

		Assert.isTrue(col.contains(miscellaneousReport), "You don't have permission to do this");

		ModelAndView result;
		result = super.edit(miscellaneousReport, "miscellaneousReport/edit", "miscellaneousReport/edit.do", "/portfolio/list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(final MiscellaneousReport miscellaneousReport, final BindingResult binding) {
		ModelAndView result = null;
		result = super.save(miscellaneousReport, binding, "miscellaneous.commit.error", "miscellaneousReport/edit", "miscellaneousReport/edit.do", "/portfolio/list.do", "redirect:/portfolio/list.do");
		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result = null;
		MiscellaneousReport m;
		m = this.service.findOne(id);
		result = super.show(m, "miscellaneousReport/edit", "miscellaneousReport/edit.do", "/portfolio/list.do");

		result.addObject("view", true);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEntity(@RequestParam final int id) {

		Portfolio portfolio;
		portfolio = this.service.findPortfolioByMiscellaneousReportId(id);
		MiscellaneousReport miscellaneousReport;
		miscellaneousReport = this.service.findOne(id);

		Collection<MiscellaneousReport> col;
		col = portfolio.getMiscellaneousReport();

		Assert.isTrue(col.contains(miscellaneousReport), "You don't have permission to do this");
		ModelAndView result;

		result = super.delete(this.service.findOne(id), "miscellaneous.commit.error", "miscellaneousReport/edit", "miscellaneousReport/edit.do", "/portfolio/list.do", "redirect:/portfolio/list.do");

		return result;
	}
	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {

		MiscellaneousReport miscellaneousReport;
		miscellaneousReport = (MiscellaneousReport) e;
		miscellaneousReport = this.service.reconstruct(miscellaneousReport, binding);
		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Portfolio portfolio;
		portfolio = c.getPortfolio();

		this.service.save(miscellaneousReport, portfolio.getId());

		ModelAndView result;
		result = new ModelAndView(nameResolver);

		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		MiscellaneousReport stu;
		stu = (MiscellaneousReport) e;
		this.service.delete(stu.getId());
		return new ModelAndView(nameResolver);
	}

	public boolean checkPortfolio(final int portfolio) {
		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Boolean res = false;
		if (c.getPortfolio().getId() == portfolio)
			res = true;
		return res;
	}

}
