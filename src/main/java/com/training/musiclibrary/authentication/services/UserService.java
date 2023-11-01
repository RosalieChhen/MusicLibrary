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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public User findByUsername(String username) { return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);}

    public List<User> findAll() {
        return userRepository.findAllActiveUsers();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<User> searchUser(UserSearchFilterRequest searchFilter) {

        List<User> users = userRepository.findAllActiveUsers();

        if(!StringUtils.isNullOrBlank(searchFilter.getUsername())) {
            final String usernameFilter = searchFilter.getUsername().toLowerCase();
            users = users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(usernameFilter))
                    .collect(Collectors.toList());
        }
        return users;
    }

    public void deleteUserById(Long id) {

        User userToDelete = userRepository.findById(id).orElse(null);

        // Get current logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByUsername(authentication.getName()); // Front have to manage


        if(userToDelete == null) {
            throw new RuntimeException("User does not exist");
        }

        if(currentUser.isPresent() && userToDelete.equals(currentUser)) {
            throw new RuntimeException("Current User can not delete themselves");
        }

        userToDelete.setDeleted(true);
        userRepository.save(userToDelete);

    }
}
