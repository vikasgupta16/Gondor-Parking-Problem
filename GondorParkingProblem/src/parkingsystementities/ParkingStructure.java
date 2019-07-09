package parkingsystementities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abstractclasses.Vehicle;
import constants.IConstants;
import enumerations.CustomerType;
import enumerations.ParkingSlotStatus;
import enumerations.VehicleType;
import interfaces.ParkingStructureInterface;
import vehicles.Bike;
import vehicles.Car;

public class ParkingStructure implements ParkingStructureInterface {

	// Key - Customer's mobile number
	private HashMap<String, Customer> customerList;
	
	// Key - Vehicle number
	private HashMap<String, ParkingSlot> parkedVehicleList;
	
	// Key - Parking slot number (i.e string concatenation of floorNumber+slotNumber)
	private HashMap<String, ParkingSlot> parkingSlots;
	
	// Array holding remaining parking capacity of each floor
	float[] floorsParkingCapacity;
	
	// Array holding remaining parking capacity of each slot
	float[][] slotsParkingCapacity;
	
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	private static ParkingStructure parkingStructure = null;
	
	private ParkingStructure(){
		this.customerList = new HashMap<>();
		this.parkedVehicleList = new HashMap<>();
		this.parkingSlots = new HashMap<>();
	}
	
	public static ParkingStructure getParkingStructureInstance() {
		if(parkingStructure == null) {
			parkingStructure = new ParkingStructure();
		}
		
		return parkingStructure;
	}
	
	public Customer getCustomerOfGivenMobileNumber(String mobileNumber) {
		Customer customer = null;
		if(null != mobileNumber && !mobileNumber.isEmpty()) {
			if(customerList.containsKey(mobileNumber)) {
				customer = customerList.get(mobileNumber);
			}
		}
		
		return customer;
	}
	
	public ParkingSlot getParkingSlotOfGivenVehicleNumber(String vehicleNumber) {
		ParkingSlot parkingSlot = null;
		if(null != vehicleNumber && !vehicleNumber.isEmpty()) {
			if(parkedVehicleList.containsKey(vehicleNumber)) {
				parkingSlot = parkedVehicleList.get(vehicleNumber);
			}
		}
		
		return parkingSlot;
	}
	
	public ParkingSlot getParkingSlotDetails(String parkingSlotNumber) {
		ParkingSlot parkingSlot = null;
		if(null != parkingSlotNumber && !parkingSlotNumber.isEmpty()) {
			if(parkingSlots.containsKey(parkingSlotNumber)) {
				parkingSlot = parkingSlots.get(parkingSlotNumber);
			}
		}
		
		return parkingSlot;
	}
	
	/*public void printFloorsCapacity() {
		for(int i=0; i<floorsParkingCapacity.length; i++) {
			System.out.print("Floor "+(i+1)+": ");
			System.out.println(" "+floorsParkingCapacity[i]);
		}
	}
	
	public void printParkingSlotsCapacity() {
		for(int i=0; i<floorsParkingCapacity.length; i++) {
			System.out.println("Floor "+(i+1)+":");
			for(int j=0; j<20; j++) {
				System.out.print("\t"+slotsParkingCapacity[i][j]);
			}
			System.out.println();
		}
	}*/
	
	@Override
	public void initializeParkingCapacity(int n) {
		
		/* Creating floors[] array of size n to store the parking capacity of a floor.
		As given, there are 20 parking slots on each floor and each slot can park 2 cars or 5 bikes with stacking.
		So, assuming each slot has capacity of 1, a car occupies 0.5 of the capacity and bike 0.2 */
		
		floorsParkingCapacity = new float[n];
		for(int i = 0; i < n; i++) {
			floorsParkingCapacity[i] = IConstants.PARKING_SIZE_OF_FLOOR;
		}
		
		slotsParkingCapacity = new float[n][20];
		for(int i=0; i<n; i++) {
			for(int j=0; j<20; j++) {
				slotsParkingCapacity[i][j] = IConstants.PARKING_SIZE_OF_SLOT;
			}
		}
	}
	
