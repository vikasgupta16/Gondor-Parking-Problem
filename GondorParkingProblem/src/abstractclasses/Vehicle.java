package abstractclasses;

import enumerations.VehicleType;

public abstract class Vehicle {
	private String vehicleNumber;
	private VehicleType vehicleType;
	private String ownerMobileNumber;
	
	public Vehicle(String vehicleNumber, VehicleType vehicleType, String ownerMobileNumber){
		this.vehicleNumber = vehicleNumber;
		this.vehicleType = vehicleType;
		this.ownerMobileNumber = ownerMobileNumber;
	}
	
	public String getVehicleNumber(){
		return this.vehicleNumber;
	}
	
	public VehicleType getVehicleType(){
		return this.vehicleType;
	}
	
	public String getOwnerMobileNumber(){
		return this.ownerMobileNumber;
	}
	
	// Abstract method
	public abstract float getRequiredParkingSlotCapacity();

	@Override
	public String toString() {
		String result = "";
		result += "Vehicle number: "+vehicleNumber+"\n";
		result += "Vehicle type: "+vehicleType+"\n";
		return result;
	}
}
