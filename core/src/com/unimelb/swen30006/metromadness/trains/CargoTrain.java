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
        return s.isCargoStation;
    }

    @Override
    public void embark(Passenger p) {

    }

    @Override
    public boolean hasSpaceFree() {
        return false;
    }
}
