
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Embeddable
@Access(AccessType.PROPERTY)
public class StudentCard {

	private String	centre;
	private Integer	code;
	private String	vat;


	@SafeHtml
	@NotBlank
	public String getVat() {
		return this.vat;
	}

	public void setVat(final String vat) {
		this.vat = vat;
	}

	@SafeHtml
	@NotBlank
	public String getCentre() {
		return this.centre;
	}

	public void setCentre(final String centre) {
		this.centre = centre;
	}

	@Column(length = 4)
	public Integer getCode() {
		return this.code;
	}

	public void setCode(final Integer code) {
		this.code = code;
	}

}
