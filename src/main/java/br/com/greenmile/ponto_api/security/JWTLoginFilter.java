package br.com.greenmile.ponto_api.security;

import br.com.greenmile.ponto_api.common.utils.BCryptUtil;
import br.com.greenmile.ponto_api.domain.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {

        Usuario usuario = new ObjectMapper().readValue(req.getInputStream(), Usuario.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getUsername(),
                        usuario.getPassword()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain, Authentication auth) throws IOException, ServletException {

        TokenAuthenticationService.addAuthentication(res, auth.getName());
    }
}
