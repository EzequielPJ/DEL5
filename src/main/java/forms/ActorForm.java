
package forms;

import security.UserAccount;

public class ActorForm {

	private String		name;
	private String		surname;
	private String		photo;
	private String		email;
	private String		phone;
	private String		adress;
	private String		authority;

	private int			id;

	private UserAccount	account;
	private boolean		terms;


	public UserAccount getAccount() {
		return this.account;
	}

	public void setAccount(final UserAccount account) {
		this.account = account;
	}


	private String	password2;


	public String getPassword2() {
		return this.password2;
	}

	public void setPassword2(final String password2) {
		this.password2 = password2;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getAdress() {
		return this.adress;
	}

	public void setAdress(final String adress) {
		this.adress = adress;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(final String authority) {
		this.authority = authority;
	}

	public boolean isTerms() {
		return this.terms;
	}

	public void setTerms(final boolean terms) {
		this.terms = terms;
	}
}
