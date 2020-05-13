package com.code.demo;

import java.util.*;

public class Elevator{
	
	private boolean operating;
	private int id;
	private ElevatorState elevatorState;
	private ElevatorDirection direction;
	private int currentFloor;
	private ElevatorRequest elevatorRequest;
	int reqFloor = elevatorRequest.getRequestFloor();
	int targetFloor = elevatorRequest.getTargetFloor();
	
	//set of floors the elevator pass by while moving
	private NavigableSet<Integer> floorPasses;
	
	//stores the up down motion of the elevator
	public Map<ElevatorState, NavigableSet<Integer>> floorStopsMap;
	
	public Elevator(int id) {
		this.id = id;
		setOperating();
	}
	
	public int getId() {
		return id;
	}
	
	public ElevatorState getElevatorState() {
		return elevatorState;
	}
	
	public ElevatorDirection getElevatorDirection() {
		return direction;
	}
	
	public void setElevatorDirection(ElevatorDirection direction) {
		this.direction = direction;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public void setElevatorState(ElevatorState elevatorState) {
		this.elevatorState = elevatorState;
	}
	
	public boolean isOperating() {
		return this.operating;
	}
	
	public void setDirection() {
		if(currentFloor<reqFloor || currentFloor<targetFloor) {
			setElevatorDirection(ElevatorDirection.UP);
			ElevatorController.updateElevatorDirections(this);
		}else if(currentFloor>reqFloor || currentFloor>targetFloor) {
			setElevatorDirection(ElevatorDirection.DOWN);
			ElevatorController.updateElevatorDirections(this);
		}else {
			setElevatorDirection(ElevatorDirection.NAN);
			ElevatorController.updateElevatorDirections(this);
		}
	}

	public void setOperating() {
		if(currentFloor==0 && direction == ElevatorDirection.NAN) {
			setElevatorState(ElevatorState.IDLE);
			this.floorPasses.clear();
		}else if(currentFloor!= reqFloor && ((currentFloor<reqFloor && direction == ElevatorDirection.UP)||(currentFloor>reqFloor && direction == ElevatorDirection.DOWN))){
			setElevatorState(ElevatorState.TO_PICKUP);			
			this.floorStopsMap = new LinkedHashMap<ElevatorState, NavigableSet<Integer>>();
			 // To let controller know that this elevator is ready to serve
		}else if(currentFloor==reqFloor) {
			setElevatorState(ElevatorState.PICKUP);			
			this.floorStopsMap = new LinkedHashMap<ElevatorState, NavigableSet<Integer>>();
		}else if(currentFloor!= targetFloor && ((currentFloor<targetFloor && direction == ElevatorDirection.UP)||(currentFloor>targetFloor && direction == ElevatorDirection.DOWN))) {
			setElevatorState(ElevatorState.TO_DROPOFF);			
			this.floorStopsMap = new LinkedHashMap<ElevatorState, NavigableSet<Integer>>();
		}else if(currentFloor==targetFloor) {
			setElevatorState(ElevatorState.TO_DROPOFF);			
			this.floorStopsMap = new LinkedHashMap<ElevatorState, NavigableSet<Integer>>();
		}
		setCurrentFloor(0); 
	}
	
	 public void setCurrentFloor(int currentFloor) {
		 this.currentFloor = currentFloor;
	 }
	
}
