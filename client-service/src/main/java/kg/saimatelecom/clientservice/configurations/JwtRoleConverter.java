package kg.saimatelecom.clientservice.configurations;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        @SuppressWarnings("unchecked")
        List<String> roles = (ArrayList<String>) source.getClaims().get("roles");
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
