package interfaces;

import java.io.IOException;

public interface ParkingStructureInterface {

	void parkVehicle() throws IOException;
	void removeVehicle() throws IOException;
	void printParkingSummaryofAllFloors();
	void printParkingSummaryofGivenFloor(int floorNumber);
	void initializeParkingCapacity(int n);
}
