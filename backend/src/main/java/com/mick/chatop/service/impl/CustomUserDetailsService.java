package com.mick.chatop.service.impl;

import com.mick.chatop.entity.UserEntity;
import com.mick.chatop.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation personnalisée de {@link UserDetailsService} utilisée par Spring Security
 * pour charger les détails d'un utilisateur à partir de la base de données via son email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructeur avec injection du repository utilisateur.
     *
     * @param userRepository Le repository permettant de récupérer les utilisateurs par email.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur depuis la base de données en utilisant son email.
     * Utilisé par Spring Security lors de l'authentification.
     *
     * @param email L'adresse email de l'utilisateur à authentifier.
     * @return Un objet {@link UserDetails} utilisé par Spring Security.
     * @throws UsernameNotFoundException Si aucun utilisateur n'est trouvé avec cet email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + email));

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("USER")))
                .build();
    }
}