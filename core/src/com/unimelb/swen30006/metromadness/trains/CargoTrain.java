package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/**
 * Created by jk on 17/4/17.
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
        if(s.isCargoStation) {
            return true;
        }

        return false;
    }
}
