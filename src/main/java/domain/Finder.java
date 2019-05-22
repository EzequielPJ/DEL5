
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Finder extends DomainEntity {

	private String					singleKey;
	private Date					registeredDate;
	private boolean					beforeOrNot;

	private Date					creationDate;
	private Category				category;
	private Collection<Proclaim>	proclaims;


	public boolean isBeforeOrNot() {
		return this.beforeOrNot;
	}

	public void setBeforeOrNot(final boolean beforeOrNot) {
		this.beforeOrNot = beforeOrNot;
	}

	@ManyToMany
	public Collection<Proclaim> getProclaims() {
		return this.proclaims;
	}

	public void setProclaims(final Collection<Proclaim> proclaims) {
		this.proclaims = proclaims;
	}

	@SafeHtml
	public String getSingleKey() {
		return this.singleKey;
	}

	public void setSingleKey(final String singleKey) {
		this.singleKey = singleKey;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd'T'HH:mm'Z'")
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	@ManyToOne(optional = true)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	public Date getRegisteredDate() {
		return this.registeredDate;
	}

	public void setRegisteredDate(final Date registeredDate) {
		this.registeredDate = registeredDate;
	}

}
