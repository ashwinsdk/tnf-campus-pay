package service;

import java.util.List;

public interface SplitwiseService {
    void createExpenseGroup(
            String groupName,
            double totalAmount,
            int createdBy,
            List<Integer> members);

    void viewGroupBalances(int groupId);

    void settleExpense(int splitId);
}
