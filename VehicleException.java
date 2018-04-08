	/**************************************************
	 * 
	 * my project starts here
	 * DARCY MORGAN  S3659847
	 * 
	 * this class handles exceptions from invalid passenger counts 
	 * and clearance heights, called by 
	 * Vehicle and OversizedVehicle
	 * and handled by BookingSystem
	 * 
	**************************************************/
public class VehicleException extends Exception{

	private static final long serialVersionUID = 1L;
	VehicleException(String s){
		super(s);
	}

}
