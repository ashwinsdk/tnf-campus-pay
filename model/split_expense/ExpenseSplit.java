package model.split_expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseSplit {
    private int splitId;
    private int groupId;
    private int studentId;
    private double amountOwed;
    private String status;

}
 