package com.code.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
public class FloorResorce {
	ElevatorRequest elvReq = new ElevatorRequest();
	Elevator elv = elvReq.submitRequest();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CalculateTime getTime(@QueryParam("fromFloor") int fromFloor, @QueryParam("toFloor") int toFloor) {
		CalculateTime time = new CalculateTime();
		time.setETA(time.getTime(fromFloor, toFloor, elv));
		return time;
		
	}
//	public Floor getTime(@QueryParam("fromFloor") int fromFloor, @QueryParam("toFloor") int toFloor) {
//		System.out.println("GetFloor");
//		System.out.println("Get "+ fromFloor);
//		System.out.println("From "+ toFloor);
//		Floor f = new Floor();
//		f.setFromFloor(fromFloor);
//		f.setToFloor(toFloor);
//		return f;
//		
//	}
}
