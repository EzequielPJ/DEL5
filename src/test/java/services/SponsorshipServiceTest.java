
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Sponsorship;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	@Autowired
	SponsorshipService	sponsorshipService;


	/*
	 * Requirement tested: An actor who is authenticated as a sponsor must be able to manage his or her sponsorships, which includes creating and updating them.
	 * 
	 * * - Analysis of sentence coverage of SponsorshipService: 19.8%
	 * Total instructions: 288; Covered Instructions: 57
	 * Analysis of data coverage: 50%
	 * Attribute: Banner | Bad value: --- | Normal value: Yes | Coverage: 50% |
	 */
	@Test
	public void testUpdateSponsorship() {
		final Object testingData[][] = {
			{
				// Positive test
				"sponsor1", "sponsorship1", "http://www.hola.com", null
			}, {
				// Negative test: The business rule that has been violated: A sponsorship cannot be updated by another sponsor.
				"sponsor2", "sponsorship1", "http://www.troll.com", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.updateSponsorship((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void updateSponsorship(final String username, final int sponsorshipId, final String linkTPage, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			Sponsorship s;
			s = this.sponsorshipService.findOne(sponsorshipId);
			s.setBanner(linkTPage);
			this.sponsorshipService.save(s);
			this.sponsorshipService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
