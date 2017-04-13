package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/**
 * Created by jk on 13/4/17.
 */
public class BigCargoTrain extends CargoTrain {
    public BigCargoTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name);
    }
}
