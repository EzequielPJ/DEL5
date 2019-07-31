
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Actor;
import domain.MessageEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService				messageService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private CustomisationSystemService	customService;


	/*
	 * Requirement tested: An actor who is authenticated must be able to:
	 * Send a message to other actor
	 * Total instructions:
	 * Covered instructions:
	 * Analysis of sentence coverage of messageService:
	 * Analysis of data coverage:
	 * Attribute: name | Bad value: null | Normal value: Yes | Coverage: 100% |
	 */
	@Test
	public void ActorTest() {
		final Object testingData[][] = {
			{
				//Positive test: Update a box
				"collaborator1", "userAccount4", "userAccount5", "subject", "body", null
			}, {
				//Negative test: The business rule that has been violated: message subject can not be null
				"collaborator1", "userAccount4", "userAccount5", "", "body", ConstraintViolationException.class
			}, {
				//Negative test: The business rule that has been violated: message body can not be null
				"collaborator1", "userAccount4", "userAccount5", "subject", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	protected void template(final String username, final int sender, final int receiver, final String subject, final String body, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			MessageEntity mes;
			mes = this.messageService.createMessage(this.actorService.findByUserAccount(sender));
			Collection<Actor> col;
			col = new ArrayList<Actor>();
			col.add(this.actorService.findByUserAccount(receiver));
			mes.setReceiver(col);
			mes.setSubject(subject);
			mes.setBody(body);
			mes.setMomentsent(new Date());
			mes.setTags(new ArrayList<String>());
			System.setProperty("spamwords", this.customService.findUnique().getSpamwords().get("en"));
			this.messageService.sendMessage(mes);
			this.messageService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
