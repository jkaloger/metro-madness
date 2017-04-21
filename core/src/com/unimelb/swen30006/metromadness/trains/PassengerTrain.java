package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/**
 * Created by Jack Kaloger on 17/4/17.
 */
public class PassengerTrain extends Train {

    private Color dotColor;
    private float dotSize;

    public PassengerTrain(Line trainLine, Station start, boolean forward, String name, int maxPassengers) {
        super(trainLine, start, forward, name, maxPassengers);
        if(maxPassengers < 80) {
            this.dotColor = Color.MAROON;
            this.dotSize = 10.0f;
        } else {
            this.dotColor = Color.LIGHT_GRAY;
            this.dotSize = 20.0f;
        }
    }

    @Override
    public void embark(Passenger p) {
        this.addPassenger(p);
    }

    public boolean hasSpaceFree() {
        if(this.getNumPassengers() > this.getMaxPassengers()){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            float percentage = this.getNumPassengers()/dotSize;
            renderer.setColor(col.cpy().lerp(dotColor, percentage));
            // We also get slightly bigger with passengers
            renderer.circle(this.getX(), this.getY(), TRAIN_WIDTH*(1+percentage));
        }
    }
}
