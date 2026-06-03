package repository;
import model.ExpenseSplit;

import java.util.List;

public interface ExpenseSplitRepository {

    void createSplit(ExpenseSplit split);

    List<ExpenseSplit> getSplitsByGroup(int groupId);

    void markAsPaid(int splitId);
}
