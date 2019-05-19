
package services;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Finder;

@Service
@Transactional
public class FinderService extends AbstractService {

	@Autowired
	private FinderRepository	repository;

	@Autowired
	private Validator			validator;

	@Autowired
	private ActorService		actorService;


	public Finder findOne(final Finder finder) {
		return this.repository.findOne(finder.getId());
	}

	//	public Collection<Position> findBySingleKey(final String singleKey) {
	//		List<Position> result;
	//
	//		result = new ArrayList<Position>(this.repository.findBySingleKey(singleKey));
	//
	//		Integer finderSize;
	//		finderSize = Integer.valueOf(System.getProperty("finderSize"));
	//
	//		if (result.size() > finderSize)
	//			result = result.subList(0, finderSize);
	//
	//		return result;
	//	}
	//

	//	public Finder save(final Finder finder, final Collection<Position> col) {
	//		//		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ROOKIE));
	//		Finder result;
	//		result = this.repository.save(finder);
	//		if (finder.getId() != 0) {
	//			result.setPositions(col);
	//			result.setCreationDate(new Date());
	//		}
	//		return result;
	//	}

	//	public Finder create() {
	//
	//		Finder finder;
	//		finder = new Finder();
	//		try {
	//			//			Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ROOKIE));
	//			finder.setCreationDate(new Date());
	//			finder.setPositions(new ArrayList<Position>());
	//		} catch (final IllegalArgumentException e) {
	//
	//			finder.setSingleKey("");
	//
	//		}
	//		return finder;
	//	}

	public Finder oldFinder() {
		Finder res;
		res = null;
		try {
			//			if (this.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ROOKIE)) {
			//				final Rookie h = (Rookie) this.actorService.findByUserAccount(LoginService.getPrincipal().getId());
			//				res = h.getFinder();
			//			}
		} catch (final IllegalArgumentException e) {
		}
		return res;
	}

	public Finder reconstruct(final Finder finder, final BindingResult binding) {

		Finder result;
		result = new Finder();

		try {
			//			Rookie h;
			//			h = (Rookie) this.repository.findActorByUserAccountId(LoginService.getPrincipal().getId());
			//			result = h.getFinder();
			result.setSingleKey(finder.getSingleKey());
			//			result.setDeadline(finder.getDeadline());
			//			result.setMinSalary(finder.getMinSalary());
			//			result.setMaxSalary(finder.getMaxSalary());
		} catch (final IllegalArgumentException e) {
			result = finder;
		}

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	//	public void clear(final Finder aux) {
	//		aux.setPositions(new ArrayList<Position>());
	//		aux.setCreationDate(new Date());
	//	}
	//
	//	public List<Position> searchWithRetain(final String s, final Date deadlineDate, final Double minSalary, final Double maxSalary) {
	//		final List<Position> result;
	//
	//		//		result = new ArrayList<Position>(this.servicePosition.getPublicPositions());
	//		//
	//		//		if (s != "" || s != null)
	//		//			result.retainAll(this.repository.findBySingleKey(s));
	//		//		if (deadlineDate != null)
	//		//			result.retainAll(this.repository.findByDate(deadlineDate));
	//		//		if (minSalary != null && maxSalary != null)
	//		//			result.retainAll(this.repository.findBySalary(minSalary, maxSalary));
	//
	//		//		return result;
	//		return null;
	//	}
}
