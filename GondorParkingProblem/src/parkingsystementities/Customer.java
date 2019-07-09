package parkingsystementities;

import enumerations.CustomerType;

public class Customer {

	private String name;
	private String mobileNumber;
	private CustomerType type;
	private int rewardPoints;
	
	public Customer(String name, String mobileNumber, CustomerType type) {
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.type = type;
		this.rewardPoints = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getMobileNumber() {
		return this.mobileNumber;
	}
	
	public CustomerType getType() {
		return this.type;
	}
	
	public void addRewardPoints(int rewardPoints) {
		this.rewardPoints = this.rewardPoints + rewardPoints;
	}
	
	public void minusRewardPoints(int rewardPoints) {
		this.rewardPoints = this.rewardPoints - rewardPoints;
	}
	
	public int getRewardPoints() {
		return this.rewardPoints;
	}

	@Override
	public String toString() {
		return "Mobile Number: " + mobileNumber + ", \nName: " + name + ", \nCustomer type: " + type + ", \nReward Points: "
				+ rewardPoints;
	}
	
	
}
