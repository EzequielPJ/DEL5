
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
import services.StudyReportService;
import domain.Collaborator;
import domain.Portfolio;
import domain.StudyReport;

@Controller
@RequestMapping("/studyReport")
public class StudyReportController extends BasicController {

	@Autowired
	private PortfolioService	servicePortfolio;

	@Autowired
	private StudyReportService	service;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));

		return super.create(this.service.create(), "studyReport/edit", "studyReport/edit.do", "portfolio/list.do");

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {

		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Portfolio portfolio;
		portfolio = c.getPortfolio();

		StudyReport s;
		s = this.service.findOne(id);

		Collection<StudyReport> col;
		col = portfolio.getStudyReport();

		Assert.isTrue(col.contains(s), "You don't have permission to do this");

		ModelAndView result;
		result = super.edit(s, "studyReport/edit", "studyReport/edit.do", "portfolio/list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(final StudyReport studyReport, final BindingResult binding) {
		ModelAndView result = null;
		if (this.service.checkDates(studyReport.getStartDate(), studyReport.getEndDate()))
			result = super.save(studyReport, binding, "study.commit.error", "studyReport/edit", "studyReport/edit.do", "portfolio/list.do", "redirect:/portfolio/list.do");
		else
			return this.createAndEditModelAndView(studyReport, "bad.dates", "studyReport/edit", "studyReport/edit.do", "redirect:/portfolio/list.do");
		return result;
	}
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result = null;
		StudyReport s;
		s = this.service.findOne(id);
		result = super.show(s, "studyReport/edit", "studyReport/edit.do", "portfolio/list.do");

		result.addObject("view", true);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEntity(@RequestParam final int id) {

		Portfolio portfolio;
		portfolio = this.service.findPortfolioByStudyReportId(id);
		StudyReport studyreport;
		studyreport = this.service.findOne(id);

		Collection<StudyReport> col;
		col = portfolio.getStudyReport();

		Assert.isTrue(col.contains(studyreport), "You don't have permission to do this");
		ModelAndView result;

		result = super.delete(this.service.findOne(id), "study.commit.error", "studyReport/edit", "studyReport/edit.do", "portfolio/list.do", "redirect:/portfolio/list.do");

		return result;
	}
	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {

		StudyReport studyReport;
		studyReport = (StudyReport) e;
		studyReport = this.service.reconstruct(studyReport, binding);
		Collaborator c;
		c = (Collaborator) this.servicePortfolio.findActorByUserAccountId(LoginService.getPrincipal().getId());
		Portfolio portfolio;
		portfolio = c.getPortfolio();

		this.service.save(studyReport, portfolio.getId());

		ModelAndView result;
		result = new ModelAndView(nameResolver);

		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		StudyReport stu;
		stu = (StudyReport) e;
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
