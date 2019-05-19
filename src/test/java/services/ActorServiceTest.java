
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	private ActorService	actorService;


	/*
	 * Requirement tested: An actor who is not authenticated must be able to:
	 * Register to the system as a company.
	 * Total instructions: 9099
	 * Covered instructions: 157
	 * Analysis of sentence coverage of actorService: 17.3%
	 * Analysis of data coverage: 5%
	 * Attribute: name | Bad value: --- | Normal value: Yes | Coverage: 50% |
	 * Attribute: surname | Bad value: --- | Normal value: Yes | Coverage: 50% |
	 * Attribute: email | Bad value: bad email | Normal value: Yes | Coverage: 20% |
	 * Attribute: vat | Bad value: --- | Normal value: Yes | Coverage: 20% |
	 * Others attribute about actor: Coverage: 0%
	 */
	@Test
	public void ActorTest() {
		final Object testingData[][] = {
			{
				//Positive test:
				null, "actorName", "actorSurname", "actor@us.es", "12312312Y", null
			}, {
				//Negative test: 
				null, "actorName", "actorSurname", "actorarrobaus.es", "12312312Y", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	protected void template(final String username, final String name, final String surname, final String email, final String vat, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			//			Company a;
			//			a = (Company) this.actorService.createActor(Authority.ADMIN);

			//			a.setName(name);
			//			a.setSurname(surname);
			//			a.setEmail(email);
			//			a.setVat(vat);
			//			a.setSpammer(false);

			//			CreditCard c;
			//			c = this.actorService.createCreditCard();
			//			c.setCvv(138);
			//			c.setExpiration(new Date());
			//			c.setHolder("John Doe");
			//			c.setMake("MASTERCARD");
			//			c.setNumber("1111222233334444");
			//			a.setCreditCard(c);
			//
			//			//			a.setAccount(this.actorService.userAccountAdapted("", "", Authority.COMPANY));
			//			a.setProfiles(new ArrayList<Profile>());
			//
			//			this.actorService.save(null, null, a, null, null);
			//			if (email.equals("actorarrobaus.es"))
			//				this.actorService.flushAdministrator();
			//			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
