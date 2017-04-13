package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;


/**
 * Created by jk on 13/4/17.
 */
public abstract class CargoTrain extends Train {

    public CargoTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name);
        maxPassengers = 100;
        dotSize = 10;
        dotColor = Color.GREEN;
    }

    @Override
    public void embark(Passenger p) throws Exception {
        if(this.passengers.size() > maxPassengers){
            throw new Exception();
        }
        this.passengers.add(p);
    }

    @Override
    public float getDepartureTime() {
        if(this.station.isCargoStation) {
            return this.station.getDepartureTime();
        }

        return 0;
    }
}
