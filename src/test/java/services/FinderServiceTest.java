
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repositories.CategoryRepository;
import security.LoginService;
import utilities.AbstractTest;
import domain.Finder;
import domain.Member;
import domain.Proclaim;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService		finderService;

	@Autowired
	private CategoryRepository	category;


	/*
	 * TEST 1
	 * Requirement tested:
	 * 9. Every member has a finder. With it, they get the proclaims not taken.
	 * Finder will search according to the following criteria: registered date, a ticker, description, proclaim type.
	 * If criteria is not found, all results will be given.
	 * 
	 * - Analysis of sentence coverage of FinderService: 36%
	 * Total instructions: 197; Covered Instructions: 71
	 * 
	 * - Analysis of data coverage: 66.7%
	 * 
	 * Attribute: singleKey| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: registeredDate| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: beforeOrNot| Bad value: false | Normal value: true | Coverage: 100% |
	 * Attribute: creationDate| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: proclaims| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: category| Bad value: null | Normal value: Yes | Coverage: 100% |
	 */

	@Test
	public void test1() throws ParseException {
		final Object testingData[][] = {
			{
				//Positive test. Normal Search
				"member1", "", super.getEntityId("category1"), false, new SimpleDateFormat("yyyy/MM/dd").parse("2019/04/01"), null
			}, {
				//Negative test. Trying to use finder function with no logged user
				"", "", super.getEntityId("category2"), true, new SimpleDateFormat("yyyy/MM/dd").parse("2019/04/01"), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (int) testingData[i][2], (boolean) testingData[i][3], (Date) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	protected void template(final String username, final String singleKey, final int category, final boolean before, final Date date, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			Member m;
			m = (Member) this.category.findByUserAccount(LoginService.getPrincipal().getId());

			Finder f;
			f = m.getFinder();

			final Collection<Proclaim> result = this.finderService.searchWithRetain(singleKey, this.category.findOne(category), date, before);

			this.finderService.save(f, result);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
