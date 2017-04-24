package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;

public class Passenger {

	final public int id;
	private Station beginning;
	private Station destination;
	private float travelTime;
	private boolean reachedDestination;
	private Cargo cargo;
	
	public Passenger(int id, Random random, Station start, Station end, Cargo cargo){
		this.id = id;
		this.beginning = start;
		this.destination = end;
		this.reachedDestination = false;
		this.travelTime = 0;
		/* replaced Passenger.generateCargo() with PassengerGenerator.generateCargo() */
		this.cargo = cargo;
	}
	
	public void update(float time){
		if(!this.reachedDestination){
			this.travelTime += time;
		}
	}
	public Cargo getCargo(){
		return cargo;
	}
	


	public Station getDestination() {
		return destination;
	}
}
