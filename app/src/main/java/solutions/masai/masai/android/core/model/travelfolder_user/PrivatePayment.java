package solutions.masai.masai.android.core.model.travelfolder_user;

/**
 * Created by cWahl on 23.08.2017.
 */

public class PrivatePayment {

	private String creditCard;
	private String defaultPaymentMethod;

	//region properties
	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getDefaultPaymentMethod() {
		return defaultPaymentMethod;
	}

	public void setDefaultPaymentMethod(String defaultPaymentMethod) {
		this.defaultPaymentMethod = defaultPaymentMethod;
	}
	//endregion
}
