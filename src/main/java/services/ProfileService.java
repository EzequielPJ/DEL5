
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProfileRepository;
import security.LoginService;
import domain.Actor;
import domain.Profile;

@Service
@Transactional
public class ProfileService {

	@Autowired
	private ProfileRepository	profileRepository;
	@Autowired
	private Validator			validator;


	public Actor getActorByUser(final int id) {
		return this.profileRepository.findActorByUserAccountId(id);
	}

	public Collection<Profile> getProfilesByActorId(final int id) {
		return this.profileRepository.getProfilesByActorId(id);
	}

	public Profile findOne(final int id) {
		Actor a;
		a = this.profileRepository.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Profile profile;
		profile = this.profileRepository.findOne(id);

		Assert.isTrue(profile.getActor() == a, "You don´t have access, you can only see your profiles");

		return profile;
	}

	public Profile createProfile() {
		Profile result;
		result = new Profile();
		Actor a;
		a = this.profileRepository.findActorByUserAccountId(LoginService.getPrincipal().getId());
		result.setNick("");
		result.setLink("");
		result.setSocialNetworkName("");
		result.setActor(a);
		return result;
	}

	public Profile save(final Profile p) {

		Actor a;
		a = this.profileRepository.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Profile modify;

		if (p.getId() == 0)
			modify = this.profileRepository.save(p);
		else {
			Assert.isTrue(p.getActor() == a, "You don´t have access, you can only update your profiles");
			modify = this.profileRepository.save(p);
		}

		return modify;
	}

	public void deleteProfile(final int id) {

		Actor a;
		a = this.profileRepository.findActorByUserAccountId(LoginService.getPrincipal().getId());

		Profile profile;
		profile = this.profileRepository.findOne(id);

		Assert.isTrue(profile.getActor() == a, "You don´t have access, you can only delete your profiles");

		this.profileRepository.delete(profile);

	}

	public Profile reconstruct(final Profile profile, final BindingResult binding) {
		Profile result;
		if (profile.getId() == 0) {
			result = profile;
			result.setActor(this.profileRepository.findActorByUserAccountId(LoginService.getPrincipal().getId()));
		} else {
			result = this.profileRepository.findOne(profile.getId());
			result.setLink(profile.getLink());
			result.setNick(profile.getNick());
			result.setSocialNetworkName(profile.getSocialNetworkName());
			result.setActor(this.profileRepository.findActorByUserAccountId(LoginService.getPrincipal().getId()));
		}
		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	public void flush() {
		this.profileRepository.flush();
	}
}
