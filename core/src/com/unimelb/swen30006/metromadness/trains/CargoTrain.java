package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/**
 * Created by Jack Kaloger on 17/4/17.
 */
public class CargoTrain extends Train {

    // maxmimum amount of cargo train can carry
    private int maxCargoWeight;

    public CargoTrain(Line trainLine, Station start, boolean forward, String name, int maxPassengers, int maxCargoWeight) {
        super(trainLine, start, forward, name, maxPassengers);
        this.maxCargoWeight = maxCargoWeight;
    }

    @Override
    public boolean shouldEnter(Station s) {
        return true;
    }

    @Override
    public void embark(Passenger p) {

    }

    public boolean hasSpaceFree(Passenger p) {
        // is there enough space for passengers?
        boolean b = this.getNumPassengers() < this.getMaxPassengers();
        // there must also be enough space for the cargo.
        return this.maxCargoWeight - this.getTotalCargo() > p.getCargo().getWeight()
                && b;
    }
    @Override
    public void checkDirection() {
        // We are ready to depart, find the next track and wait until we can enter
        boolean endOfLine = this.trainLine.endOfLine(this.station);
        if(endOfLine){
            this.forward = !this.forward;
        } else if(!this.findNextCargoStation()) {
            this.forward = !this.forward;
        }
    }
    /* find next cargo station:
     * return true if ahead
     * return false if behind
    */
    public boolean findNextCargoStation() {

        try {
            return this.trainLine.checkCargoStationsAhead(this.station, this.forward);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean seekTrack() {
        // edge case, if there is only 1 cargo station, the train cant go anywhere...
        if(this.trainLine.getNumCargoStations() > 1)
            return super.seekTrack();
        else
            return false;
    }
}
