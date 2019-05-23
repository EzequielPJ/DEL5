
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Swap extends DomainEntity {

	private String			phone;
	private String			status;
	private String			description;
	private Comission		comission;
	private Collaborator	receiver;
	private Collaborator	sender;


	@NotBlank
	@SafeHtml
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@NotBlank
	@SafeHtml
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@OneToOne
	public Comission getComission() {
		return this.comission;
	}

	public void setComission(final Comission comission) {
		this.comission = comission;
	}

	@ManyToOne
	public Collaborator getReceiver() {
		return this.receiver;
	}

	public void setReceiver(final Collaborator receiver) {
		this.receiver = receiver;
	}

	@ManyToOne
	public Collaborator getSender() {
		return this.sender;
	}

	public void setSender(final Collaborator sender) {
		this.sender = sender;
	}

}
