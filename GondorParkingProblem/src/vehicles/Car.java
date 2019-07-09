package vehicles;

import abstractclasses.Vehicle;
import constants.IConstants;
import enumerations.VehicleType;

public class Car extends Vehicle{

	public Car(String vehicleNumber, String ownerMobileNumber) {
		super(vehicleNumber, VehicleType.CAR, ownerMobileNumber);
	}

	@Override
	public float getRequiredParkingSlotCapacity() {
		return IConstants.REQUIRED_PARKING_SIZE_FOR_CAR;
	}
}
