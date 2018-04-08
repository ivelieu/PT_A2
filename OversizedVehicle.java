	/**************************************************
	 * 
	 * my project starts here
	 * DARCY MORGAN  S3659847
	 * 
	 * this class handles the OversizedVehicle child of the Vehicle class, with
	 * additional functionality surrounding weight, height and extra charges
	**************************************************/
public class OversizedVehicle extends Vehicle{
	private double weight=0;
	private String category; //could use an enum here
	public static final double LIGHT_VEHICLE_CHARGE=10.0;
	public static final double MEDIUM_VEHICLE_CHARGE=20.0;
	public static final double HEAVY_VEHICLE_CHARGE=50.0;
	public static final double LIGHT_VEHICLE_MIN_WEIGHT=3.0; //subject to change based on forum post
	public static final double MEDIUM_VEHICLE_MIN_WEIGHT=4.50;
	public static final double HEAVY_VEHICLE_MIN_WEIGHT=8;
	public static final double MIN_SURCHARGE_WEIGHT=3.0;
	final double CLEARANCE_HEIGHT;
	OversizedVehicle(String regNo, String make, int year, String description, double clearance) {
		super(regNo, make, year, description);
		CLEARANCE_HEIGHT=clearance;
		this.category="N/A";
	}
	void recordWeight(double weight){
		this.weight=weight;
		if(weight>HEAVY_VEHICLE_MIN_WEIGHT){this.category="HEAVY";}
		else if(weight>MEDIUM_VEHICLE_MIN_WEIGHT){this.category="MEDIUM";}
		else if(weight>LIGHT_VEHICLE_MIN_WEIGHT){this.category="LIGHT";}
		else{this.category="N/A";}
	}
	double book(int numPassengers, DateTime date, double weight) throws VehicleException{
		this.recordWeight(weight);
		if(CLEARANCE_HEIGHT>3){
			throw new VehicleException
				("clearance height must be below 3 metres (too tall to fit on ferry)");
		}
		double tPrice=super.book(numPassengers, date);
		if(tPrice==-1 || tPrice==-2){
			return tPrice;
		}
		this.recordWeight(weight);
		switch (category){
			case "HEAVY":
				tPrice += HEAVY_VEHICLE_CHARGE*(weight-MIN_SURCHARGE_WEIGHT);
				break;
			case "MEDIUM":
				tPrice += MEDIUM_VEHICLE_CHARGE*(weight-MIN_SURCHARGE_WEIGHT);
				break;
			case "LIGHT":
				tPrice += LIGHT_VEHICLE_CHARGE*(weight-MIN_SURCHARGE_WEIGHT);
				break;
		}
		super.Book(tPrice);
		return tPrice;
	}
	public String getVehicleDetails() {
		String tDetails=super.getVehicleDetails();
		tDetails+="\n";
		if(this.weight!=0){
			tDetails += String.format("%-20s %s\n", "Category:", category);
			tDetails += String.format("%-20s %s\n", "Weight:", weight);
		}
		tDetails += String.format("%-20s %s\n", "Height:", CLEARANCE_HEIGHT);
		return tDetails;
	}
	public String getConcatDetails(){
		String details = "";
		details += CLEARANCE_HEIGHT + DELIM;
		
		details += super.getConcatDetails() + DELIM;
		if(weight != 0){
			details += weight;
		}
		return details;
	}
}
