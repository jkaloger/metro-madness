package com.unimelb.swen30006.metromadness.passengers;

/**
 * Created by jk on 24/4/17.
 */
public class Cargo{
    private int weight;

    public Cargo(int weight){
        this.setWeight(weight);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}