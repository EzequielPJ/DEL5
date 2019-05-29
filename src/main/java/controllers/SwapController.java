
package controllers;

import java.util.Arrays;
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
import security.UserAccount;
import services.SwapService;
import domain.Collaborator;
import domain.Swap;

@Controller
@RequestMapping("/swap/collaborator")
public class SwapController extends BasicController {

	@Autowired
	private SwapService	swapService;


	@RequestMapping(value = "/listMySwap")
	public ModelAndView myList() {
		ModelAndView result = null;
		UserAccount user;
		Collaborator c;
		user = LoginService.getPrincipal();
		if (this.swapService.findAuthority(user.getAuthorities(), Authority.COLLABORATOR)) {
			c = (Collaborator) this.swapService.getActorByUserId(user.getId());
			result = super.listModelAndView("swaps", "swap/list", this.swapService.getSwapsByCollaboratorId(c.getId()), "swap/collaborator/listMySwap.do");
		}
		return result;
	}

	@RequestMapping(value = "/listSwap")
	public ModelAndView listSwap() {
		ModelAndView result = null;
		UserAccount user;
		Collaborator c;
		user = LoginService.getPrincipal();
		if (this.swapService.findAuthority(user.getAuthorities(), Authority.COLLABORATOR)) {
			c = (Collaborator) this.swapService.getActorByUserId(user.getId());
			result = super.listModelAndView("swaps", "swap/list", this.swapService.getSwapsPendingByCollaboratorId(c.getId()), "swap/collaborator/listMySwap.do");
			result.addObject("pend", true);
		}
		return result;
	}
	@RequestMapping(value = "/list")
	public ModelAndView list() {
		Assert.isTrue(this.swapService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR), "You must be a collaborator");
		ModelAndView result = null;
		UserAccount user;
		Collaborator c;
		user = LoginService.getPrincipal();
		c = (Collaborator) this.swapService.getActorByUserId(user.getId());
		if (c.getComission() != null) {
			result = super.listModelAndView("actors", "actor/list", this.swapService.findAllCollaboratorByComission(c.getId(), c.getComission().getId()), "swap/collaborator/list.do");
			result.addObject("comis", true);
		} else {
			result = super.listModelAndView("actors", "actor/list", this.swapService.findAll(), "swap/collaborator/list.do");
			result.addObject("comis", false);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int idCollaborator) {
		Assert.isTrue(this.swapService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR), "You must be a collaborator");
		ModelAndView result;
		/*
		 * UserAccount user;
		 * user = LoginService.getPrincipal();
		 * Collaborator c;
		 * c = (Collaborator) this.swapService.getActorByUserId(user.getId());
		 */
		result = super.create(this.swapService.createSwap(idCollaborator), "swap/edit", "swap/collaborator/edit.do", "swap/collaborator/listMySwap.do");
		result.addObject("statusCol", Arrays.asList("pending"));
		result.addObject("send", true);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Swap swap, final BindingResult binding) {
		Assert.isTrue(this.swapService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR), "You must be a collaborator");
		ModelAndView result = null;
		result = super.save(swap, binding, "swap.commit.error", "swap/edit", "swap/collaborator/edit.do", "redirect:listMySwap.do", "redirect:listMySwap.do");
		result.addObject("statusCol", Arrays.asList("accepted", "rejected"));
		return result;
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView update(@RequestParam final int idSwap) {
		Swap aux;
		aux = this.swapService.findOne(idSwap);

		UserAccount user;
		user = LoginService.getPrincipal();

		Collection<Swap> col;
		Collaborator c;
		c = (Collaborator) this.swapService.getActorByUserId(user.getId());
		col = this.swapService.getSwapsPendingByCollaboratorId(c.getId());
		Assert.isTrue(col.contains(aux), "You don't have permission to do this");
		ModelAndView result;
		result = super.edit(aux, "swap/edit", "swap/collaborator/edit.do", "swap/collaborator/listSwap.do");
		result.addObject("send", false);
		result.addObject("statusCol", Arrays.asList("accepted", "rejected"));
		return result;
	}
	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Swap s;
		s = (Swap) e;
		s = this.swapService.reconstruct(s, binding);
		Swap saved;
		saved = this.swapService.save(s);
		result = new ModelAndView(nameResolver);
		result.addObject("saved", saved);
		UserAccount user;
		user = LoginService.getPrincipal();
		Collaborator c;
		c = (Collaborator) this.swapService.getActorByUserId(user.getId());
		if (saved.getSender().getId() == c.getId()) {
			result.addObject("send", true);
			result.addObject("statusCol", Arrays.asList("pending"));
		} else {
			result.addObject("statusCol", Arrays.asList("accepted", "rejected"));
			result.addObject("send", false);
		}
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		// TODO Auto-generated method stub
		return null;
	}

}