	@Override
	public void parkVehicle() throws IOException {
		// Getting Incoming Vehicle details
		Vehicle vehicle = getIncomingVehicleDetails();
		if(null == vehicle) {
			return;
		}
		
		// Finding parking slot
		ParkingSlot parkingSlot = findParkingSlot(vehicle);
		if(null == parkingSlot) {
			return;
		}
		
		// Parking vehicle in the parking slot and updating necessary information in the system
		parkingSlot.parkVehicle(vehicle);
		
		floorsParkingCapacity[parkingSlot.getFloorNumber()] -= vehicle.getRequiredParkingSlotCapacity();
		slotsParkingCapacity[parkingSlot.getFloorNumber()][parkingSlot.getSlotNumber()] -= vehicle.getRequiredParkingSlotCapacity();
		
		float remainingParkingCapacity = parkingSlot.getRemainingParkingCapacity();
		remainingParkingCapacity -= vehicle.getRequiredParkingSlotCapacity();
		parkingSlot.setRemainingParkingCapacity(remainingParkingCapacity);
		
		parkingSlot.updateAvailabilityStatus();
		
		parkingSlots.put(""+parkingSlot.getFloorNumber()+parkingSlot.getSlotNumber(), parkingSlot);
		parkedVehicleList.put(vehicle.getVehicleNumber(), parkingSlot);
		
		System.out.println("\nVehicle number "+vehicle.getVehicleNumber()+" parked on floor "+(parkingSlot.getFloorNumber()+1)+", slot "+(parkingSlot.getSlotNumber()+1));
	}
	
	@Override
	public void removeVehicle() throws IOException{
		
		// Checking if any vehicles are parked.
		// If no, the return.
		if(parkedVehicleList.isEmpty()) {
			System.err.println("No vehicles are parked.");
			return;
		}
		
		// Getting Outgoing Vehicle number
		String vehicleNumber = getOutgoingVehicleDetails();
		
		ParkingSlot parkingSlot = getParkingSlotOfGivenVehicleNumber(vehicleNumber);
		if(null == parkingSlot) {
			System.err.println("Vehicle number "+vehicleNumber+" is not parked.");
			return;
		}
		
		// Getting updated parking slot details
		parkingSlot = getParkingSlotDetails(""+parkingSlot.getFloorNumber()+parkingSlot.getSlotNumber());
		
		// Getting Vehicle object for given vehicle number
		List<Vehicle> parkedVehicles = parkingSlot.getParkedVehicles();
		Vehicle vehicle = null;
		if(null != parkedVehicles && !parkedVehicles.isEmpty()) {
			for(Vehicle v : parkedVehicles) {
				if(v.getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
					vehicle = v;
					break;
				}
			}
		}
		
		if(null == vehicle) {
			System.err.println("Vehicle number "+vehicleNumber+" is not parked on floor "+(parkingSlot.getFloorNumber()+1)+", slot "+(parkingSlot.getSlotNumber()+1));
			return;
		}
		
		// Getting number of hours of parking
		int noOfHoursOfParking = getNumberOfHoursOfParking();
		
		// Removing vehicle from parking slot and updating necessary information in the system
		parkingSlot.removeVehicle(vehicle);
		
		floorsParkingCapacity[parkingSlot.getFloorNumber()] += vehicle.getRequiredParkingSlotCapacity();
		slotsParkingCapacity[parkingSlot.getFloorNumber()][parkingSlot.getSlotNumber()] += vehicle.getRequiredParkingSlotCapacity();
		
		float remainingParkingCapacity = parkingSlot.getRemainingParkingCapacity();
		remainingParkingCapacity += vehicle.getRequiredParkingSlotCapacity();
		parkingSlot.setRemainingParkingCapacity(remainingParkingCapacity);
		
		parkingSlot.updateAvailabilityStatus();
		
		parkingSlots.put(""+parkingSlot.getFloorNumber()+parkingSlot.getSlotNumber(), parkingSlot);
		parkedVehicleList.remove(vehicle.getVehicleNumber());
		
		System.out.println("\nVehicle number "+vehicle.getVehicleNumber()+" removed from floor "+(parkingSlot.getFloorNumber()+1)+", slot "+(parkingSlot.getSlotNumber()+1));
		
		calculateParkingAmountAndGiveRewardsToCustomer(noOfHoursOfParking, vehicle.getOwnerMobileNumber());
	}
	
