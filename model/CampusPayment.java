package model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class CampusPayment {
    private String typeOfFine;
    private int fee;
    CampusPayment(String typeOfFine,int fee){
        this.typeOfFine = typeOfFine;
        this.fee=fee;
    }
}

class Canteen extends CampusPayment{
    Canteen() {
        super("canteen",1000);
    }
}
class Library  extends CampusPayment{
    Library() {
        super("library",500);
    }
}
class Hackathon extends CampusPayment{
    Hackathon() {
        super("hackathon",300);
    }
}
class WorkShop extends CampusPayment{
    WorkShop() {
        super("workshop",400);
    }
}
class Hostel extends CampusPayment{
    Hostel() {
        super("hostel",2000);
    }
}