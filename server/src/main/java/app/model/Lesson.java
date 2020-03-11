package app.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Dance lesson.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Getter
@Setter
public class Lesson extends EntityBase {
    private static final long serialVersionUID = -5699489134197892950L;
    Schedule schedule;
    LocalDateTime date;
    String status;

    User teacher;
    List<User> students;
}
