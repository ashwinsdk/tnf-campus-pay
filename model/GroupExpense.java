package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupExpense {
    private int groupId;
    private String groupName;
    private double totalAmount;
    private int createdBy;
    private Timestamp createdAt;

}
