package fr.upload.uploadfiles.security;

import fr.upload.uploadfiles.security.jwt.AuthEntryPointJwt;
import fr.upload.uploadfiles.security.jwt.AuthTokenFilter;
import fr.upload.uploadfiles.security.jwt.JwtUtils;
import fr.upload.uploadfiles.security.service.CustomOAuth2UserService;
import fr.upload.uploadfiles.services.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig{

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //TODO get google auth info and get token from google

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http.headers().and().cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeHttpRequests()
                .requestMatchers("/api/file/myFiles").authenticated()
                .requestMatchers("/api/file/delete/**").authenticated()
                .requestMatchers("/api/profile/**").authenticated()
                .anyRequest().permitAll()
               .and()
               .oauth2Login()
               .loginPage("/login")
               .defaultSuccessUrl("/upload", true)
               .failureUrl("/login")
               .userInfoEndpoint()
               .userService(oauthUserService)
               .and()
               .successHandler((request, response, authentication) -> {

                   DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
                   String email = oauthUser.getAttribute("email");
                   String pseudo = oauthUser.getAttribute("name");
                   userService.processOAuthPostLogin(pseudo, email);

                   String jwt = jwtUtils.generateJwtTokenProvider(authentication);

                   Cookie cookie = new Cookie("user-token", jwt);
                   cookie.setMaxAge(24 * 60 * 60);
                   cookie.setPath("/");

                   response.addCookie(cookie);
                   response.sendRedirect("/upload");
               });

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
