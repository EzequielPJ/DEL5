
package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapKeyColumn;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CustomisationSystem extends DomainEntity {

	private String				systemName;
	private String				banner;
	private String				message;
	private Map<String, String>	spamwords;
	private Integer				hoursFinder;
	private Integer				resultFinder;
	private String				phonePrefix;
	private String				creditCardMakes;


	@NotBlank
	@SafeHtml
	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}
	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}
	@NotBlank
	@SafeHtml
	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	@ElementCollection(targetClass = String.class)
	@MapKeyColumn(name = "lang")
	public Map<String, String> getSpamwords() {
		return this.spamwords;
	}

	public void setSpamwords(final Map<String, String> spamwords) {
		this.spamwords = spamwords;
	}
	@Range(min = 1, max = 24)
	public Integer getHoursFinder() {
		return this.hoursFinder;
	}

	public void setHoursFinder(final Integer hoursFinder) {
		this.hoursFinder = hoursFinder;
	}
	@Range(min = 10, max = 100)
	public Integer getResultFinder() {
		return this.resultFinder;
	}

	public void setResultFinder(final Integer resultFinder) {
		this.resultFinder = resultFinder;
	}

	@NotBlank
	@SafeHtml
	public String getPhonePrefix() {
		return this.phonePrefix;
	}

	public void setPhonePrefix(final String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	@NotBlank
	@SafeHtml
	public String getCreditCardMakes() {
		return this.creditCardMakes;
	}

	public void setCreditCardMakes(final String creditCardMakes) {
		this.creditCardMakes = creditCardMakes;
	}

}
