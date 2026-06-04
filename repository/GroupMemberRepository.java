package repository;

import java.util.List;

public interface GroupMemberRepository {
    void addMember(int groupId, int studentId);

    List<Integer> getMembersByGroup(int groupId);
}
