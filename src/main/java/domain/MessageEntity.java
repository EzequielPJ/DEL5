
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "`message`")
public class MessageEntity extends DomainEntity {

	private Actor				sender;
	private Collection<Actor>	receiver;
	private Date				momentsent;
	private String				subject;
	private String				body;
	private Collection<String>	tags;
	private String				priority;
	private Collection<Box>		box;


	@ManyToOne(optional = false)
	public Actor getSender() {
		return this.sender;
	}

	public void setSender(final Actor sender) {
		this.sender = sender;
	}

	@ManyToMany
	public Collection<Actor> getReceiver() {
		return this.receiver;
	}

	public void setReceiver(final Collection<Actor> receiver) {
		this.receiver = receiver;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	public Date getMomentsent() {
		return this.momentsent;
	}

	public void setMomentsent(final Date momentsent) {
		this.momentsent = momentsent;
	}

	@NotBlank
	@SafeHtml
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	@SafeHtml
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@ElementCollection
	public Collection<String> getTags() {
		return this.tags;
	}

	public void setTags(final Collection<String> tags) {
		this.tags = tags;
	}

	@NotBlank
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}

	@ManyToMany
	public Collection<Box> getBox() {
		return this.box;
	}

	public void setBox(final Collection<Box> box) {
		this.box = box;
	}

}
