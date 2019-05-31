
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Profile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProfileServiceTest extends AbstractTest {

	@Autowired
	private ProfileService	service;


	/**
	 * An actor who is authenticated must be able to:
	 * 1. Manage his or her social profiles, which includes listing, showing, creating, updating, and deleting them.
	 * 
	 * Sentence Coverage: 40.6 %
	 * Total Instructions: 165
	 * Instructions Covered: 67
	 * 
	 * Data Coverage. 50%
	 * 
	 * Atributo Bad Value Good Value
	 * ==============================================================
	 * Link | - | Yes
	 * Nick | - | Yes
	 * SocialNetwork | - | Yes
	 */

	@Test
	public void driver() {
		final Object[][] objects = {
			{
				"student1", 0, null
			}, {
				"member1", super.getEntityId("profile2"), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < objects.length; i++)
			this.template((String) objects[i][0], (int) objects[i][1], (Class<?>) objects[i][2]);
	}

	protected void template(final String username, final int i, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {
			super.authenticate(username);

			Profile res;
			res = i == 0 ? this.service.createProfile() : this.service.findOne(i);

			res.setLink("https://www.probando.es");
			res.setNick("Tuenti");
			res.setSocialNetworkName("SocialNetwork1");

			this.service.save(res);
			this.service.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
