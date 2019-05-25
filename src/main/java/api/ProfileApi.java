
package api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import security.LoginService;
import services.ProfileService;
import domain.Actor;
import domain.Profile;

@RestController
@RequestMapping("/profileApi")
public class ProfileApi {

	@Autowired
	private ProfileService	profile;


	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Collection<Profile> findAll() {

		final Actor a = this.profile.getActorByUser(LoginService.getPrincipal().getId());

		return this.profile.getProfilesByActorId(a.getId());
	}
}
