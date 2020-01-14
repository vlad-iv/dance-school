package app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Base entity.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public abstract class EntityBase implements Serializable {
    private static final long serialVersionUID = -9112401529530955779L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idgen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version = 0;
}
