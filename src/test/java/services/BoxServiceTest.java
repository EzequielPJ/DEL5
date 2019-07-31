
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Box;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BoxServiceTest extends AbstractTest {

	@Autowired
	private BoxService	boxService;


	/*
	 * Requirement tested: An actor who is authenticated must be able to:
	 * Update a box that is not system box.
	 * Total instructions: 302
	 * Covered instructions: 53
	 * Analysis of sentence coverage of boxService: 17.5%
	 * Analysis of data coverage: 100%
	 * Attribute: name | Bad value: null | Normal value: Yes | Coverage: 100% |
	 */
	@Test
	public void BoxTest1() {
		final Object testingData[][] = {
			{
				//Positive test: Create a box
				"collaborator2", "pruebaBox", null
			}, {
				//Negative test: The business rule that has been violated: Box name can not be null
				"collaborator2", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void template(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			//create a box
			Box b;
			b = this.boxService.createBox();
			b.setName(name);
			this.boxService.save(b);
			this.boxService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	@Test
	public void BoxTestDeleteFromSystem() {
		final Object testingData[][] = {
			{
				//Negative test: Delete a system box
				"collaborator2", IllegalArgumentException.class
			}
		};

		this.template((String) testingData[0][0], (Class<?>) testingData[0][1]);
	}
	private void template(final String string, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {
			super.authenticate(string);
			this.boxService.delete(this.boxService.findOne(super.getEntityId("box21")));
			this.boxService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
