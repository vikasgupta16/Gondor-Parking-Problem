package parkingsystementities;

import java.util.ArrayList;
import java.util.List;

import abstractclasses.Vehicle;
import constants.IConstants;
import enumerations.ParkingSlotStatus;
import interfaces.ParkingSlotInterface;

public class ParkingSlot implements ParkingSlotInterface{

	private int slotNumber;
	private int floorNumber;
	private ParkingSlotStatus status;
	private float remainingParkingCapacity;
	private List<Vehicle> parkedVehicles;
	
	public ParkingSlot(int slotNumber, int floorNumber) {
		this.slotNumber = slotNumber;
		this.floorNumber = floorNumber;
		this.status = ParkingSlotStatus.AVAILABLE;
		this.remainingParkingCapacity = IConstants.PARKING_SIZE_OF_SLOT;
		this.parkedVehicles = new ArrayList<>();
	}
	
	public int getSlotNumber() {
		return this.slotNumber;
	}
	
	public int getFloorNumber() {
		return this.floorNumber;
	}
	
	public void setStatus(ParkingSlotStatus status) {
		this.status = status;
	}
	
	public ParkingSlotStatus getStatus() {
		return this.status;
	}
	
	public void setRemainingParkingCapacity(float remainingParkingCapacity) {
		this.remainingParkingCapacity = remainingParkingCapacity;
	}
	
	public float getRemainingParkingCapacity() {
		return this.remainingParkingCapacity;
	}
	
	public List<Vehicle> getParkedVehicles() {
		return this.parkedVehicles;
	}

	@Override
	public void parkVehicle(Vehicle v) {
		this.parkedVehicles.add(v);
	}

	@Override
	public void removeVehicle(Vehicle v) {
		this.parkedVehicles.remove(v);
	}
	
	@Override
	public void updateAvailabilityStatus() {
		if(this.getRemainingParkingCapacity() == IConstants.PARKING_SIZE_OF_SLOT) {
			this.setStatus(ParkingSlotStatus.AVAILABLE);
		} else if(this.getRemainingParkingCapacity() == 0) {
			this.setStatus(ParkingSlotStatus.OCCUPIED);
		} else {
			this.setStatus(ParkingSlotStatus.PARTIALLY_AVAILABLE);
		}
	}

	@Override
	public String toString() {
		String result = "";
		result += "Floor number: "+(floorNumber+1)+"\n";
		result += "Slot number: "+(slotNumber+1)+"\n";
		result += "Status: "+status+"\n";
		result += "Parked vehicles:\n";
		
		if(parkedVehicles.isEmpty()) {
			result += "------------------------\n";
			result += "No vehicles are parked.\n";
		} else {
			for(Vehicle v : parkedVehicles) {
				result += "------------------------\n";
				result += v.toString();
			}
		}
		
		result += "------------------------\n";
		
		return result;
	}
}
