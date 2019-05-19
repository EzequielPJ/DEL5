
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

import security.LoginService;
import services.BoxService;
import domain.Box;

@Controller
@RequestMapping("/box")
public class BoxController extends BasicController {

	@Autowired
	BoxService	boxService;

	Boolean		val	= true;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		return super.listModelAndView("boxes", "box/list", this.boxService.getBoxesFromUserAccount(LoginService.getPrincipal().getId()), "box/list.do");
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(defaultValue = "0") final int parent) {
		if (parent != 0)
			Assert.isTrue(this.boxService.getBoxesFromUserAccount(LoginService.getPrincipal().getId()).contains(this.boxService.findOne(parent)), "You don´t have access");
		ModelAndView result;
		result = super.create(this.boxService.createBox(), "box/edit", "box/edit.do", "/box/list.do");
		result.addObject("parent", parent);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(@RequestParam(defaultValue = "0") final int parent, final Box box, final BindingResult binding) {
		ModelAndView result;
		result = super.save(box, binding, "box.commit.error", "box/edit", "box/edit.do", "redirect:list.do", "redirect:list.do");
		result.addObject("parent", parent);
		if (parent != 0) {
			Collection<Box> colboxp;
			Box bo;
			bo = this.boxService.findOne(parent);
			colboxp = bo.getBoxes();
			colboxp.add((Box) result.getModel().get("saved"));
			bo.setBoxes(colboxp);
			this.boxService.save(bo);
		}
		return result;
	}
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		ModelAndView result;
		Box b;
		b = this.boxService.findOne(id);
		Assert.isTrue(this.boxService.getActorByUserAccount(LoginService.getPrincipal().getId()).getBoxes().contains(b) || b.isFromSystem(), "You don't have permission to do this");
		result = super.edit(this.boxService.findOne(id), "box/edit", "box/edit.do", "/box/list.do");
		result.addObject("box", b);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEntity(@RequestParam final int id) {
		ModelAndView result;
		result = super.delete(this.boxService.findOne(id), "box.commit.error", "box/edit", "box/edit.do", "redirect:list.do", "redirect:list.do");
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result = null;
		Box box;
		box = (Box) e;
		box = this.boxService.reconstruct(box, binding);
		Box saved;
		saved = this.boxService.save(box);
		result = new ModelAndView(nameResolver);
		result.addObject("saved", saved);
		result.addObject("idSaved", saved.getId());
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		ModelAndView result;
		Box box;
		box = (Box) e;
		if (!box.isFromSystem())
			this.boxService.delete(box);
		result = new ModelAndView(nameResolver);
		return result;
	}

}
