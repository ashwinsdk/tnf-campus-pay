package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private int transactionId;
    private int senderId;
    private int receiverId;
    private double amount;
    private String transactionType;
    private Timestamp transactionTime;
}