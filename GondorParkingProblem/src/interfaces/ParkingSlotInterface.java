package interfaces;

import abstractclasses.Vehicle;

public interface ParkingSlotInterface {
	
	void parkVehicle(Vehicle v);
	void removeVehicle(Vehicle v);
	void updateAvailabilityStatus();
}
