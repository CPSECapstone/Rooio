package com.rooio.repairs;

public class JobsData  {
    String status;
    String name;
    String image;
    String repairType;
    String address;
    String time;

    public JobsData(String status, String name, String image, String repairType, String address, String time ){
        this.status = status;
        this.name = name;
        this.image = image;
        this.repairType = repairType;
        this.address = address;
        this.time = time;

    }
}
