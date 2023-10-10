package com.training.musiclibrary.authentication.services;

import com.training.musiclibrary.authentication.DTO.AuthenticationRequest;
import com.training.musiclibrary.authentication.DTO.AuthenticationResponse;
import com.training.musiclibrary.authentication.DTO.RegisterRequest;
import com.training.musiclibrary.authentication.DTO.UserSearchFilterRequest;
import com.training.musiclibrary.authentication.models.User;
import com.training.musiclibrary.authentication.models.enums.Role;
import com.training.musiclibrary.authentication.repositories.UserRepository;
import com.training.musiclibrary.utils.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<User> searchUser(UserSearchFilterRequest searchFilter) {
        List<User> users = userRepository.findAll();
        if(!StringUtils.isNullOrBlank(searchFilter.getUsername())) {
            final String usernameFilter = searchFilter.getUsername().toLowerCase();
            users = users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(usernameFilter))
                    .collect(Collectors.toList());
        }
        return users;
    }

    public void deleteUserById(Long id) {

        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            user.get().setDeleted(true);
        } else {
            throw new RuntimeException("User does not exist");
        }
    }
}
