
package controllers;

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
import domain.Collaborator;
import domain.Portfolio;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController extends BasicController {

	@Autowired
	private PortfolioService	service;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listPorfolio() {
		Collaborator c;
		c = (Collaborator) this.service.findActorByUserAccountId(LoginService.getPrincipal().getId());

		final ModelAndView result = super.listModelAndView("portfolio", "portfolio/list", this.service.findPortfolioByActor(LoginService.getPrincipal().getId()), "portfolio/list.do");

		if (c.getPortfolio() != null) {
			result.addObject("port", true);
			result.addObject("lista", false);
			result.addObject("StudyReport", c.getPortfolio().getStudyReport());
			result.addObject("WorkReport", c.getPortfolio().getWorkReport());
			result.addObject("MiscellaneousReport", c.getPortfolio().getMiscellaneousReport());
		} else {
			result.addObject("port", false);
			result.addObject("lista", true);
		}
		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));

		return super.create(this.service.create(), "portfolio/edit", "portfolio/edit.do", "portfolio/list.do");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));
		Portfolio p;
		p = this.service.findOne(id);
		Collaborator c;
		c = (Collaborator) this.service.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Assert.isTrue(p.getId() == c.getPortfolio().getId(), "You don't have permission to do this");
		final ModelAndView result = super.edit(p, "portfolio/edit", "portfolio/edit.do?id=" + id, "portfolio/list.do");

		result.addObject("view", false);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Portfolio p, final BindingResult binding) {
		ModelAndView result;
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));
		Collaborator c;
		c = (Collaborator) this.service.findActorByUserAccountId(LoginService.getPrincipal().getId());
		if (p.getId() != 0)
			Assert.isTrue(p.getId() == c.getPortfolio().getId(), "You don't have permission to do this");

		result = super.save(p, binding, "portfolio.commit.error", "portfolio/edit", "portfolio/edit.do", "portfolio/list.do", "redirect:list.do");
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Portfolio p;
		p = (Portfolio) e;
		p = this.service.reconstruct(p, binding);
		this.service.save(p);
		result = new ModelAndView(nameResolver);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		ModelAndView result;
		Portfolio p;
		p = (Portfolio) e;
		this.service.delete(p.getId());
		result = new ModelAndView(nameResolver);
		return result;
	}

}
