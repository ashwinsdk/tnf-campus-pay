package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstituteMoney {
    private int hostelBal;
    private int canteenBal;
    private int hackathonBal;
    private int libraryBal;
    private int workshopBal;

    public void setFineFromStudent(String type,int amount){
        switch (type){
            case "hackathon":
                hackathonBal=amount;
                break;
            case "canteen":
                canteenBal=amount;
                break;
            case "hostel":
                hostelBal=amount;
                break;
            case "workshop":
                workshopBal=amount;
                break;
            case "library":
                libraryBal=amount;
                break;
            default:
                break;
        }
    }
}