	private void calculateParkingAmountAndGiveRewardsToCustomer(int noOfHoursOfParking, String customerMobileNumber ) {
		// Calculate parking charge
		Customer customer = getCustomerOfGivenMobileNumber(customerMobileNumber);
		int parkingCharge = IConstants.PARKING_CHARGE_PER_HOUR * noOfHoursOfParking;
		int rewardPoints = customer.getRewardPoints();
		int discountedParkingCharge = parkingCharge - rewardPoints;
		
		System.out.println("Collect parking charge Rs. "+discountedParkingCharge+"/-");
		System.out.println("Parking charge split:");
		System.out.println("\t Parking charge (for "+noOfHoursOfParking+" hours): Rs. "+parkingCharge+"/-");
		System.out.println("\t Reward points (1 point = Rs. 1): "+rewardPoints);
		System.out.println("\t Discounted parking charge (Parking charge - Reward points): Rs. "+discountedParkingCharge+"/-");

		customer.minusRewardPoints(rewardPoints);
		
		// Adding reward points for customer
		rewardPoints = IConstants.REWARD_POINTS_PER_HOUR * noOfHoursOfParking;
		if(rewardPoints > IConstants.MAXIMUM_REWARD_POINTS_PER_TIME) {
			rewardPoints = IConstants.MAXIMUM_REWARD_POINTS_PER_TIME;
		}
		customer.addRewardPoints(rewardPoints);
	}

	@Override
	public void printParkingSummaryofAllFloors() {
		for(int i=0; i<floorsParkingCapacity.length; i++) {
			System.out.print("Floor "+(i+1)+": ");
			if(floorsParkingCapacity[i] == IConstants.PARKING_SIZE_OF_FLOOR) {
				System.out.print(""+ParkingSlotStatus.AVAILABLE);
			} else if(floorsParkingCapacity[i] == 0) {
				System.out.print(""+ParkingSlotStatus.OCCUPIED);
			} else {
				System.out.print(""+ParkingSlotStatus.PARTIALLY_AVAILABLE);
			}
			System.out.println();
		}
	}
	
	@Override
	public void printParkingSummaryofGivenFloor(int floorNumber) {
		boolean isVehicleParked = false;
		for(Map.Entry<String, ParkingSlot> parkingSlot : parkingSlots.entrySet()) {
			if(parkingSlot.getKey().startsWith(""+floorNumber)) {
				System.out.println("---------------------------------------------");
				System.out.println(""+parkingSlot.getValue().toString());
				isVehicleParked = true;
			}
		}
		
		if(!isVehicleParked) {
			System.out.println("---------------------------------------------");
			System.out.println("No vehicles are parked.");
		}
		System.out.println("---------------------------------------------");
	}
	
	private String getOutgoingVehicleDetails() throws IOException {
		System.out.println("Please enter ougoing vehicle details.");
		String vehicleNumber = readVehicleNumber();
		return vehicleNumber;
	}
	
	private Vehicle getIncomingVehicleDetails() throws IOException {
		System.out.println("Please enter incoming vehicle details.");
		VehicleType vehicleType = readVehicleType();
		String vehicleNumber = readVehicleNumber();
		
		// Validating if Vehicle with same number is already parked
		ParkingSlot parkingSlot = getParkingSlotOfGivenVehicleNumber(vehicleNumber);
		if(null != parkingSlot) {
			System.err.print("\nVehicle number "+vehicleNumber+" is already parked on floor "+(parkingSlot.getFloorNumber()+1)+", slot "+(parkingSlot.getSlotNumber()+1));
			return null;
		}
		
		System.out.print("\nPlease enter customer's mobile number: ");
		String mobileNumber;
		
		while(true) {
			mobileNumber = br.readLine();
			Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}"); 
			Matcher m = p.matcher(mobileNumber);
			if(!m.matches()) {
				System.err.print("Please enter valid mobile number: ");
			} else {
				break;
			}
		}
		
