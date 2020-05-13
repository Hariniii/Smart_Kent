package com.code.demo;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CalculateTime {
	public static int TRAVEL_TIME = 3;
	public static int PICK_DROP_TIME = 4;
	private int ETA;
	public int time = 0;
	public int tot = 0;
	
	public int getETA() {
		return ETA;
	}
	
	public void setETA(int ETA) {
		this.ETA = ETA;
	}
	
	@Override
	public String toString() {
		return "CalculateTime [ETA=" + ETA + "]";
	}
	
	
    public int getTime(int fromFloor, int toFloor, Elevator elevator) {
    	int length = elevator.floorStopsMap.size();
    	for(int i = 0; i<length; i++) {
	    	if(elevator.getElevatorState().equals(ElevatorState.TO_PICKUP)) {
	    		time = Math.abs(elevator.getCurrentFloor() - fromFloor) * TRAVEL_TIME;
	    	}else if (elevator.getElevatorState().equals(ElevatorState.PICKUP)){
	    		time = PICK_DROP_TIME;
	    	}else if(elevator.getElevatorState().equals(ElevatorState.TO_DROPOFF)){
	    		time = Math.abs(elevator.getCurrentFloor() - toFloor) * TRAVEL_TIME;
	    	}else if(elevator.getElevatorState().equals(ElevatorState.DROPOFF)) {
	    		time = PICK_DROP_TIME;
	    	}
	    	tot = tot + time;
	    	}
    	System.out.println(tot);
		return tot;
    	
    }
   
}
