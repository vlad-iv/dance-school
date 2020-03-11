package app.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Convert list of UserRole to string.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
public class UserRoleConverter implements AttributeConverter<Set<UserRole>, String> {
    @Override
    public String convertToDatabaseColumn(Set<UserRole> userRoles) {
        return StringUtils.join(userRoles, ',');
    }

    @Override
    public Set<UserRole> convertToEntityAttribute(String s) {
        String[] split = StringUtils.split(s, ',');
        if (split == null) {
            return null;
        }
        if (split.length == 0) {
            return Collections.emptySet();
        }
        return Stream.of(s.split(",")).map(UserRole::valueOf).collect(Collectors.toSet());
    }
}
