
/**************************************************
 * 
 * my project starts here
 * DARCY MORGAN  S3659847
 * 
 * this class handles user input
 * and file persistence
**************************************************/
import java.util.Scanner;
import java.io.*;//for FileOutputStream, FileNotFoundException
import java.util.StringTokenizer;

public class BookingSystem {
	public static final int MAXIMUM_VEHICLES = 30;
	public static final String FILENAME = "backup.txt";
	public static final String READ_DELIM="#";//for reading files
	public static final String DATE_DELIM="-";//for reading dates in files
	Vehicle[] vehicles = new Vehicle[MAXIMUM_VEHICLES];

	private int vehicleIndex = 0;// the index of the vehicles array where
									// the next vehicle should be made
									// using this instance variable name instead
									// of bookingCount for clarity
	public BookingSystem(){
		// file reading
		//given the situation, i have chosen to store data without metadata 
		//on vehicle type, since each vehicle status (4 total) has a 
		//different number of elements in it. this is easy to measure using
		//the StringTokenizer class.
		//(each Vehicle has all data on one line of the text document)
		
		//note that I am using a second StringTokenizer for converting
		//booking dates to BookingDate objects.
		
		// file persistence order for each type is listed next to the case.
		try {
			Scanner iStream = new Scanner(new File(FILENAME));
			
			String[] buffer;
			int i = 0;
			while (iStream.hasNextLine()) {
				StringTokenizer st = new StringTokenizer
						(iStream.nextLine(), READ_DELIM);
				switch (st.countTokens()) {
				case 4: //normal non-booked

					//buffer order:
					// regNo make year description
					buffer= new String[]
							{st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken()};
					vehicles[i]=new Vehicle
							(buffer[0], 
									buffer[1], 
									Integer.parseInt(buffer[2]), 
									buffer[3]);
					//need to cast input for the constructor 
					//(since iStream output is String)
					break;
					
				case 5: //oversized non-booked

					//buffer order:
					// clearance regNo make year description 
					buffer= new String[]
							{st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken()};
					vehicles[i]=new OversizedVehicle
						(buffer[1],
								buffer[2],
								Integer.parseInt(buffer[3]),
								buffer[4],
								Double.parseDouble(buffer[0]));
					
					break;
				case 8: //normal booked

					//buffer order:
					// regNo make year description
					// bookingID bookingDate bookingFee numPassengers
					buffer= new String[]
							{st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken()};
					vehicles[i]=new Vehicle
							(buffer[0], 
									buffer[1], 
									Integer.parseInt(buffer[2]), 
									buffer[3]);
					//convert the String to DateTime
					StringTokenizer st2 = new StringTokenizer(buffer[5], DATE_DELIM);
					int[]  dateBuffer = new int[3];
					dateBuffer[2]=Integer.parseInt(st2.nextToken());
					dateBuffer[1]=Integer.parseInt(st2.nextToken());
					dateBuffer[0]=Integer.parseInt(st2.nextToken());
					//i am making a dateBuffer so that in future it would be more 
					//straightforward to increment recorded dates, or change them on load.
					if(vehicles[i].book(Integer.parseInt(buffer[7]),
							new DateTime(dateBuffer[0],
									dateBuffer[1],
									dateBuffer[2]))
							!= Double.parseDouble(buffer[6])){
						throw new VehicleException
							("Data does not match calculation");
					}
						
					//buffer[4] and buffer[6] store the bookingID and 
					//bookingFee respectively. if these do not match 
					//what is calculated by Book(),
					//then there is inconsistent calculation 
					//and a VehicleException should
					//be thrown. 
					
					//since bookingID is not parsed through book, 
					//I am not checking it as this level of
					//data validity should not be necessary.
					break;
				case 10: //oversized booked
					//buffer order:
					// clearance regNo make year description 
					// bookingID bookingDate bookingFee numPassengers weight 
					buffer= new String[]
							{st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken(),
									st.nextToken()};
					vehicles[i]=new OversizedVehicle
							(buffer[1],
									buffer[2],
									Integer.parseInt(buffer[3]),
									buffer[4],
									Double.parseDouble(buffer[0]));

					StringTokenizer st3 = new StringTokenizer(buffer[6], DATE_DELIM);
					int[]  oversizedDateBuffer = new int[3];
					oversizedDateBuffer[2]=Integer.parseInt(st3.nextToken());
					oversizedDateBuffer[1]=Integer.parseInt(st3.nextToken());
					oversizedDateBuffer[0]=Integer.parseInt(st3.nextToken());
					if(((OversizedVehicle)vehicles[i]).book
					(Integer.parseInt(buffer[8]), 
							new DateTime(oversizedDateBuffer[0],
									oversizedDateBuffer[1],
									oversizedDateBuffer[2]), 
							Double.parseDouble(buffer[9])) 
					!= Double.parseDouble(buffer[7])) {
						throw new VehicleException("Data does not match calculation");
						
					}
					
					break;
				
				}
				i++;
			}
			iStream.close();
			if(i==0){
				throw new FileNotFoundException("file is empty");
			}
			//when the import is finished, internal class vehicleIndex can be updated.
			// I chose not to use vehicleIndex for the 
			//file reading increment to make the commands shorter
			//as they are already quite long and hard to read even with spacing.
			vehicleIndex=i;
			
			System.out.println("Existing Vehicle data was loaded");
			
		} catch (FileNotFoundException e) {
			System.out.println("No vehicle data was loaded");
		}
		catch (VehicleException e) {
			System.out.println("Error in the loaded Vehicle data: incomplete import");
			System.out.println("Cause of error: " + e.getMessage());
	}
		
	}
	public void menu() {

		Scanner io2 = new Scanner(System.in);
		// need multiple scanners: one for a method call, one for the menu
		// can be done differently using visibility settings
		System.out.print("\n*** Vehicle Booking System Menu ***\n" 
				+ "Seed Data                  A\n"
				+ "Add Vehicle                B\n" 
				+ "Display Vehicles           C\n" 
				+ "Book Passage               D\n"
				+ "Exit Program               X\n" 
				+ "Enter selection:           ");
		String tString = io2.nextLine();
		//case-sensitive! 
		switch (tString) {
		case "A":
			seedData();
			break;
		case "B":
			addVehicle();
			break;
		case "C":
			displayVehicles();
			break;
		case "D":
			bookPassage();
			break;
		case "X":
			// file writing belongs here
			// a lot more simple than reading :)
			try {
				PrintWriter pw = new PrintWriter(FILENAME);
				for (int i = 0; i < this.vehicleIndex; i++) {
					if (vehicles[i] instanceof OversizedVehicle) {
						pw.print(((OversizedVehicle)vehicles[i]).getConcatDetails() 
								+ "\n");
					}
					else {
						pw.print(vehicles[i].getConcatDetails() + "\n");
					}
				}
				pw.close();
			} catch (FileNotFoundException e) {
			}
			System.exit(0);
			break;
		default:
			System.out.println("Invalid selection.");
		}
	}

