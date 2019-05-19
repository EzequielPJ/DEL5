
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import security.Authority;
import security.LoginService;
import domain.Category;
import domain.Proclaim;

@Service
@Transactional
public class CategoryService extends AbstractService {

	@Autowired
	private Validator			validator;

	@Autowired
	private CategoryRepository	categoryRepository;


	public Collection<Category> findAll() {
		return this.categoryRepository.findAll();
	}

	public Category findOne(final int id) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		return this.categoryRepository.findOne(id);
	}

	public Category getCategoryFather(final int id) {
		return this.categoryRepository.getCategoryFather(id);
	}

	public Collection<Proclaim> findAllProclaimsByCategory(final int id) {
		return this.categoryRepository.findAllProclaimsByCategory(id);
	}

	public Collection<String> getNamesFromCategory() {
		return this.categoryRepository.getNamesFromCategory();
	}

	public Category createCategory() {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Category c;
		c = new Category();
		c.setName("");
		c.setCategories(new ArrayList<Category>());

		return c;
	}

	public Category save(final Category c) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Category saved;
		saved = this.categoryRepository.save(c);
		return saved;
	}

	public Category saveSubCategory(final int categoryParent, final Category newCategory) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Category result;
		result = this.categoryRepository.save(newCategory);

		Category aux;
		aux = this.categoryRepository.findOne(categoryParent);
		Collection<Category> category;
		category = aux.getCategories();
		category.add(result);
		aux.setCategories(category);

		return result;
	}

	public void deleteCategory(final int id) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Assert.isTrue(this.categoryRepository.findAllProclaimsByCategory(id).size() <= 2, "You can not delete this category because more than two proclaim are assocciate with it");
		Category c;
		c = this.categoryRepository.findOne(id);
		if (this.categoryRepository.findAllProclaimsByCategory(id).size() != 0) {
			Collection<Proclaim> pro;
			pro = this.categoryRepository.findAllProclaimsByCategory(id);
			for (final Proclaim proclaim : pro)
				proclaim.setCategory(null);
		}
		if (this.categoryRepository.getCategoryFather(id) != null) {
			Category cate;
			cate = this.categoryRepository.getCategoryFather(id);
			Collection<Category> colo;
			colo = cate.getCategories();
			colo.remove(c);
			cate.setCategories(colo);
		}
		this.categoryRepository.delete(c);
	}

	public Category recontract(final Category category, final BindingResult binding) {
		Category result = null;

		if (this.categoryRepository.getNamesFromCategory().contains(category.getName()))
			binding.rejectValue("name", "category.name.error");

		if (category.getId() == 0)
			result = category;
		else {
			result = this.categoryRepository.findOne(category.getId());
			result.setName(category.getName());
		}

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}
	public void flush() {
		this.categoryRepository.flush();
	}

}
