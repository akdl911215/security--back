package com.example.security.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name); //이거는 옵셔널 안 걸거에요.


}