	public void seedData() {
		try {
			//remember: everything is case-sensitive
			//again: splitting lines for readibility. 
			
			vehicles[this.vehicleIndex] = new Vehicle
					("ABC123", "honda", 1984, "blue van");
			vehicleIndex++;
			vehicles[this.vehicleIndex] = new Vehicle
					("123123", "honda", 1989, "white van");
			vehicleIndex++;
			vehicles[this.vehicleIndex] = new Vehicle
					("123456", "honda", 1999, "red van");
			vehicleIndex++;
			vehicles[this.vehicleIndex - 1].book
					(4, new DateTime(31, 12, 2017));
			
			vehicles[this.vehicleIndex] = new Vehicle
					("ABCABC", "honda", 1989, "black van");
			vehicleIndex++;
			vehicles[this.vehicleIndex - 1].book
					(2, new DateTime(30, 11, 2017));

			// now for stage 4
			vehicles[this.vehicleIndex] = new OversizedVehicle
					("111111", "honda", 1952, "blue truck", 2.4);
			vehicleIndex++;
			((OversizedVehicle) vehicles[this.vehicleIndex - 1]).book
					(3, new DateTime(30, 11, 2017), 3.5);
			vehicles[this.vehicleIndex] = new OversizedVehicle
					("222222", "honda", 1954, "black truck", 2.3);
			vehicleIndex++;
			((OversizedVehicle) vehicles[this.vehicleIndex - 1]).book
					(3, new DateTime(30, 11, 2017), 5.0);
			vehicles[this.vehicleIndex] = new OversizedVehicle
					("333333", "honda", 1964, "pink truck", 2.2);
			vehicleIndex++;
			((OversizedVehicle) vehicles[this.vehicleIndex - 1]).book
					(3, new DateTime(30, 11, 2017), 9.9);

			vehicles[this.vehicleIndex] = new OversizedVehicle
					("333334", "honda", 1784, "turqoise truck", 2.2);
			vehicleIndex++;
			vehicles[this.vehicleIndex] = new OversizedVehicle
					("444444", "honda", 1980, "green truck", 2.0);
			vehicleIndex++;
			vehicles[this.vehicleIndex] = new OversizedVehicle
					("555555", "honda", 1994, "blue truck", 2.9);
			vehicleIndex++;
		} catch (VehicleException e) {

		}
		// laugh if you noticed it
	}
	
