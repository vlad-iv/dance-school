package app.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User model.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Entity
@Table(name = "users")
@GenericGenerator(name = "idgen", strategy = "native",
        parameters = @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "seq_users"))
@Getter
@Setter
public class User extends EntityBase implements UserDetails {
    private static final GrantedAuthority DEFAULT_AUTHORITY = new SimpleGrantedAuthority("ROLE_USER");
    private static final Set<GrantedAuthority> DEFAULT_AUTHORITIES = Collections.singleton(DEFAULT_AUTHORITY);
    private static final long serialVersionUID = -6493907526963659447L;
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "role")
    @Convert(converter = UserRoleConverter.class)
    private Set<UserRole> roles;

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                "} ";
    }
}
