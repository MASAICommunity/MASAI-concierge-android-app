package solutions.masai.masai.android.core.model.travelfolder_user;

import java.util.Date;

/**
 * Created by cWahl on 21.08.2017.
 */

public class TravelfolderUser {

	private String identitySource;
	private String userId;
	private Esta esta;
	private BillingAddress billingAddress;
	private PrivatePayment privatePayment;
	private ContactInfo contactInfo;
	private Date created;
	private Passport passport;
	private JourneyPreferences journeyPreferences;
	private Date modified;
	private PrivateAddress privateAddress;
	private PersonalData personalData;

	//region properties
	public String getIdentitySource() {
		return identitySource;
	}

	public void setIdentitySource(String identitySource) {
		this.identitySource = identitySource;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public PersonalData getPersonalData() {
		return personalData;
	}

	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public JourneyPreferences getJourneyPreferences() {
		if (journeyPreferences == null) {
			journeyPreferences = new JourneyPreferences();
		}

		return journeyPreferences;
	}

	public void setJourneyPreferences(JourneyPreferences journeyPreferences) {
		this.journeyPreferences = journeyPreferences;
	}

	public PrivateAddress getPrivateAddress() {
		return privateAddress;
	}

	public void setPrivateAddress(PrivateAddress privateAddress) {
		this.privateAddress = privateAddress;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Esta getEsta() {
		return esta;
	}

	public void setEsta(Esta esta) {
		this.esta = esta;
	}

	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public PrivatePayment getPrivatePayment() {
		return privatePayment;
	}

	public void setPrivatePayment(PrivatePayment privatePayment) {
		this.privatePayment = privatePayment;
	}

	public Passport getPassport() {
		return passport;
	}

	public void setPassport(Passport passport) {
		this.passport = passport;
	}

//endregion
}
