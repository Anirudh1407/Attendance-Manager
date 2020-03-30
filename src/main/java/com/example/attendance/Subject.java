package com.example.attendance;

public class Subject {
    static  private int minPer;
    private String name;
    private int ca,cd;
    private boolean changed;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    void setCa(int ca){
        this.ca = ca;
    }
     void setCd(int cd){
        this.cd = cd;
    }
     int getCa(){
        return ca;
    }
     int getCd(){
        return cd;
    }
     int perCalc(){
        if(cd!=0)
            return ca*100/cd;
        else
            return 0;
    }
     void setChanged(boolean c){
        changed = c;
    }
     boolean isChanged(){
        return changed;
    }

     static int getMinPer() {
        return minPer;
    }

     static void setMinPer(int minPer) {
        Subject.minPer = minPer;
    }

    boolean safe(){
        if(cd==0)
            return true;
        return ca*100/cd > minPer;
    }

    int needToAttend(){
        float x = (float) minPer/100;
        return (int) (((x * cd) - ca) / (1 - x)) + 1;
    }

}
