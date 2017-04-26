package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.tracks.Track;

public abstract class Train {
	
	// Logger
	private static Logger logger = LogManager.getLogger();
	// The state that a train can be in 
	public enum State {
		IN_STATION, READY_DEPART, ON_ROUTE, WAITING_ENTRY, FROM_DEPOT
	}

	// Constants
	public static final int MAX_TRIPS=4;
	public static final Color FORWARD_COLOUR = Color.ORANGE;
	public static final Color BACKWARD_COLOUR = Color.VIOLET;
	public static final float TRAIN_WIDTH=4;
	public static final float TRAIN_LENGTH = 6;
	public static final float TRAIN_SPEED=50f;
	public static final int BIG_PASSENGER_CAPACITY=80;
	public static final int SMALL_PASSENGER_CAPACITY=10;
	public static final int BIG_CARGO_CAPACITY=1000;
	public static final int SMALL_CARGO_CAPACITY=200;
	
	// The train's name
	
	private String name;

	// The line that this is traveling on
	protected Line trainLine;

	// the maximum number of passengers the train can carry
	private int maxPassengers;

	// Passenger Information
	private ArrayList<Passenger> passengers;
	private float departureTimer;
	
	// Station and track and position information
	protected Station station;
	protected Track track;
	private Point2D.Float pos;
	private Station targetStation;

	// Direction and direction
	protected boolean forward;
	protected State state;

	// State variables
	private int numTrips;
	private boolean disembarked;


	private State previousState = null;

	
	public Train(Line trainLine, Station start, boolean forward, String name, int maxPassengers){
		this.trainLine = trainLine;
		this.station = start;
		this.state = State.FROM_DEPOT;
		this.forward = forward;
		this.passengers = new ArrayList<Passenger>();
		this.name = name;
		this.maxPassengers = maxPassengers;
	}

	public void update(float delta){
		// Update all passengers
		for(Passenger p: this.passengers){
			p.update(delta);
		}
		boolean hasChanged = false;
		if(previousState == null || previousState != this.state){
			previousState = this.state;
			hasChanged = true;
		}
		
		// Update the state
		switch(this.state) {
		case FROM_DEPOT:
			if(hasChanged){
				logger.info(this.name+ " is travelling from the depot: "+this.station.getName()+" Station...");
			}
			
			this.departDepot();
			break;
		case IN_STATION:
			this.waitAtStation(delta);
			if(hasChanged){
				logger.info(this.name+" is in "+this.station.getName()+" Station.");
			}
			break;
		case READY_DEPART:
			if(hasChanged){
				logger.info(this.name+ " is ready to depart for "+this.station.getName()+" Station!");
			}
			
			this.departStation();
			break;
		case ON_ROUTE:
			if(hasChanged){
				logger.info(this.name+ " enroute to "+this.station.getName()+" Station!");
			}
			
			this.checkArrival(delta);
			break;
		case WAITING_ENTRY:
			if(hasChanged){
				logger.info(this.name+ " is awaiting entry "+this.station.getName()+" Station..!");
			}
			
			this.enterStation();
			break;
		}


	}

	private void departDepot() {
		// We have our station initialized we just need to retrieve the next track, enter the
		// current station officially and mark as in station
		if(this.station.canEnter(this.trainLine)){
			this.station.enter(this);
			this.pos = (Point2D.Float) this.station.getPosition().clone();
			this.state = State.IN_STATION;
			this.disembarked = false;
		}
	}

	private void waitAtStation(float delta) {
		if(!station.shouldStop(this)) {
			this.seekTrack();
			this.departStation();
			return;
		}
		// When in station we want to disembark passengers
		// and wait 10 seconds for incoming passengers
		if(!this.disembarked){
			this.disembark();
			this.departureTimer = this.station.getDepartureTime();
			this.disembarked = true;
		} else {
			// Count down if departure timer.
			if(this.departureTimer>0){
				this.departureTimer -= delta;
			} else {
				this.seekTrack();
			}
		}
	}

