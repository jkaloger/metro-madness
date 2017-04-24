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
        boolean b = this.getNumPassengers() < this.getMaxPassengers();
        return this.maxCargoWeight - this.getTotalCargo() > p.getCargo().getWeight()
                && b;
    }

    @Override
    public void seekTrack() {


        super.seekTrack();
    }

    @Override
    public void checkDirection() {
        // We are ready to depart, find the next track and wait until we can enter
        boolean endOfLine = this.trainLine.endOfLine(this.station);
        if(endOfLine){
            this.forward = !this.forward;
        } else if(this.findNextCargoStation()) {
            this.forward = !this.forward;
        }
    }

    public boolean findNextCargoStation() {
        /* find next cargo station:
            return true if ahead
            return false if behind
         */

        return true;
    }
}
