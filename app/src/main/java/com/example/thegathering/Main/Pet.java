package com.example.thegathering.Main;

import java.sql.Timestamp;
import java.util.Random;

public class Pet {
    public String name;
    private String born;
    private boolean dead;

    private int max_stat_value = 100;
    private int min_stat_value = 0;

    private int statSocial;
    private int statHappy;
    private int statFed;
    private int statLove;
    private int statFit;

    Pet(){
        this.name = "Pet";
        this.statHappy = 80;
        this.statFed = 80;
        this.statSocial = 80;
        this.statLove = 80;
        this.statFit = 80;
        this.born = new Timestamp(System.currentTimeMillis()).toString();
        this.dead = false;
    }

    void decreaseStats(){
        Random r = new Random();

        this.love(-r.nextInt(5));
        this.cheer(-r.nextInt(4));
        this.feed(-r.nextInt(3));
        this.socialize(-r.nextInt(2));

        if (this.fed() > (max_stat_value/2))
            this.exercise(-r.nextInt(5));
        else
            this.exercise(-r.nextInt(2));
    }

    void feed(int value) {
        this.statFed = increaseStat(this.statFed, value);
    }
    void socialize(int value) {
        this.statSocial = increaseStat(this.statSocial, value);
    }
    void cheer(int value) {
        this.statHappy = increaseStat(this.statHappy, value);
    }
    void love(int value) {
        this.statLove = increaseStat(this.statLove, value);
    }
    void exercise(int value) {
        this.statFit = increaseStat(this.statFit, value);
    }

    private int increaseStat(int stat, int value){
        if (stat+value <= max_stat_value && stat+value > min_stat_value)
            stat+=value;
        else
            stat = value>0?max_stat_value:min_stat_value;
        return stat;
    }

    int fed(){
        return this.statFed;
    }
    int happy(){
        return this.statHappy;
    }
    int social(){
        return this.statSocial;
    }
    int love(){
        return this.statLove;
    }
    int fit() {return this.statFit;}
    boolean dead() {return this.dead;}
    String getBorn() {
        return born;
    }


    void setStatSocial(int statSocial) {
        this.statSocial = statSocial;
    }
    void setStatFit(int statFit) {
        this.statFit = statFit;
    }
    void setStatHappy(int statHappy) {
        this.statHappy = statHappy;
    }
    void setStatFed(int statFed) {
        this.statFed = statFed;
    }
    void setStatLove(int statLove) {
        this.statLove = statLove;
    }
    void setBorn(String born) {
        this.born = born;
    }

    void die(){
        dead = true;
    }
}
