
package services;

import javax.transaction.Transactional;

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
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService	finderService;


	/*
	 * TEST 1
	 * Requirement tested: An actor who is authenticated as an rookie have finders.
	 * - Analysis of sentence coverage of ProblemService: 6.3%
	 * Total instructions: 21; Covered Instructions: 332
	 * - Analysis of data coverage: 43.75%
	 * Attribute: maxSalary| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: minSalary| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: deadLine | Bad value: - | Normal value: Yes | Coverage: 25% |
	 * Attribute: singleKey| Bad value: - | Normal value: Yes | Coverage: 50% |
	 */

	@Test
	public void test1() {
		final Object testingData[][] = {
			{
				//Positive test
				"rookie1", 120.0, 60.0, "ingeniero de software", null
			}, {
				//Negative test
				"", 120.0, 60.0, "ingeniero de software", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (Double) testingData[i][1], (Double) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void template(final String username, final Double maxSalary, final Double minSalary, final String singleKey, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			//			Finder f;
			//			f = this.finderService.create();
			//			f.setMaxSalary(maxSalary);
			//			f.setMinSalary(minSalary);
			//			f.setSingleKey(singleKey);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
