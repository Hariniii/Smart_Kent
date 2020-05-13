package com.code.demo;

public class ElevatorMain {
	
	private static ElevatorController elevatorController;
    private static Thread elevatorControllerThread;

	public static void main(String[] args) {
		elevatorController = ElevatorController.getInstance();
        elevatorControllerThread = new Thread(elevatorController);
        elevatorControllerThread.start();
	}

}
