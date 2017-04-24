package com.unimelb.swen30006.metromadness.stations;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;
import com.unimelb.swen30006.metromadness.trains.Train;

/**
 * Created by jk on 17/4/17.
 */
public class CargoStation extends ActiveStation {
    public CargoStation(float x, float y, PassengerRouter router, String name, float maxPax) {
        super(x, y, router, name, maxPax);
    }

    @Override
    public boolean shouldStop(Train t) {
        return t.getClass() == CargoTrain.class;
    }

    @Override
    protected Passenger[] generatePassengers() {
        return this.getPassengerGenerator().generatePassengers();
    }

    @Override
    protected PassengerGenerator createPassengerGenerator(float maxPax) {
        return new PassengerGenerator(this, this.getLines(), maxPax, true);
    }
}
