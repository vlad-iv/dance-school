package app.model;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Group schedule.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Getter
@Setter
public class Schedule extends EntityBase {
    private static final long serialVersionUID = -1889207038768568303L;
    Group group;
    DayOfWeek dayOfWeek;
    LocalTime start;
    LocalTime finish;

    @Override
    public String toString() {
        return "Schedule{" +
                "group=" + group +
                ", dayOfWeek=" + dayOfWeek +
                ", start=" + start +
                ", finish=" + finish +
                '}';
    }
}
