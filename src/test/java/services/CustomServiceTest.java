
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CustomisationSystem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CustomServiceTest extends AbstractTest {

	@Autowired
	private ActorService				actorService;

	@Autowired
	private CustomisationSystemService	customService;


	/*
	 * TEST 1
	 * Requirement tested: An actor who is authenticated as an administrator must be able to display a dashboard
	 * - Analysis of sentence coverage of CustomisationSystemService:26.5%
	 * Total instructions: 381; Covered Instructions: 101
	 * 
	 * - Analysis of data coverage: 0%
	 */
	@Test
	public void CustomTest() {
		final Object testingData[][] = {
			{
				//Positive test:
				"admin1", null
			}, {
				//Negative test: 
				"student2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void template1(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			super.authenticate(username);

			this.customService.marcadorNumericoArray();

			this.customService.nearestEvents();

			this.customService.marcadorNumerico();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void EditCustomTest() {
		final Object testingData[][] = {
			{
				//Negative test: System name empty
				"admin1", null, ConstraintViolationException.class
			}
		};

		this.templateEditCustom((String) testingData[0][0], (String) testingData[0][1], (Class<?>) testingData[0][2]);
	}

	protected void templateEditCustom(final String username, final String param, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			super.authenticate(username);

			CustomisationSystem c;
			c = this.customService.findUnique();

			c.setSystemName(param);

			this.customService.save(this.customService.fromCustomisationSystemToObjetForm(c));
			this.customService.flush();
			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