	public void addVehicle() {
		Scanner io = new Scanner(System.in);
		String tRegi;
		String tMake;
		int tYear;
		String tDesc;
		int tHeight;
		int tIndex;
		System.out.print("Enter vehicle registration:            ");
		tRegi = io.nextLine();
		
		tIndex=this.getVehicleIndex(tRegi);
		if(tIndex!=-1){
			System.out.println("Error - Registration "
			+ tRegi + " already exists in the system!");
			
			return;
		}
		
		System.out.print("Enter vehicle make:                    ");
		tMake = io.nextLine();
		System.out.print("Enter vehicle year:                    ");
		tYear = Integer.parseInt(io.nextLine());
		//note: this a convention of mine, 
		//since i have found Scanner.nextInt() to be unreliable
		System.out.print("Enter vehicle description:             ");
		tDesc = io.nextLine();
		System.out.print("Enter vehicle height:                  ");
		tHeight = Integer.parseInt(io.nextLine());
		if (tHeight <= 1) {
			vehicles[this.vehicleIndex] = new Vehicle(tRegi, tMake, tYear, tDesc);
			System.out.println
				("\nNew Vehicle added successfully for registration " 
			+ tRegi + ".");
		} else {
			vehicles[this.vehicleIndex] = new OversizedVehicle
					(tRegi, tMake, tYear, tDesc, tHeight);
			System.out.println
				("\nNew Oversized Vehicle added successfully for registration "
			+ tRegi + ".");
		}
		vehicleIndex++;
		
	}

	public void displayVehicles() {
		for (int i = 0; i < this.vehicleIndex; i++) {
			if (vehicles[i] instanceof OversizedVehicle) {

				System.out.println(
						((OversizedVehicle) vehicles[i]).getVehicleDetails());
			} else {
				System.out.println(vehicles[i].getVehicleDetails());
			}
		}
	}

	public int getVehicleIndex(String regi) {
		for (int i = 0; i < this.vehicleIndex; i++) {
			if (vehicles[i].getRegNo().equals(regi)) {
				return i;
				// once it finds the match, it should stop looking
			}
		}
		return -1;// negatives reserved for errors
		// read VehicleException for why this isn't an exception
	}

	public void bookPassage() {
		Scanner io = new Scanner(System.in);
		String tRegi;
		int tDay;
		int tMonth;
		int tYear;
		int tPass;
		double tPrice;
		int tIndex;
		System.out.print("Enter registration number: ");
		tRegi = io.nextLine();
		tIndex=this.getVehicleIndex(tRegi);
		if (tIndex == -1) {
			System.out.println("Error - registration number not found");
			
			return;
		}
		//note: for user input, I chose not to format using printf
		//simply because I'm less famaliar with it, so it would take
		//a lot of effort to auto-indent to the same position compared to 
		//manual spacing measurement.
		System.out.print("Enter day of month:        ");
		tDay = Integer.parseInt(io.nextLine());
		System.out.print("Enter month:               ");
		tMonth = Integer.parseInt(io.nextLine());
		System.out.print("Enter year:                ");
		tYear = Integer.parseInt(io.nextLine());
		DateTime tDate = new DateTime(tDay, tMonth, tYear);
		System.out.print("Enter passengers:          ");
		tPass = Integer.parseInt(io.nextLine());

		// this is commented out as per stage 5
		/*
		 * if (tPass < 0) {
		 * System.out.println("Error - passenger count must be greater than 0!"
		 * );  return; } else if (tPass > 6) {
		 * System.out.println("Error - passenger count must be less than 6!");
		 *  return; }
		 */
		if (vehicles[tIndex] instanceof OversizedVehicle) {
			double tWeight;
			System.out.print("Enter weight:              ");
			tWeight = Double.parseDouble(io.nextLine());
			try {
				tPrice = ((OversizedVehicle) vehicles[tIndex]).book
						(tPass, tDate, tWeight);
			} catch (VehicleException e) {
				System.out.println(e.getMessage());
				
				return;
			}
		}

		else {
			try {
				tPrice = vehicles[tIndex].book(tPass, tDate);
			} catch (VehicleException e) {
				System.out.println(e.getMessage());
				
				return;
			}
			catch (Exception e){
				System.out.println(e.getMessage());
				
				return;
			}
		}
		System.out.println("Booking for " 
		+ tRegi + " on 28/09/2017 was successful.");
		System.out.println("The total cost for the booking is: $" + tPrice);
	}
}