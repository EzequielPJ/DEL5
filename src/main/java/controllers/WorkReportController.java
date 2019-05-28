
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
import services.PortfolioService;
import services.WorkReportService;
import domain.Collaborator;
import domain.Portfolio;
import domain.WorkReport;

@Controller
@RequestMapping("/workReport")
public class WorkReportController extends BasicController {

	@Autowired
	private PortfolioService	servicePortfolio;

	@Autowired
	private WorkReportService	service;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));

		return super.create(this.service.create(), "workReport/edit", "workReport/edit.do", "portfolio/list.do");

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Portfolio portfolio;
		portfolio = c.getPortfolio();

		WorkReport workreport;
		workreport = this.service.findOne(id);

		Collection<WorkReport> col;
		col = portfolio.getWorkReport();

		Assert.isTrue(col.contains(workreport), "You don't have permission to do this");

		ModelAndView result;
		result = super.edit(workreport, "workReport/edit", "workReport/edit.do", "portfolio/list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(final WorkReport workReport, final BindingResult binding) {
		ModelAndView result = null;
		if (this.service.checkDates(workReport.getStartDate(), workReport.getEndDate()))
			result = super.save(workReport, binding, "study.commit.error", "workReport/edit", "workReport/edit.do", "portfolio/list.do", "redirect:/portfolio/list.do");
		else
			return this.createAndEditModelAndView(workReport, "bad.dates", "workReport/edit", "workReport/edit.do", "redirect:/portfolio/list.do");
		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result = null;
		WorkReport s;
		s = this.service.findOne(id);
		result = super.show(s, "workReport/edit", "workReport/edit.do", "portfolio/list.do");

		result.addObject("view", true);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEntity(@RequestParam final int id) {

		Portfolio portfolio;
		portfolio = this.service.findPortfolioByWorkReportId(id);
		WorkReport workReport;
		workReport = this.service.findOne(id);

		Collection<WorkReport> col;
		col = portfolio.getWorkReport();

		Assert.isTrue(col.contains(workReport), "You don't have permission to do this");
		ModelAndView result;

		result = super.delete(this.service.findOne(id), "workReport.commit.error", "studyReport/edit", "workReport/edit.do", "portfolio/list.do", "redirect:/portfolio/list.do");

		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {

		WorkReport workReport;
		workReport = (WorkReport) e;
		workReport = this.service.reconstruct(workReport, binding);
		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Portfolio portfolio;
		portfolio = c.getPortfolio();

		this.service.save(workReport, portfolio.getId());

		ModelAndView result;
		result = new ModelAndView(nameResolver);

		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		WorkReport stu;
		stu = (WorkReport) e;
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