	public void seekTrack() {
	    this.checkDirection();
        this.track = this.trainLine.nextTrack(this.station, this.forward);
        this.state = State.READY_DEPART;
	}

	private void departStation() {
		// When ready to depart, check that the track is clear and if
		// so, then occupy it if possible.
		if(this.track.canEnter(this.forward)) {
			try {
				// Find the next
				Station next = this.trainLine.nextStation(this.station, this.forward);
				// Depart our current station
				this.station.depart(this);
				this.station = next;

			} catch (Exception e) {
//					e.printStackTrace();
			}
			this.track.enter(this);
			this.state = State.ON_ROUTE;
		}
	}

	private void checkArrival(float delta) {
		// Checkout if we have reached the new station
		if(this.pos.distance(this.station.getPosition()) < 10 ){
			this.state = State.WAITING_ENTRY;
		} else {
			move(delta);
		}
	}

	private void enterStation() {
		// Waiting to enter, we need to check the station has room and if so
		// then we need to enter, otherwise we just wait
		if(this.station.canEnter(this.trainLine)){
			this.track.leave(this);
			this.pos = (Point2D.Float) this.station.getPosition().clone();
			this.station.enter(this);
			this.state = State.IN_STATION;
			this.disembarked = false;
		}
	}

	public void move(float delta){
		// Work out where we're going
		float angle = angleAlongLine(this.pos.x,this.pos.y,this.station.getPosition().x,this.station.getPosition().y);
		float newX = this.pos.x + (float)( Math.cos(angle) * delta * TRAIN_SPEED);
		float newY = this.pos.y + (float)( Math.sin(angle) * delta * TRAIN_SPEED);
		this.pos.setLocation(newX, newY);
	}

	public abstract void embark(Passenger p);
	public abstract boolean hasSpaceFree(Passenger p);


	public ArrayList<Passenger> disembark(){
		ArrayList<Passenger> disembarking = new ArrayList<Passenger>();
		Iterator<Passenger> iterator = this.passengers.iterator();
		while(iterator.hasNext()){
			Passenger p = iterator.next();
			if(this.station.shouldLeave(p)){
				logger.info("Passenger "+p.id+" is disembarking at "+this.station.getName());
				disembarking.add(p);
				iterator.remove();
			}
		}
		return disembarking;
	}

	@Override
	public String toString() {
		return "Train [line=" + this.trainLine.getName() +", departureTimer=" + departureTimer + ", pos=" + pos + ", forward=" + forward + ", state=" + state
				+ ", numTrips=" + numTrips + ", disembarked=" + disembarked + "]";
	}

	public boolean inStation(){
		return (this.state == State.IN_STATION || this.state == State.READY_DEPART);
	}
	
	public float angleAlongLine(float x1, float y1, float x2, float y2){	
		return (float) Math.atan2((y2-y1),(x2-x1));
	}

	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			renderer.setColor(col);
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH);
		}
	}

	public int getMaxPassengers() {
		return maxPassengers;
	}

	public boolean shouldEnter(Station s) {
		return true;
	}

	public void addTrip()
	{
		this.numTrips++;
	}

	public ArrayList<Passenger> getPassengers() {
		return passengers;
	}

	public float getX() {
		return this.pos.x;
	}

	public float getY(){
		return this.pos.y;
	}

	public int getNumPassengers() {
		return this.passengers.size();
	}

	public void addPassenger(Passenger p) {
		passengers.add(p);
	}

	public String getName() {
		return name;
	}

	public Station getStation() {
		return station;
	}

	public boolean isForward() {
		return forward;
	}

	public int getTotalCargo() {
		int total = 0;
		for (Passenger p : passengers) {
			total += p.getCargo().getWeight();
		}
		return total;
	}

	public void checkDirection() {
        // We are ready to depart, find the next track and wait until we can enter
        boolean endOfLine = this.trainLine.endOfLine(this.station);
        if(endOfLine){
            this.forward = !this.forward;
        }
    }
}
