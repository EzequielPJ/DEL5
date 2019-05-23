
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
import services.ComissionService;
import domain.Comission;
import domain.Member;

@Controller
@RequestMapping(value = {
	"/comission/member", "/comission/collaborator"
})
public class ComissionController extends BasicController {

	@Autowired
	private ComissionService	comissionService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		Assert.isTrue(this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER) || this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR),
			"You don't have access to do this");
		ModelAndView result = null;
		if (this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			Member m;
			m = (Member) this.comissionService.getActorByUserId(LoginService.getPrincipal().getId());
			result = super.listModelAndView("comissions", "comission/list", this.comissionService.getComissionsByMemberId(m.getId()), "comission/list.do");
		} else if (this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR))
			result = super.listModelAndView("comissions", "comission/list", this.comissionService.findAll(), "comission/list.do");
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Assert.isTrue(this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER), "You must be a member");
		ModelAndView result;
		result = super.create(this.comissionService.createComission(), "comission/edit", "comission/member/edit.do", "/comission/member/list.do");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@RequestParam(defaultValue = "0") final int parent, final Comission comission, final BindingResult binding) {
		Assert.isTrue(this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER), "You must be a member");
		ModelAndView result;
		result = super.save(comission, binding, "comission.commit.error", "comission/edit", "comission/member/edit.do", "redirect:list.do", "redirect:list.do");
		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int idComission) {
		ModelAndView result;
		Assert.isTrue(this.comissionService.findOne(idComission).getMember().getId() == this.comissionService.getActorByUserId(LoginService.getPrincipal().getId()).getId(), "You don�t have access, you can only see your comissions");
		result = super.show(this.comissionService.findOne(idComission), "comission/edit", "comission/member/edit.do", "comission/member/list.do");
		return result;
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int idComission) {
		Assert.isTrue(this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER), "You must be a member");
		ModelAndView result;
		Comission aux;
		aux = this.comissionService.findOne(idComission);
		Assert.notNull(aux);
		result = super.edit(this.comissionService.findOne(idComission), "comission/edit", "comission/member/edit.do", "comission/member/list.do");
		result.addObject("comission", aux);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int idComission) {
		ModelAndView result;
		Assert.isTrue(this.comissionService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER), "You must be a member");
		result = super.delete(this.comissionService.findOne(idComission), "comission.commit.error", "comission/edit", "comission/member/edit.do", "redirect:list.do", "redirect:list.do");
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Comission org;
		org = (Comission) e;
		org = this.comissionService.reconstruct(org, binding);
		Comission saved;
		saved = this.comissionService.save(org);
		result = new ModelAndView(nameResolver);
		result.addObject("saved", saved);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		final ModelAndView result;
		Comission comission;
		comission = (Comission) e;
		//Condici�n de que sea tuya
		this.comissionService.deleteComission(comission.getId());
		result = new ModelAndView(nameResolver);
		return result;
	}
}
