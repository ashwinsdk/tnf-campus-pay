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
    protected CampusPayment(String typeOfFine, int fee){
        this.typeOfFine = typeOfFine;
        this.fee=fee;
    }
}



