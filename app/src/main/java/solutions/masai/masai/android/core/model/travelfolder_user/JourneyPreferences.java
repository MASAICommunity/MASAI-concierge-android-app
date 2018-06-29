package solutions.masai.masai.android.core.model.travelfolder_user;

/**
 * Created by cWahl on 23.08.2017.
 */

public class JourneyPreferences {

	private Flight flights;
	private Hotel hotel;
	private Car car;
	private Train train;

	//region properties
	public Flight getFlights() {
		if (flights == null) {
			flights = new Flight();
		}
		return flights;
	}

	public void setFlights(Flight flights) {
		this.flights = flights;
	}

	public Hotel getHotel() {
		if (hotel == null) {
			hotel = new Hotel();
		}
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Car getCar() {
		if (car == null) {
			car = new Car();
		}
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Train getTrain() {
		if (train == null) {
			train = new Train();
		}
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}
	//endregion
}
