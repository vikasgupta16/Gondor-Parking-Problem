package vehicles;

import abstractclasses.Vehicle;
import constants.IConstants;
import enumerations.VehicleType;

public class Bike extends Vehicle{

	public Bike(String vehicleNumber, String ownerMobileNumber) {
		super(vehicleNumber, VehicleType.BIKE, ownerMobileNumber);
	}

	@Override
	public float getRequiredParkingSlotCapacity() {
		return IConstants.REQUIRED_PARKING_SIZE_FOR_BIKE;
	}
}