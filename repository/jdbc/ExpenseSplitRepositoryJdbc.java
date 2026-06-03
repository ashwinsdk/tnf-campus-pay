package repository.jdbc;

import model.ExpenseSplit;
import repository.ExpenseSplitRepository;

import java.util.List;

public class ExpenseSplitRepositoryJdbc implements ExpenseSplitRepository {

    @Override
    public void createSplit(ExpenseSplit split) {

    }

    @Override
    public List<ExpenseSplit> getSplitsByGroup(int groupId) {

        return null;
    }

    @Override
    public void markAsPaid(int splitId) {

    }
}