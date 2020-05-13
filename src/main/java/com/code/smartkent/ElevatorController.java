package com.code.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public final class ElevatorController implements Runnable {
	
	private boolean stopController;
	
	//All the UP moving elevators
	private static Map<Integer, Elevator> upMovingMap = new HashMap<Integer, Elevator>();
	
	 // All the DOWN moving elevators
    private static Map<Integer, Elevator> downMovingMap = new HashMap<Integer, Elevator>();
    
    private static List<Elevator> elevatorList = new ArrayList<Elevator>(2);
    
    private static final ElevatorController instance = new ElevatorController();
    private ElevatorController(){
        if(instance != null){
            throw new IllegalStateException("Already instantiated");
        }
        setStopController(false);
        initializeElevators();
    }

    public static ElevatorController getInstance(){
        return instance;
    }
    
    //select an elevator for the request optimally
    public synchronized Elevator selectElevator(ElevatorRequest elevatorRequest) {

        Elevator elevator = null;

        ElevatorDirection elevatorDirection = getRequestedElevatorDirection(elevatorRequest);
        int requestedFloor = elevatorRequest.getRequestFloor();
        int targetFloor = elevatorRequest.getTargetFloor();

        elevator = findElevator(elevatorDirection, requestedFloor, targetFloor);

        // So that elevators can start moving again.
        notifyAll();
        return elevator;
    }
    
    private static void initializeElevators(){
        for(int i=0; i<2; i++){
            Elevator elevator = new Elevator(i);
            Thread t = new Thread();
            t.start();

            elevatorList.add(elevator);
        }
    }
    
    private static ElevatorDirection getRequestedElevatorDirection(ElevatorRequest elevatorRequest){
    	ElevatorDirection elevatorDirection = null;
        int requestedFloor = elevatorRequest.getRequestFloor();
        int targetFloor = elevatorRequest.getTargetFloor();

        if(targetFloor - requestedFloor > 0){
        	elevatorDirection = ElevatorDirection.UP;
        } else {
        	elevatorDirection = ElevatorDirection.DOWN;
        }

        return elevatorDirection;
    }
    
    //select an elevator 
    private static Elevator findElevator(ElevatorDirection elevatorDirection, int requestedFloor, int targetFloor) {
    	Elevator elevator = null;
    	 // Data structure to hold distance of eligible elevators from the request floor
        // The keys represent the current distance of an elevator from request floor
        TreeMap<Integer, Integer> sortedKeyMap = new TreeMap<Integer, Integer>();
        
        if(elevatorDirection.equals(ElevatorDirection.UP)){
        // Let's go over all elevators that are going UP
	        for(Map.Entry<Integer, Elevator> elvMap : upMovingMap.entrySet()){
	            Elevator elv = elvMap.getValue();
	            Integer distance = requestedFloor - elv.getCurrentFloor();
	            if(distance < 0 && elv.getElevatorDirection().equals(ElevatorDirection.UP)){
	                // No point selecting these elevators. They have already passed by our request floor
	                continue;
	            } else {
	                sortedKeyMap.put(Math.abs(distance), elv.getId());
	            }
	        }
	        
	        Integer selectedElevatorId = sortedKeyMap.firstEntry().getValue();
	        elevator = upMovingMap.get(selectedElevatorId);
        }else if(elevatorDirection.equals(ElevatorDirection.DOWN)){
            // Let's go over all elevators that are going DOWN
            for(Map.Entry<Integer, Elevator> elvMap : downMovingMap.entrySet()){
                Elevator elv = elvMap.getValue();
                Integer distance = elv.getCurrentFloor() - requestedFloor;
                if(distance < 0 && elv.getElevatorDirection().equals(ElevatorDirection.DOWN)){
                    // No point selecting these elevators. They have already passed by our requested floor
                    continue;
                } else {
                    sortedKeyMap.put(Math.abs(distance), elv.getId());
                }
            }
            // TODO - potential NullPointerException
            Integer selectedElevatorId = sortedKeyMap.firstEntry().getValue();
            elevator = downMovingMap.get(selectedElevatorId);

        }
        return elevator;
    }
    
    //update the direction of elevator as soon as it change its direction
    public static synchronized void updateElevatorDirections(Elevator elevator) {
    	if(elevator.getElevatorDirection().equals(ElevatorDirection.UP)) {
    		upMovingMap.put(elevator.getId(), elevator);
            downMovingMap.remove(elevator.getId());
    	}else if(elevator.getElevatorDirection().equals(ElevatorDirection.DOWN)) {
    		downMovingMap.put(elevator.getId(), elevator);
            upMovingMap.remove(elevator.getId());
    	}else if(elevator.getElevatorDirection().equals(ElevatorDirection.NAN)) {
    		upMovingMap.remove(elevator.getId());
            downMovingMap.remove(elevator.getId());
    	}
    }
    
    @Override
    public void run() {
        stopController =  false;
        while(true){
            try {
                Thread.sleep(100);
                if(stopController){
                    break;
                }
            } catch (InterruptedException e){
                System.out.println(e.getStackTrace());
            }
        }
    }
    
    public void setStopController(boolean stop){
        this.stopController = stop;

    }

    public synchronized List<Elevator> getElevatorList() {
        return elevatorList;
    }

    public boolean isStopController() {
        return stopController;
    }

}
