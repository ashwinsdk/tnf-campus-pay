package repository.jdbc;

import model.GroupExpense;
import repository.GroupExpenseRepository;

import java.util.List;

public class GroupExpenseRepositoryJdbc
        implements GroupExpenseRepository {

    @Override
    public int createGroup(GroupExpense group) {
        return 0;
    }

    @Override
    public GroupExpense getGroupById(int groupId) {
        return null;
    }

    @Override
    public List<GroupExpense> getAllGroups() {
        return null;
    }
}