		// Checking if customer already exists in the list of customers. If not then creating one and putting in the list
		getAndSaveCustomerDetails(mobileNumber);
		
		Vehicle vehicle = null;
		if(vehicleType.equals(VehicleType.CAR)){
			vehicle = new Car(vehicleNumber, mobileNumber);
		} else if(vehicleType.equals(VehicleType.BIKE)){
			vehicle = new Bike(vehicleNumber, mobileNumber);
		}
		
		return vehicle;
	}
	
	private String readVehicleNumber() throws IOException {
		String vehicleNumber;
		System.out.print("Vehicle number: ");
		
		while(true) {
			vehicleNumber = br.readLine();
			if(null != vehicleNumber && !vehicleNumber.isEmpty()) {
				break;
			} else {
				System.err.print("Please enter valid Vehicle number: ");
			}
		}
		return vehicleNumber;
	}
	
	private VehicleType readVehicleType() throws IOException {
		VehicleType vehicleType = null;
		String ch;
		System.out.print("Vehicle type (enter c for Car and b for Bike): ");
		while(true) {
			ch = br.readLine();
			if(ch.equalsIgnoreCase("c")) {
				vehicleType = VehicleType.CAR;
				break;
			} else if(ch.equalsIgnoreCase("b")) {
				vehicleType = VehicleType.BIKE;
				break;
			} else {
				System.err.print("Please enter valid Vehicle type (enter c for Car and b for Bike): ");
			}
		}
		
		return vehicleType;
	}
	
	private void getAndSaveCustomerDetails(String mobileNumber) throws IOException {
		Customer customer = getCustomerOfGivenMobileNumber(mobileNumber);
		
		if(null != customer) {
			System.out.println("\nCustomer details: ");
			System.out.println(customer.toString());
			
			String choice;
			while(true) {
				System.out.print("Do you want to update customer details? (y/n): ");
				choice = br.readLine();
				
				if(choice.equalsIgnoreCase("y")) {
					customer = readNewCustomerDetails(mobileNumber);
					break;
				} else if(choice.equalsIgnoreCase("n")) {
					break;
				} else {
					System.err.println("Invalid input");
				}
			}
			
		} else {
			customer = readNewCustomerDetails(mobileNumber);
		}
		
		customerList.put(mobileNumber, customer);
	}
	
	private Customer readNewCustomerDetails(String mobileNumber) throws IOException {
		System.out.println("Please enter customer details.");
		Customer customer = new Customer(readCustomerName(), mobileNumber, readCustomerType());
		return customer;
		 
	}
	
	private String readCustomerName() throws IOException {
		String name;
		System.out.print("Name: ");
		while(true) {
			name = br.readLine();
			if(null !=  name && !name.isEmpty()) {
				break;
			} else {
				System.err.print("Please enter valid Name: ");
			}
		}
		
		return name;
	}
	
	private CustomerType readCustomerType() throws IOException {
		CustomerType customerType;
		String type;
		System.out.print("Customer type (enter G for General, E for Elder, or R for Royal): ");
		while(true) {
			type = br.readLine();
			
			if(type.equalsIgnoreCase("e")) {
				customerType = CustomerType.ELDER;
				break;
			} else if(type.equalsIgnoreCase("r")) {
				customerType = CustomerType.ROYAL;
				break;
			} else if(type.equalsIgnoreCase("g")) {
				customerType = CustomerType.GENERAL;
				break;
			} else {
				System.err.print("Please enter valid Customer type (enter G for General, E for Elder, or R for Royal: ");
			}
		}
		
		return customerType;
	}
	
	private ParkingSlot findParkingSlot(Vehicle v) {
		ParkingSlot parkingSlot = null;
		int floorNumber = getFloor(v.getRequiredParkingSlotCapacity());
		if(floorNumber == -1) {
			System.err.print("No parking slot available for vehicle "+v.getVehicleNumber());
			return null;
		}
		
		int slotNumber = getAvailableParkingSlotNumberOnGivenFloor(floorNumber, v.getRequiredParkingSlotCapacity());
		if(slotNumber == -1) {
			System.err.print("No parking slot available for vehicle "+v.getVehicleNumber());
			return null;
		}
		
		parkingSlot = getParkingSlotDetails(""+floorNumber+slotNumber);
		if(null == parkingSlot) {
			parkingSlot = new ParkingSlot(slotNumber, floorNumber);
		}
		
		return parkingSlot;
	}
	
	private int getFloor(float requiredParkingCapacity) {
		for(int i=0; i < floorsParkingCapacity.length; i++) {
			if(floorsParkingCapacity[i] >= requiredParkingCapacity) {
				return i;
			}
		}
		
		return -1;
	}
	
	private int getAvailableParkingSlotNumberOnGivenFloor(int floorNumber, float requiredParkingCapacity) {
		
		// Defining maximum required available capacity based on the Vehicle type.
		// If Car the it is 1, else if Bike then it is 0.8
		// No stacking
		float maximumRequiredAvailableCapacity = IConstants.PARKING_SIZE_OF_SLOT;
		if(requiredParkingCapacity == IConstants.REQUIRED_PARKING_SIZE_FOR_BIKE) {
			maximumRequiredAvailableCapacity = 0.8f;
		}
		
		// Checking for empty slot in the parking lane of slots between 11 to 20, which are closer to exit
		// No stacking
		for(int i=10, offset=9; i<=14; i++, offset -= 2) {
			if(slotsParkingCapacity[floorNumber][i] >= maximumRequiredAvailableCapacity) {
				return i;
			}
			
			if(slotsParkingCapacity[floorNumber][i+offset] >= maximumRequiredAvailableCapacity) {
				return (i+offset);
			}
		}
		
		// Checking for empty slot in the parking lane of slots between 1 to 10
		// No stacking
		for(int i=0, offset=9; i<=4; i++, offset -= 2) {
			if(slotsParkingCapacity[floorNumber][i] >= maximumRequiredAvailableCapacity) {
				return i;
			}
			
			if(slotsParkingCapacity[floorNumber][i+offset] >= maximumRequiredAvailableCapacity) {
				return (i+offset);
			}
		}
		
		// Checking for available slot which can occupy the vehicle in the parking lane of slots between 11 to 20, which are closer to exit
		// With stacking
		for(int i=10, offset=9; i<=14; i++, offset -= 2) {
			if(slotsParkingCapacity[floorNumber][i] >= requiredParkingCapacity) {
				return i;
			}
			
			if(slotsParkingCapacity[floorNumber][i+offset] >= requiredParkingCapacity) {
				return (i+offset);
			}
		}
		
		// Checking for available slot which can occupy the vehicle in the parking lane of slots between 1 to 10
		// With stacking
		for(int i=0, offset=9; i<=4; i++, offset -= 2) {
			if(slotsParkingCapacity[floorNumber][i] >= requiredParkingCapacity) {
				return i;
			}

			if(slotsParkingCapacity[floorNumber][i+offset] >= requiredParkingCapacity) {
				return (i+offset);
			}
		}
		
		return -1;
	}

	public void printAllCustomers() {
		
		if(customerList.isEmpty()) {
			System.out.println("---------------------------------------------");
			System.out.println("No customers :(");
		} else {
			for(Map.Entry<String, Customer> customer: customerList.entrySet()) {
				System.out.println("---------------------------------------------");
				System.out.println(""+customer.getValue().toString());
			}
		}
		
		System.out.println("---------------------------------------------");
	}
	
	private int getNumberOfHoursOfParking() throws IOException {
		int noOfHoursOfParking = 0;
		while(true) {
			try {
				System.out.print("\nEnter number of hours of parking: ");
				noOfHoursOfParking = Integer.parseInt(br.readLine());
				if(noOfHoursOfParking <= 0) {
					System.err.println("Please enter a positive number.");
				} else {
					break;
				}
			} catch (NumberFormatException ne) {
				System.err.println("Please enter a valid number.");
			}
		}
		return noOfHoursOfParking;
	}
}
