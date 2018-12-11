package com.stephanie;

class Room {
    private String name, feature;
    private int capacity;
    private boolean alcohol;
    private int totalCapacity = 0;

    public Room(String name, int capacity, String feature, boolean alcohol) {
        this.name = name;
        this.capacity = capacity;
        this.feature = feature;
        this.alcohol = alcohol;
        totalCapacity = totalCapacity + capacity;
    }//constructor method

    /**
     * @return the totalCapacity
     */
    public int getTotalCapacity() {
        return totalCapacity;
    }

    /**
     * @param totalCapacity to set
     */
    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity (int capacity) {
        this.capacity = capacity;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature (String feature) {
        this.feature = feature;
    }
     public boolean getAlcohol() {
         return alcohol;
     }

     public void setAlcohol (boolean alcohol) {
         this.alcohol = alcohol;
     }
     
}