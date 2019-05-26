
package api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	private ProfileService	service;


	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findAll() {

		ResponseEntity<?> result = null;

		Actor a;
		try {
			a = this.service.getActorByUser(LoginService.getPrincipal().getId());
			result = new ResponseEntity<>(this.service.getProfilesByActorId(a.getId()), HttpStatus.OK);
		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Please, use a valid account", HttpStatus.UNAUTHORIZED);
		}

		return result;
	}
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findProfile(@RequestParam final int id) {
		ResponseEntity<?> result = null;

		Actor a;
		try {
			a = this.service.getActorByUser(LoginService.getPrincipal().getId());
			result = new ResponseEntity<>(this.service.findOne(id), HttpStatus.OK);
		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Element not found", HttpStatus.NOT_FOUND);
		}

		return result;
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> save(@RequestBody final Profile profile) {

		ResponseEntity<?> result = null;

		Actor a;
		try {
			a = this.service.getActorByUser(LoginService.getPrincipal().getId());
			result = new ResponseEntity<>(this.service.save(profile), HttpStatus.OK);
		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Element not found", HttpStatus.NOT_FOUND);
		}

		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@RequestParam final int id) {

		ResponseEntity<?> result = null;

		Actor a;
		try {

			a = this.service.getActorByUser(LoginService.getPrincipal().getId());
			this.service.deleteProfile(id);
			result = new ResponseEntity<>("Profile deleted", HttpStatus.OK);
		} catch (final Throwable oops) {
			result = new ResponseEntity<>("Element not found", HttpStatus.NOT_FOUND);
		}

		return result;
	}
}
