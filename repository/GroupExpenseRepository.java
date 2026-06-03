package repository;
import model.GroupExpense;

import java.util.List;

public interface GroupExpenseRepository {
    int createGroup(GroupExpense group);

    GroupExpense getGroupById(int groupId);

    List<GroupExpense> getAllGroups();
}
