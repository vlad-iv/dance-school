package app.service;

import app.model.Group;
import app.repository.GroupRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * Group service.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Service
@Transactional
public class GroupService {
    final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public Group getGroup(Long groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }
}
