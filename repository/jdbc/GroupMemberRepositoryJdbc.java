package repository.jdbc;

import repository.GroupMemberRepository;

import java.util.List;

public class GroupMemberRepositoryJdbc implements GroupMemberRepository {

    @Override
    public void addMember(int groupId, int studentId) {

    }

    @Override
    public List<Integer> getMembersByGroup(int groupId) {

        return null;
    }
}