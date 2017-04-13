package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class BigPassengerTrain extends PassengerTrain {

	public BigPassengerTrain(Line trainLine, Station start, boolean forward, String name) {
		super(trainLine, start, forward, name);
		maxPassengers = 80;
		dotSize = 20.0f;
		dotColor = Color.MAROON;
	}

}
