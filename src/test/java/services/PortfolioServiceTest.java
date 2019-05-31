
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import utilities.AbstractTest;
import domain.Collaborator;
import domain.MiscellaneousReport;
import domain.Portfolio;
import domain.StudyReport;
import domain.WorkReport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PortfolioServiceTest extends AbstractTest {

	@Autowired
	private PortfolioService			service;

	@Autowired
	private WorkReportService			serviceWork;

	@Autowired
	private StudyReportService			serviceStudy;

	@Autowired
	private MiscellaneousReportService	serviceMisc;


	/**
	 * An actor who is authenticated as collaborator must be able to:
	 * 1. Collaborators can manage their portfolio. Every portfolio has an only personal record,
	 * zero or more work reports, zero or more study report, or more miscellaneous data.
	 * 
	 * Sentence Coverage: 55.3%
	 * Total Instructions: 228
	 * Instructions Covered: 126
	 * 
	 * Data Coverage. 40.7%
	 * 
	 * Atributte Bad Value Good Value
	 * ==============================================================
	 * title | - | Yes | 50%
	 * moment | - | Yes | 50%
	 * fullName | - | Yes | 50 %
	 * address | - | Yes | 50%
	 * phone | - | Yes | 50%
	 * workReport | - | Yes | 25%
	 * studyReport | - | Yes | 25%
	 * miscellaneousReport | - | Yes | 25%
	 * 
	 */
	@Test
	public void driverPortfolio() {
		final Object[][] testingData = {
			{
				"collaborator1", null
			}, {
				"collaborator1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templatePortfolio((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	public void templatePortfolio(final String auth, final Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticate(auth);
			Collaborator c;
			c = (Collaborator) this.service.findActorByUserAccountId(LoginService.getPrincipal().getId());
			Portfolio p;
			p = c.getPortfolio();

			if (expected == null)
				p.setTitle("JUNIT4");
			else {
				p = this.service.create();
				p.setAddress("address");
				p.setFullName("fullName");
				p.setPhone("+34 955251416");
				p.setTitle("Change");
			}
			this.service.save(p);
			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * 
	 * 13. For every work report, users will have to register the start and
	 * the end date (this last can be optional), the name of the business where the member
	 * has worked or is working and a short piece of the text about the job done.
	 * 
	 * Sentence Coverage: 38.3%
	 * Total Instructions: 102
	 * Instructions Covered: 266
	 * 
	 * Data Coverage. 50%
	 * 
	 * Atributte Bad Value Good Value
	 * ==============================================================
	 * title | - | Yes | 50%
	 * moment | - | Yes | 50%
	 * startDate | - | Yes | 50%
	 * endDate | - | Yes | 50%
	 * businessName | - | Yes | 50%
	 * text | - | Yes | 50%
	 * 
	 */

	@Test
	public void driverWorkReport() {
		final Object[][] testingData = {
			{
				"collaborator1", 0, null
			}, {
				"collaborator2", super.getEntityId("workReport1"), IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateWorkReport((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void templateWorkReport(final String auth, final int report, final Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticate(auth);

			Collaborator c;
			c = (Collaborator) this.service.findActorByUserAccountId(LoginService.getPrincipal().getId());

			Portfolio p;
			p = c.getPortfolio();

			WorkReport saveTo = null;

			if (report == 0) {
				saveTo = this.serviceWork.create();
				saveTo.setBusinessName("JUNIT4");
				saveTo.setEndDate(this.convertedFrom("2020/05/13"));
				saveTo.setStartDate(this.convertedFrom("2018/05/13"));
				saveTo.setText("Texto");
				saveTo.setTitle("Title1");
			} else {
				saveTo = this.serviceWork.findOne(report);
				saveTo.setBusinessName("JUNIT4");
				saveTo.setEndDate(this.convertedFrom("2020/05/13"));
				saveTo.setStartDate(this.convertedFrom("2018/05/13"));
			}

			this.serviceWork.save(saveTo, p.getId());
			this.serviceWork.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * 
	 * 13. For every
	 * study report, it must be saved the current course (the current course is understood as
	 * the highest course in which is registered), the moment when he/she began the degree
	 * and the percentage of credits course, an optional average and an optional end date.
	 * 
	 * Sentence Coverage: 41.8%
	 * Total Instructions: 107
	 * Instructions Covered: 256
	 * 
	 * Data Coverage. 43.8%
	 * 
	 * Atributte Bad Value Good Value
	 * ==============================================================
	 * title | - | Yes | 50%
	 * moment | - | Yes | 50%
	 * startDate | - | Yes | 50%
	 * endDate | - | Yes | 50%
	 * course | - | Yes | 50%
	 * average | - | Yes | 25%
	 * percentajeCredits | - | Yes | 25%
	 * 
	 */

	@Test
	public void driverStudyReport() {
		final Object[][] testingData = {
			{
				"collaborator1", 0, null
			}, {
				"collaborator2", super.getEntityId("studyReport1"), IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateStudyReport((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void templateStudyReport(final String auth, final int report, final Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticate(auth);

			Collaborator c;
			c = (Collaborator) this.service.findActorByUserAccountId(LoginService.getPrincipal().getId());

			Portfolio p;
			p = c.getPortfolio();

			StudyReport saveTo = null;

			if (report == 0) {
				saveTo = this.serviceStudy.create();
				saveTo.setEndDate(this.convertedFrom("2020/05/13"));
				saveTo.setStartDate(this.convertedFrom("2018/05/13"));
				saveTo.setAverage(5.6);
				saveTo.setPercentajeCredits(25.0);
				saveTo.setCourse("QUINTO");
				saveTo.setTitle("Title1");
			} else {
				saveTo = this.serviceStudy.findOne(report);
				saveTo.setEndDate(this.convertedFrom("2020/05/13"));
				saveTo.setStartDate(this.convertedFrom("2018/05/13"));
			}

			this.serviceStudy.save(saveTo, p.getId());
			this.serviceStudy.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * 
	 * 13. For every miscellaneous data must be provided a short piece of text and some optional
	 * attachments.
	 * 
	 * Sentence Coverage: 40%
	 * Total Instructions: 90
	 * Instructions Covered: 225
	 * 
	 * Data Coverage. 50%
	 * 
	 * Atributte Bad Value Good Value
	 * ==============================================================
	 * title | - | Yes | 50%
	 * moment | - | Yes | 50%
	 * text | - | Yes | 50%
	 * attachments | - | Yes | 50%
	 * 
	 */

	@Test
	public void driverMiscReport() {
		final Object[][] testingData = {
			{
				"collaborator1", 0, null
			}, {
				"collaborator2", super.getEntityId("miscReport1"), IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateMiscReport((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void templateMiscReport(final String auth, final int report, final Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticate(auth);

			Collaborator c;
			c = (Collaborator) this.service.findActorByUserAccountId(LoginService.getPrincipal().getId());

			Portfolio p;
			p = c.getPortfolio();

			MiscellaneousReport saveTo = null;

			if (report == 0) {
				saveTo = this.serviceMisc.create();
				saveTo.setTitle("Title1");
				saveTo.setText("Texto2");
			} else {
				saveTo = this.serviceMisc.findOne(report);
				saveTo.setTitle("Title1");
				saveTo.setText("Texto2");
			}

			this.serviceMisc.save(saveTo, p.getId());
			this.serviceMisc.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	public Date convertedFrom(final String s) throws ParseException {
		return new SimpleDateFormat("yyyy/MM/dd").parse(s);
	}
}
