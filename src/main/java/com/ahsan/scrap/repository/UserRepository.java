package com.ahsan.scrap.repository;

import com.ahsan.scrap.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDtls, Long> {
    public boolean existsByUsername(String username);
    public UserDtls findByUsername(String username);
    public UserDtls findByRole(String role);
}
