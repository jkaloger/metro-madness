package com.unimelb.swen30006.metromadness.tracks;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.metromadness.trains.Train;

import java.awt.geom.Point2D;

/**
 * Created by jk on 18/4/17.
 */
public class SingleTrack extends Track {
    public boolean occupied;
    public SingleTrack(Point2D.Float start, Point2D.Float end, Color trackCol) {
        super(start, end, trackCol);
        this.occupied = false;
    }

    @Override
    public boolean canEnter(boolean forward) {
        return !this.occupied;
    }

    @Override
    public void enter(Train t) {
        this.occupied = true;
    }

    @Override
    public String toString() {
        return "Track [startPos=" + startPos + ", endPos=" + endPos + ", trackColour=" + trackColour + ", occupied="
                + occupied + "]";
    }

    @Override
    public void leave(Train t){
        this.occupied = false;
    }


}
