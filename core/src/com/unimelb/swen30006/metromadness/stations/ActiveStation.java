package com.unimelb.swen30006.metromadness.stations;

import java.util.ArrayList;
import java.util.Iterator;

import com.unimelb.swen30006.metromadness.trains.PassengerTrain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class ActiveStation extends Station {
	// Logger
	private static Logger logger = LogManager.getLogger();
	
	private PassengerGenerator g;
	private ArrayList<Passenger> waiting;
	private float maxVolume;
	
	public ActiveStation(float x, float y, PassengerRouter router, String name, float maxPax) {
		super(x, y, router, name);
		this.waiting = new ArrayList<Passenger>();
		this.g = new PassengerGenerator(this, this.getLines(), maxPax);
		this.maxVolume = maxPax;
	}
	
	@Override
	public boolean enter(Train t) {
		if(getTrains().size() >= PLATFORMS){
			return false;
		} else {
			// Add the train
			this.getTrains().add(t);
			// Add the waiting passengers
			Iterator<Passenger> pIter = this.waiting.iterator();
			while(pIter.hasNext()){
				Passenger p = pIter.next();
				if(t.hasSpaceFree(p)) {
					logger.info("Passenger "+p.id+" carrying "+p.getCargo().getWeight() +" kg cargo embarking at "+this.getName()+" heading to "+p.getDestination().getName());
					t.embark(p);
					pIter.remove();
				} else {
					// Do nothing, already waiting
					break;
				}
			}
			
			//Do not add new passengers if there are too many already
			if (this.waiting.size() > maxVolume){
				return false;
			}
			// Add the new passenger
			Passenger[] ps = this.g.generatePassengers();
			for(Passenger p: ps){
				if(t.hasSpaceFree(p)) {
					logger.info("Passenger "+p.id+" carrying "+p.getCargo().getWeight() +" kg embarking at "+this.getName()+" heading to "+p.getDestination().getName());
					t.embark(p);
				} else {
					this.waiting.add(p);
				}
			}
		}

		return true;
	}
	
	public void render(ShapeRenderer renderer){
		float radius = RADIUS;
		for(int i=0; (i<this.getLines().size() && i<MAX_LINES); i++){
			Line l = this.getLines().get(i);
			renderer.setColor(l.getLineColour());
			renderer.circle(this.getPosition().x, this.getPosition().y, radius, NUM_CIRCLE_STATMENTS);
			radius = radius - 1;
		}
		
		// Calculate the percentage
		float t = this.getTrains().size()/(float)PLATFORMS;
		Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
		if(this.waiting.size() > 0){
			c = Color.RED;
		}
		
		renderer.setColor(c);
		renderer.circle(this.getPosition().x, this.getPosition().y, radius, NUM_CIRCLE_STATMENTS);
	}

	@Override
	public boolean shouldStop(Train t) {
		return t.getClass() == PassengerTrain.class;
	}
}
