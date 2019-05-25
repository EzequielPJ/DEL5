
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import security.Authority;
import security.LoginService;
import domain.Category;
import domain.Finder;
import domain.Member;
import domain.Proclaim;

@Service
@Transactional
public class FinderService extends AbstractService {

	@Autowired
	private FinderRepository	repository;

	@Autowired
	private Validator			validator;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ProclaimService		serviceProclaim;


	public Finder findOne(final Finder finder) {
		return this.repository.findOne(finder.getId());
	}

	public Finder save(final Finder finder, final Collection<Proclaim> col) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER));
		Finder result;
		result = this.repository.save(finder);
		if (finder.getId() != 0) {
			result.setProclaims(col);
			result.setCreationDate(new Date());
		}
		return result;
	}

	public Finder create() {
		Finder finder;
		finder = new Finder();
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER));
		finder.setCreationDate(new Date());
		finder.setRegisteredDate(new Date());
		finder.setCategory(null);
		finder.setProclaims(new ArrayList<Proclaim>());
		return finder;
	}

	public Finder oldFinder() {
		Finder res;
		res = null;
		try {
			if (this.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
				final Member h = (Member) this.actorService.findByUserAccount(LoginService.getPrincipal().getId());
				res = h.getFinder();
			}
		} catch (final IllegalArgumentException e) {
		}
		return res;
	}

	public Finder reconstruct(final Finder finder, final BindingResult binding) {

		Finder result;
		result = new Finder();

		Member h;
		h = (Member) this.repository.findActorByUserAccountId(LoginService.getPrincipal().getId());
		result = h.getFinder();
		result.setSingleKey(finder.getSingleKey());
		result.setCategory(finder.getCategory());
		result.setRegisteredDate(finder.getRegisteredDate());
		result.setBeforeOrNot(finder.isBeforeOrNot());
		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void clear(final Finder aux) {
		aux.setProclaims(new ArrayList<Proclaim>());
		aux.setCreationDate(new Date());
	}

	public List<Proclaim> searchWithRetain(final String s, final Category category, final Date registered, final boolean before) {
		List<Proclaim> result;

		result = new ArrayList<Proclaim>(this.serviceProclaim.findNoAssigned());

		if (s != "" || s != null)
			result.retainAll(this.repository.findBySingleKey(s));

		if (category != null)
			result.retainAll(this.repository.findByCategory(category.getId()));

		if (registered != null)
			result.retainAll(before ? this.repository.findByRegisteredBeforeDate(registered) : this.repository.findByRegisteredAfterDate(registered));

		if (result.isEmpty())
			result = new ArrayList<Proclaim>(this.serviceProclaim.findNoAssigned());

		return result;
	}
	public void flush() {
		this.repository.flush();
	}
}
