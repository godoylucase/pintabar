package com.pintabar.persistence.repositories;

import com.pintabar.entities.user.User;
import com.pintabar.repositories.GenericJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by lucasgodoy on 10/03/17.
 */
@Transactional
public interface UserRepository extends GenericJpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
}
