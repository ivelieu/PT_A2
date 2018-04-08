/**************************************************
 * 
 * my project starts here DARCY MORGAN S3659847
 * 
 * this class handles the generic Vehicle object to store Vehicles.
 **************************************************/
public class Vehicle {
	private String regNo;
	private String make;
	private int year;
	private String description;
	private String bookingID;
	private DateTime bookingDate;
	private double bookingFee; // this was indicated unnecessary in the
								// instructions but i believe it is necessary
								// for my implementation

	private int numPassengers;
	public static final int BOOKING_FEE = 100;
	public static final int PASSENGER_SURCHARGE = 20;
	public static final String DELIM="#";//for writing to files
	//considered calling it WRITE_DELIM, doesn't really matter i guess

	Vehicle(String regNo, String make, int year, String description) {
		this.regNo = regNo;
		this.make = make;
		this.year = year;
		this.description = description;
		this.bookingID = "N/A";
	}

	public String getRegNo() {
		return this.regNo;
	}

	private boolean recordNumPassengers(int a) {
		if (a < 1 || a > 6) {
			return false;
		}
		this.numPassengers = a;
		return true;
	}

	public double book(int numPassengers, DateTime bookingDate) throws VehicleException {
		if (recordNumPassengers(numPassengers) == false) {
			throw new VehicleException("invalid passenger numbers");
			// return -1.0; 
			//this would run in a pre-stage 5 implementation
		}
		DateTime current = new DateTime();
		if (DateTime.diffDays(bookingDate, current) < 0) {
			throw new VehicleException("invalid booking date");
			// return -2.0;
			//this would run in a pre-stage 5 implementation
		}

		this.bookingID = this.regNo + bookingDate.getEightDigitDate();
		this.bookingDate = bookingDate;
		this.bookingFee = BOOKING_FEE + PASSENGER_SURCHARGE * numPassengers;
		if(this instanceof OversizedVehicle){
		}
		return BOOKING_FEE + PASSENGER_SURCHARGE * numPassengers;
	}
	public void Book(double Fee){
		//this function is for directly setting the fee if it is an OversizedVehicle
		//an alternative to overloading Book is changing privacy settings of Vehicle.bookingFee
		this.bookingFee=Fee;
	}
	public String getVehicleDetails() {
		//just using format syntax provided
		//not as confusing as pointers!!
		String details = String.format("%-20s %s\n", "Reg Num:", regNo);
		details += String.format("%-20s %s\n", "Make:", make);
		details += String.format("%-20s %s\n", "Year:", year);
		details += String.format("%-20s %s\n", "Description:", description);
		details += String.format("%-20s %s\n", "Booking Ref:", bookingID);
		if (this.bookingID.equals("N/A") == false) {
			details += String.format("%-20s %s\n", "Booking Date:", bookingDate);
			details += String.format("%-20s %s\n", "Num Passengers:", numPassengers);
			details += String.format("%-20s %s\n", "Fee:", "$" + bookingFee);
		}
		return details;
	}
	public String getConcatDetails(){
		//for file persistence (concat the data (with delims)
		//before storing it so redundant identifiers aren't used)
		String details = "";
		details += regNo + DELIM
				+ make + DELIM
				+ year + DELIM
				+ description;
		if(this.bookingID.equals("N/A") == false) {
			details += DELIM+ bookingID + DELIM
					+ bookingDate + DELIM
					+ bookingFee + DELIM
					+ numPassengers;
		}
		
		
		return details;
	}
}
