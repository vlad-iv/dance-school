package app.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/**
 * Dance group.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Entity
@Table(name = "groups")
@GenericGenerator(name = "idgen", strategy = "native",
        parameters = @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "seq_groups"))
@Getter
@Setter
public class Group extends EntityBase {
    private static final long serialVersionUID = 4072156267332886154L;
    @Column(name = "name")
    String name;
    @Column(name = "style")
    String style;
    @Column(name = "level")
    String level;
    @Column(name = "start")
    LocalDate start;
    @Column(name = "finish")
    LocalDate finish;
    @Column(name = "active")
    private boolean active = true;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User teacher;

//    @OneToMany(mappedBy = "group_id")
//    List<Schedule> schedules;
//    @OneToMany(mappedBy = "group_id")
//    List<Lesson> lessons;

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", style='" + style + '\'' +
                ", level='" + level + '\'' +
                ", start=" + start +
                ", finish=" + finish +
                ", teacher=" + teacher +
//                ", schedules=" + schedules +
//                ", lessons=" + lessons +
                '}';
    }
}
