
package services;

/*
 * SampleTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Sponsor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	ActorService	actorService;


	/*
	 * Requirement tested: An actor who is not authenticated must be able to register as a sponsor
	 * - Analysis of sentence coverage of ActorService: 12.9%
	 * Total instructions: 1173; Covered Instructions: 151
	 * - Analysis of data coverage: 66.67%
	 * Attribute: Name | Bad value: null | Normal value: Yes | Coverage: 100% |
	 * Attribute: Surname | Bad value: --- | Normal value: Yes | Coverage: 50% |
	 * Attribute: Email | Bad value: --- | Normal value: Yes | Coverage: 50% |
	 * Attribute: Phone | Bad value: --- | Normal value: Yes | Coverage: 50% | Reason: It's optional
	 * Attribute: Photo | Bad value: --- | Normal value: Yes | Coverage: 100% | Reason: It's optional
	 * Attribute: Title | Bad value: --- | Normal value: Yes | Coverage: 50% |
	 */
	@Test
	public void testRegisterSponsor() {
		final Object testingData[][] = {
			{
				// Positive test
				"Belén", "Garrido", "BGL", "belen@example.com", "666444333", "http://www.hola.com", "DP", null
			}, {
				// Negative test: The business rule that has been violated: Name cannot be blank
				null, "Garrido", "BGL", "belen@example.com", "666444333", "http://www.hola.com", "DP", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerSponsor(i, (String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void registerSponsor(final int i, final String name, final String surname, final String middleName, final String email, final String phone, final String photo, final String title, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.unauthenticate();
			Sponsor s;
			s = (Sponsor) this.actorService.createActor(Authority.SPONSOR);
			s.setName(name);
			s.setSurname(surname);
			s.setEmail(email);
			s.setPhone(phone);
			s.setPhoto(photo);
			UserAccount user;
			user = s.getAccount();
			user.setUsername("springJUnit4Sponsor" + i);
			user.setPassword(this.actorService.hashPassword("sponsorJunit4"));
			s.setAccount(user);
			this.actorService.save(null, null, null, null, s);

			this.actorService.flushSponsor();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
