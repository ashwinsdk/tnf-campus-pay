package repository;
import model.split_expense.ExpenseSplit;

import java.util.List;

public interface ExpenseSplitRepository {

    void createSplit(ExpenseSplit split);

    List<ExpenseSplit> getSplitsByGroup(int groupId);

    void markAsPaid(int splitId);
}
