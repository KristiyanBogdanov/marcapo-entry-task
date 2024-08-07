//package com.example.springbootservice.config.jwt;
//
//import com.example.springbootservice.model.user.User;
//import com.example.springbootservice.repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@AllArgsConstructor
//public class UsernamePasswordAuthProvider implements AuthenticationProvider {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        System.out.println("dsjdknlkjsajsklajskaljskajskaljskaljskalsjkal");
//        final String username = authentication.getName();
//        final String password = authentication.getCredentials().toString();
//
//        final User user = userRepository.findByUsername(username)
//                .filter(entity -> passwordEncoder.matches(password, entity.getPassword()))
//                .orElseThrow(() -> new AuthenticationServiceException("Invalid username or password"));
//
//        return new UsernamePasswordAuthenticationToken(user.getId(), null, user.getPermissions());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
