package com.unimelb.swen30006.metromadness.tracks;import java.util.ArrayList;import com.badlogic.gdx.graphics.Color;import com.badlogic.gdx.graphics.glutils.ShapeRenderer;import com.unimelb.swen30006.metromadness.stations.CargoStation;import com.unimelb.swen30006.metromadness.stations.Station;public class Line {		// The colour of this line	private Color lineColour;	private Color trackColour;		// The name of this line	private String name;	// The stations on this line	private ArrayList<Station> stations;	// The tracks on this line between stations	private ArrayList<Track> tracks;			// Create a line	public Line(Color stationColour, Color lineColour, String name){		// Set the line colour		this.lineColour = stationColour;		this.trackColour = lineColour;		this.name = name;				// Create the data structures		this.stations = new ArrayList<Station>();		this.tracks = new ArrayList<Track>();	}			public void addStation(Station s, Boolean two_way){		// We need to build the track if this is adding to existing stations		if(this.stations.size() > 0){			// Get the last station			Station last = this.stations.get(this.stations.size()-1);						// Generate a new track			Track t;			if(two_way){				t = new DualTrack(last.getPosition(), s.getPosition(), this.trackColour);			} else {				t = new SingleTrack(last.getPosition(), s.getPosition(), this.trackColour);			}			this.tracks.add(t);		}				// Add the station		s.registerLine(this);		this.stations.add(s);	}		@Override	public String toString() {		return "Line [lineColour=" + lineColour + ", trackColour=" + trackColour + ", name=" + name + "]";	}	public boolean endOfLine(Station s) {		int index = this.stations.indexOf(s);		return (index==0 || index==this.stations.size()-1);	}			public Track nextTrack(Station currentStation, boolean forward) {		// Determine the track index		int curIndex = this.stations.lastIndexOf(currentStation);		// Increment to retrieve		if(!forward){ curIndex -=1;}		return this.tracks.get(curIndex);	}		public Station nextStation(Station s, boolean forward) throws Exception{		if(this.stations.contains(s)){			int curIndex = this.stations.lastIndexOf(s);			if(forward){ curIndex+=1;}else{ curIndex -=1;}						// Check index is within range			if((curIndex < 0) || (curIndex > this.stations.size()-1)){				throw new Exception();			} else {				return this.stations.get(curIndex);			}		} else {			throw new Exception();		}	}	public boolean checkCargoStationsAhead(Station s, boolean forward) throws Exception {		if(this.stations.contains(s)){			int i;			if(forward)				i = 1;			else				i = -1;			int curIndex = this.stations.lastIndexOf(s) + i;			while(curIndex != this.stations.lastIndexOf(s)) {				if(this.stations.get(curIndex).getClass() == CargoStation.class)					return true;				if(curIndex == this.stations.size()-1 || curIndex == 0) {					break;				}				if(forward){ curIndex+=1;}else{ curIndex -=1;}			}			return false;		} else {			throw new Exception();		}	}	public int getNumCargoStations() {	    int total = 0;	    for(Station s : this.stations) {	        if(s.getClass() == CargoStation.class)	            total++;        }        return total;    }		public void render(ShapeRenderer renderer){		// Set the color to our line		renderer.setColor(trackColour);			// Draw all the track sections		for(Track t: this.tracks){			t.render(renderer);		}		}	public String getName() {		return name;	}	public Color getLineColour() {		return lineColour;	}	public Color getTrackColour() {		return trackColour;	}	public ArrayList<Station> getStations() {		return stations;	}	public Station getStation(int index) {		return stations.get(index);	}	public int getNumStations() {		return stations.size();	}}