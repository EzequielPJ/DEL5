
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CustomisationSystemRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.CustomisationSystem;
import forms.CustomForm;

@Service
@Transactional
public class CustomisationSystemService extends AbstractService {

	@Autowired
	private CustomisationSystemRepository	repository;
	@Autowired
	private Validator						validator;
	private int								i	= 0;


	public void flagSpam(final int idActor) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Actor a;
		a = this.repository.findActorByUserAccountId(idActor);
		a.setSuspicious(true);
	}

	@Cacheable(value = "cust")
	public CustomisationSystem findUnique() {
		System.out.println("Entro en el findUnique: " + this.i);
		this.i++;
		return this.repository.findAll().get(0);
	}
	public Collection<Actor> findAllNoEnabledActors() {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		return this.repository.findAllNoEnabledActors();
	}

	@CacheEvict(value = "cust", allEntries = true)
	public CustomisationSystem save(final CustomForm custom) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		CustomisationSystem aux;
		aux = this.fromCustomObjetFormToDomain(custom);
		aux.setId(this.findUnique().getId());
		aux.setVersion(this.findUnique().getVersion());
		return this.repository.save(aux);
	}
	public void banActor(final int id) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Actor a;
		a = this.repository.findActor(id);
		a.getAccount().setEnabled(false);
	}

	public void unBanActor(final int id) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Actor a;
		a = this.repository.findActor(id);
		a.getAccount().setEnabled(true);
	}

	public CustomForm reconstruct(final CustomForm c, final BindingResult binding) {
		boolean check;
		check = false;
		try {
			Long.valueOf(c.getPhonePrefix());
		} catch (final Exception e) {
			check = true;
		}
		this.validator.validate(c, binding);
		if (check)
			binding.rejectValue("phonePrefix", "custom.worng.prefix");

		if (binding.hasErrors())
			throw new ValidationException();

		return c;
	}
	public CustomForm fromCustomisationSystemToObjetForm(final CustomisationSystem c) {
		CustomForm form;
		form = new CustomForm();

		form.setBanner(c.getBanner());
		form.setHoursFinder(c.getHoursFinder());
		form.setMessage(c.getMessage());
		form.setPhonePrefix(c.getPhonePrefix());
		form.setResultFinder(c.getResultFinder());
		form.setSystemName(c.getSystemName());
		form.setSpamwordsEnglish(c.getSpamwords().get("en"));
		form.setSpamwordsSpanish(c.getSpamwords().get("es"));
		form.setCreditCardMakes(c.getCreditCardMakes());

		return form;
	}
	public CustomisationSystem fromCustomObjetFormToDomain(final CustomForm c) {
		CustomisationSystem form;
		form = new CustomisationSystem();

		form.setBanner(c.getBanner());
		form.setHoursFinder(c.getHoursFinder());
		form.setMessage(c.getMessage());
		form.setPhonePrefix(c.getPhonePrefix());
		form.setResultFinder(c.getResultFinder());
		form.setSystemName(c.getSystemName());
		Map<String, String> spamWords;
		spamWords = new HashMap<String, String>();

		spamWords.put("en", c.getSpamwordsEnglish());
		spamWords.put("es", c.getSpamwordsSpanish());

		form.setSpamwords(spamWords);
		form.setCreditCardMakes(c.getCreditCardMakes());

		return form;
	}

	public Map<String, Double> marcadorNumerico() {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Map<String, Double> result;
		result = new HashMap<String, Double>();
		result.put("ratioMemberComission", this.repository.ratioMemberWithComission());
		return result;
	}

	public Map<String, Double[]> marcadorNumericoArray() {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Map<String, Double[]> result;
		result = new HashMap<String, Double[]>();
		result.put("MinMaxAvgStdevProclaimsByStudents", this.repository.minMaxAvgStddevOfProclaimsByStudents());
		result.put("MinMaxAvgStdevProclaimsByMembers", this.repository.minMaxAvgStddevOfTakenProclaimsByMember());
		result.put("MinMaxAvgStdevFinderResults", this.repository.minMaxAvgStddevOfFinderResults());
		result.put("MinMaxAvgStdevCollaboratorPerComissions", this.repository.minMaxAvgStddevOfCollaboratorsPerComission());
		result.put("MinMaxAvgStdevNotesPerEvent", this.repository.minMaxAvgStddevOfNotesPerEvent());
		return result;
	}

	public Map<String, List<String>> nearestEvents() {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
		Map<String, List<String>> result;
		result = new HashMap<String, List<String>>();
		result.put("nearestEvents", this.repository.eventsNearestToday().size() <= 3 ? this.repository.eventsNearestToday() : this.repository.eventsNearestToday().subList(0, 2));
		return result;
	}

	public Map<String, Long> histogram() {
		Map<String, Long> result;

		result = new HashMap<String, Long>();

		final Collection<Object[]> repo = this.repository.histogramNumberProclaimPerCategory();

		for (final Object[] o : repo)
			result.put((String) o[0], (Long) o[1]);

		return result;
	}
}
