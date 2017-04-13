package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/**
 * Created by jk on 13/4/17.
 */
public abstract class PassengerTrain extends Train {
    public PassengerTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name);
    }

    @Override
    public void embark(Passenger p) throws Exception {
        if(this.passengers.size() > maxPassengers){
            throw new Exception();
        }
        this.passengers.add(p);
    }

    @Override
    public Station findNextStation() throws Exception {
        return this.trainLine.nextStation(this.station, this.forward);
    }
}
