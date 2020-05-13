package com.code.demo;

public class ElevatorRequest {
		
    private int requestFloor;
    private int targetFloor;
  

    public int getRequestFloor() {
        return requestFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }
    
    public void setRequestFloor(int reqFloor) {
    	this.requestFloor = reqFloor;
    }
    
    public void setTargetFloor(int targetFloor) {
    	this.targetFloor = targetFloor;
    }


    /**
     * Submit the request to the ElevatorController to select the
     * optimal elevator for this request
     * @return
     */
    public Elevator submitRequest(){
        return ElevatorController.getInstance().selectElevator(this);
    }
    
    Elevator elevator = submitRequest();
}
