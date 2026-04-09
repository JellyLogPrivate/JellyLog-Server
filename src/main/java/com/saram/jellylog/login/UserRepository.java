package com.saram.jellylog.login;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserAuthProviderId(String userAuthProviderId);

}