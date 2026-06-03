package repository;
import model.GroupMember;

import java.util.List;

public interface GroupMemberRepository {
    void addMember(int groupId, int studentId);

    List<Integer> getMembersByGroup(int groupId);
}
