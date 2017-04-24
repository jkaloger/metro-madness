package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerGenerator {
	
	// Random number generator
	static final private Random random = new Random(30006);
	
	// Passenger id generator
	static private int idGen = 1;
	
	
	// The station that passengers are getting on
	public Station s;
	// The line they are travelling on
	public ArrayList<Line> lines;
	
	// The max volume
	public float maxVolume;

	private boolean generateCargo;
	
	public PassengerGenerator(Station s, ArrayList<Line> lines, float max, boolean generateCargo){
		this.s = s;
		this.lines = lines;
		this.maxVolume = max;
		this.generateCargo = generateCargo;
	}
	
	public Passenger[] generatePassengers(){
		int count = random.nextInt(4)+1;
		Passenger[] passengers = new Passenger[count];
		for(int i=0; i<count; i++){
			passengers[i] = generatePassenger(random);
		}
		return passengers;
	}

	private Passenger generatePassenger(Random random){
		// Pick a random station from the line
		Line l = this.lines.get(random.nextInt(this.lines.size()));
		int current_station = l.getStations().indexOf(this.s);
		boolean forward = random.nextBoolean();
		
		// If we are the end of the line then set our direction forward or backward
		if(current_station == 0){
			forward = true;
		} else if (current_station == l.getNumStations()-1){
			forward = false;
		}
		
		// Find the station
		int index = 0;

		/* picks random station, but we need it to pick a cargo station if have cargo */
		if (forward){
			index = random.nextInt(l.getNumStations()-1-current_station) + current_station + 1;
		} else {
			index = current_station - 1 - random.nextInt(current_station);
		}
		Station s = l.getStation(index);

        return new Passenger(idGen++, random, this.s, s, generateCargo(random));
	}

	private Cargo generateCargo(Random random) {
		if(!generateCargo)
			return new Cargo(0);
		return new Cargo(random.nextInt(51));
	}



	
}
