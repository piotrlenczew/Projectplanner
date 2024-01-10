package pw.pap.config;

// import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;


// @RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    // private final UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
            // .and()
            // .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
            .csrf().disable()
            // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // .and()
            // .authorizeHttpRequests(
            //     (requests) -> requests
            //         .requestMatchers("/login", "/register").permitAll()
            //         .anyRequest().authenticated()
            // )
        ;
        return http.build();
    }
}
