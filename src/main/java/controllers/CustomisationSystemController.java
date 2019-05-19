
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
import services.CustomisationSystemService;
import forms.CustomForm;

@Controller
@RequestMapping(value = "/customisation/administrator")
public class CustomisationSystemController extends BasicController {

	@Autowired
	private CustomisationSystemService	serviceCustom;


	@RequestMapping(value = "/custom", method = RequestMethod.GET)
	public ModelAndView custom() {
		Assert.isTrue(this.serviceCustom.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		return super.edit(this.serviceCustom.fromCustomisationSystemToObjetForm(this.serviceCustom.findUnique()), "custom/edit", "customisation/administrator/edit.do", "/");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final CustomForm customForm, final BindingResult binding) {
		Assert.isTrue(this.serviceCustom.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		return super.save(customForm, binding, "custom.commit.error", "custom/edit", "customisation/administrator/edit.do", "/", "redirect:/welcome/index.do");
	}

	@RequestMapping(value = "/spam", method = RequestMethod.GET)
	public ModelAndView spamActor(@RequestParam final int id) {
		ModelAndView result = null;
		this.serviceCustom.flagSpam(id);
		result = this.custom(new ModelAndView("redirect:../../"));
		return result;

	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		CustomForm c;
		c = (CustomForm) e;
		c = this.serviceCustom.reconstruct(c, binding);
		this.serviceCustom.save(c);
		result = new ModelAndView(nameResolver);
		return result;
	}

	/*
	 * @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	 * public ModelAndView dashboard() {
	 * ModelAndView result;
	 * Assert.isTrue(this.serviceCustom.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN), "You don't have permission to do this.");
	 * result = this.custom(new ModelAndView("custom/list"));
	 * 
	 * result.addObject("marcadorNumerico", this.serviceCustom.marcadorNumerico());
	 * result.addObject("marcadorNumericoArray", this.serviceCustom.marcadorNumericoArray());
	 * result.addObject("CompanyRookies", this.serviceCustom.CompanyRookies());
	 * result.addObject("BestWorstPositionSalary", this.serviceCustom.BestWorstPositionSalary());
	 * 
	 * return result;
	 * 
	 * }
	 *///Delete method is not neccesary
	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		// TODO Auto-generated method stub
		return null;
	}

}
