package com.unimelb.swen30006.metromadness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// Imports for parsing XML files
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;


// The things we are generating
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.routers.SimpleRouter;
import com.unimelb.swen30006.metromadness.stations.ActiveStation;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;
import com.unimelb.swen30006.metromadness.trains.PassengerTrain;
import com.unimelb.swen30006.metromadness.trains.Train;

public class MapReader {

	private ArrayList<Train> trains;
	private HashMap<String, Station> stations;
	private HashMap<String, Line> lines;

	private boolean processed;
	private String filename;

	public MapReader(String filename){
		this.trains = new ArrayList<Train>();
		this.stations = new HashMap<String, Station>();
		this.lines = new HashMap<String, Line>();
		this.filename = filename;
		this.processed = false;
	}

	public void process(){
		try {
			// Build the doc factory
			FileHandle file = Gdx.files.internal("../core/assets/maps/melbourne.xml");
//			FileHandle file = Gdx.files.internal("../core/assets/maps/world.xml");
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);

			// Process stations
			Element stations = root.getChildByName("stations");
			Array<Element> stationList = stations.getChildrenByName("station");
			for(Element e : stationList){
				Station s = processStation(e);
				this.stations.put(s.getName(), s);
			}

			// Process Lines
			Element lines = root.getChildByName("lines");
			Array<Element> lineList = lines.getChildrenByName("line");
			for(Element e : lineList){
				Line l = processLine(e);
				this.lines.put(l.getName(), l);
			}

			// Process Trains
			Element trains = root.getChildByName("trains");
			Array<Element> trainList = trains.getChildrenByName("train");
			for(Element e : trainList){
				Train t = processTrain(e);
				this.trains.add(t);
			}

			this.processed = true;

		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	public Collection<Train> getTrains(){
		if(!this.processed) { this.process(); }
		return this.trains;
	}

	public Collection<Line> getLines(){
		if(!this.processed) { this.process(); }
		return this.lines.values();
	}

	public Collection<Station> getStations(){
		if(!this.processed) { this.process(); }
		return this.stations.values();
	}

	private Train processTrain(Element e) throws Exception{
		// Retrieve the values
		String type = e.get("type");
		String line = e.get("line");
		String start = e.get("start");
		String name = e.get("name");
		boolean dir = e.getBoolean("direction");

		// Retrieve the lines and stations
		Line l = this.lines.get(line);
		Station s = this.stations.get(start);

		// Make the train
		if(type.equals("BigPassenger")){
			return new PassengerTrain(l,s,dir,name, Train.BIG_PASSENGER_CAPACITY);
		} else if (type.equals("SmallPassenger")){
			return new PassengerTrain(l,s,dir,name, Train.SMALL_PASSENGER_CAPACITY);
		}  else if (type.equals("BigCargo")){
			return new CargoTrain(l,s,dir,name, Train.BIG_PASSENGER_CAPACITY,Train.BIG_CARGO_CAPACITY);
		}  else if (type.equals("SmallCargo")){
			return new CargoTrain(l,s,dir,name, Train.SMALL_PASSENGER_CAPACITY,Train.SMALL_CARGO_CAPACITY);
		} else {
			throw new Exception("Invalid train type " + type + " specified in XML");
		}
	}

	private Station processStation(Element e) throws Exception	 {
		String type = e.get("type");
		String name = e.get("name");
		int x_loc = e.getInt("x_loc")/8;
		int y_loc = e.getInt("y_loc")/8;
		String router = e.get("router");
		PassengerRouter r = createRouter(router);
		int maxPax = e.getInt("max_passengers");
		if(type.equals("Active")){
			return new ActiveStation(x_loc, y_loc, r, name, maxPax);
		} else if (type.equals("Passive")){
			return new Station(x_loc, y_loc, r, name);
		} else if (type.equals("Cargo")){
			return new CargoStation(x_loc, y_loc, r, name, maxPax);
		} else{
			throw new Exception("Invalid station type " + type + " specified in XML");
		}
	}

	private Line processLine(Element e){
		Color stationCol = extractColour(e.getChildByName("station_colour"));
		Color lineCol = extractColour(e.getChildByName("line_colour"));
		String name = e.get("name");
		Line l = new Line(stationCol, lineCol, name);

		Array<Element> stations = e.getChildrenByNameRecursively("station");
		for(Element s: stations){
			Station station = this.stations.get(s.get("name"));
			boolean twoWay = s.getBoolean("double");
			l.addStation(station, twoWay);
		}

		return l;
	}

	private PassengerRouter createRouter(String type){
		if(type.equals("simple")){
			return new SimpleRouter();
		}
		return null;
	}

	private Color extractColour(Element e){
		float red = e.getFloat("red")/255f;
		float green = e.getFloat("green")/255f;
		float blue = e.getFloat("blue")/255f;
		return new Color(red, green, blue, 1f);
	}



}
