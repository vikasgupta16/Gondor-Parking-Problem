package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import parkingsystementities.ParkingStructure;

public class ParkingSystem {
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws IOException {
		
		printParkingStructure();
		
		// Reading number of floors
		int n = getNumberOfFloors();
		
		ParkingStructure ps = ParkingStructure.getParkingStructureInstance();
		ps.initializeParkingCapacity(n);
		
		int ch = -1;
		do {
			ch = getChoice();
			switch(ch) {
			case 1:
				System.out.println("\n\n-----------------------------");
				System.out.println("Park Vehicle");
				System.out.println("-----------------------------\n");
				ps.parkVehicle();
				toContinue();
				break;
			case 2:
				System.out.println("\n\n-----------------------------");
				System.out.println("Remove Vehicle");
				System.out.println("-----------------------------\n");
				ps.removeVehicle();
				toContinue();
				break;
			case 3:
				System.out.println("\n\n----------------------------------------------");
				System.out.println("Parking space availability of all the floors");
				System.out.println("----------------------------------------------\n");
				ps.printParkingSummaryofAllFloors();
				toContinue();
				break;
			case 4:
				int floorNumber = readFloorNumber(n);
				System.out.println("\n\n-----------------------------");
				System.out.println("Parked vehicles on floor "+(floorNumber+1)+":");
				System.out.println("-----------------------------\n");
				ps.printParkingSummaryofGivenFloor(floorNumber);
				toContinue();
				break;
			case 5:
				System.out.println("\n\n-----------------------------");
				System.out.println("List of customers:");
				System.out.println("-----------------------------\n");
				ps.printAllCustomers();
				toContinue();
				break;
			case 6:
				System.out.println("\nExited.. Thank you.. :)");
				break;
			default:
				break;
			}
		} while (ch != 6);
		
		
	}
	
	private static void toContinue() throws IOException {
		System.out.print("\nPress enter key to continue..");
		br.readLine();
	}
	
	private static int getChoice() throws IOException {
		
		System.out.println("\n\n-------------------------------------------------------");
		System.out.println("MENU");
		System.out.println("-------------------------------------------------------");
		System.out.println("1. Park Vehicle");
		System.out.println("2. Remove Vehicle");
		System.out.println("3. View parking space availability of all the floors");
		System.out.println("4. View parked vehicles on given floor");
		System.out.println("5. View all the customers");
		System.out.println("6. Exit");
		System.out.println("-------------------------------------------------------");
		System.out.print("\nEnter your choice: ");
		int ch = -1;
		
		while(true) {
			try {
				ch = Integer.parseInt(br.readLine());
				if(ch < 0 || ch > 6) {
					System.err.print("Please enter a valid choice: ");
				} else {
					break;
				}
				break;
			} catch (NumberFormatException ne) {
				System.err.print("Please enter a valid choice: ");
			}
		}
		
		return ch;
	}
	
	private static int getNumberOfFloors() throws IOException {
		int n = 0;
		while(true) {
			try {
				System.out.print("\nEnter number of floors (N): ");
				n = Integer.parseInt(br.readLine());
				if(n < 0) {
					System.err.println("Please enter a positive number.");
				} else {
					break;
				}
			} catch (NumberFormatException ne) {
				System.err.println("Please enter a valid number.");
			}
		}
		return n;
	}
	
	private static int readFloorNumber(int n) throws IOException {
		int floorNumber;
		System.out.print("Enter floor number: ");
		while(true) {
			try {
				floorNumber = Integer.parseInt(br.readLine());
				if(floorNumber <= 0 || floorNumber > n) {
					System.err.print("Please enter a valid floor number: ");
				} else {
					break;
				}
			} catch (NumberFormatException ne) {
				System.err.print("Please enter a valid floor number: ");
			}
		}
		
		return --floorNumber;
	}
	
	private static void printParkingStructure() throws IOException {
		System.out.println("*** Gondor Parking Problem ***\n");
		System.out.println("Parking Structure of a floor\n");
		System.out.println("\t  1 \t 2 \t 3 \t 4 \t 5");
		System.out.println("Entry -> - - - - - - - - - - - - - - - - - - -");
		System.out.println("\t  10 \t 9 \t 8 \t 7 \t 6\n");
		System.out.println("\t  11 \t 12 \t 13 \t 14 \t 15");
		System.out.println("Exit  <- - - - - - - - - - - - - - - - - - - -");
		System.out.println("\t  20 \t 19 \t 18 \t 17 \t 16\n");
		
		System.err.println("Numbers between 1 to 20 represent the parking slots on a given floor.\n");
		toContinue();
	}
}
