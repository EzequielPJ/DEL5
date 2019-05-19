
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
import services.CategoryService;
import domain.Category;

@Controller
@RequestMapping("/category/administrator")
public class CategoryController extends BasicController {

	@Autowired
	private CategoryService	serviceCategory;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		Assert.isTrue(this.serviceCategory.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		return super.listModelAndView("categories", "category/list", this.serviceCategory.findAll(), "category/administrator/list.do");
	}

	//	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//	public ModelAndView list(@CookieValue(value = "language", required = false) String lang) {
	//		ModelAndView result;
	//		result = new ModelAndView("category/list");
	//		result.addObject("categories", this.serviceCategory.findAll());
	//		if (lang == null || lang == "")
	//			lang = "en";
	//		result.addObject("lang", lang);
	//		result.addObject("requestURI", "category/administrator/list.do");
	//		return result;
	//	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(defaultValue = "0") final int parent) {
		Assert.isTrue(this.serviceCategory.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN), "You must be an admin");
		ModelAndView result;
		result = super.create(this.serviceCategory.createCategory(), "category/edit", "category/administrator/edit.do", "/category/administrator/list.do");
		result.addObject("parent", parent);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@RequestParam(defaultValue = "0") final int parent, final Category category, final BindingResult binding) {
		Assert.isTrue(this.serviceCategory.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN), "You must be an admin");
		ModelAndView result;
		result = super.save(category, binding, "category.commit.error", "category/edit", "category/administrator/edit.do", "redirect:list.do", "redirect:list.do");
		result.addObject("parent", parent);
		if (parent != 0) {
			Collection<Category> colC;
			Category cat;
			cat = this.serviceCategory.findOne(parent);
			colC = cat.getCategories();
			colC.add((Category) result.getModel().get("saved"));
			cat.setCategories(colC);
			this.serviceCategory.save(cat);
		}
		return result;
	}

	/*
	 * @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	 * public ModelAndView edit(@RequestParam final boolean check, @RequestParam final int parent, @Valid final Category category, final BindingResult binding) {
	 * ModelAndView result;
	 * 
	 * if (binding.hasErrors())
	 * result = this.createEditModelAndView(category);
	 * else
	 * try {
	 * if (check) {
	 * if (parent == 0)
	 * this.serviceCategory.saveParent(category);
	 * else
	 * this.serviceCategory.saveSubCategory(parent, category);
	 * 
	 * } else
	 * this.serviceCategory.saveParent(category);
	 * 
	 * result = new ModelAndView("redirect:list.do");
	 * } catch (final Throwable oops) {
	 * result = this.createEditModelAndView(category, "category.commit.error");
	 * }
	 * return result;
	 * }
	 *///Updating a warranty

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		Assert.isTrue(this.serviceCategory.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN), "You must be an admin");
		ModelAndView result;
		Category aux;
		aux = this.serviceCategory.findOne(id);
		Assert.notNull(aux);
		result = super.edit(this.serviceCategory.findOne(id), "category/edit", "category/administrator/edit.do", "category/administrator/list.do");
		result.addObject("category", aux);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int cat) {
		ModelAndView result;
		Assert.isTrue(this.serviceCategory.findAllProclaimsByCategory(cat).size() <= 2, "You can not delete this category because more than 2 proclaims are assocciate with it");
		result = super.delete(this.serviceCategory.findOne(cat), "category.commit.error", "category/edit", "category/administrator/edit.do", "redirect:list.do", "redirect:list.do");
		return result;
	}

	//Delete a category
	/*
	 * @RequestMapping(value = "/delete", method = RequestMethod.GET)
	 * public ModelAndView delete(final int cat) {
	 * ModelAndView result;
	 * Category category = null;
	 * try {
	 * category = this.serviceCategory.findOne(cat);
	 * this.serviceCategory.deleteCategory(category.getId());
	 * result = new ModelAndView("redirect:list.do");
	 * } catch (final Throwable oops) {
	 * result = this.createEditModelAndView(category, "category.commit.error");
	 * }
	 * 
	 * return result;
	 * }
	 */

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Category category;
		category = (Category) e;
		category = this.serviceCategory.recontract(category, binding);
		Category saved;
		saved = this.serviceCategory.save(category);
		result = new ModelAndView(nameResolver);
		result.addObject("saved", saved);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		ModelAndView result;
		Category category;
		category = (Category) e;
		if (this.serviceCategory.findAllProclaimsByCategory(category.getId()).size() <= 2)
			this.serviceCategory.deleteCategory(category.getId());
		result = new ModelAndView(nameResolver);
		return result;
	}
}
