
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Embeddable
@Access(AccessType.PROPERTY)
public class CreditCard {

	private String	holder;
	private String	make;
	private String	number;
	private Date	expiration;
	private int		cvv;


	@SafeHtml
	@NotBlank
	public String getHolder() {
		return this.holder;
	}

	public void setHolder(final String holder) {
		this.holder = holder;
	}

	@SafeHtml
	@NotBlank
	public String getMake() {
		return this.make;
	}

	public void setMake(final String make) {
		this.make = make;
	}

	@SafeHtml
	@NotBlank
	@Size(min = 16, max = 16)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@Range(min = 100, max = 999)
	public int getCvv() {
		return this.cvv;
	}

	public void setCvv(final int cvv) {
		this.cvv = cvv;
	}
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM")
	public Date getExpiration() {
		return this.expiration;
	}

	public void setExpiration(final Date expiration) {
		this.expiration = expiration;
	}

}
