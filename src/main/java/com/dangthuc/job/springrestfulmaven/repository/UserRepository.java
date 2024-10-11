package com.dangthuc.job.springrestfulmaven.repository;

import com.dangthuc.job.springrestfulmaven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String username);
}
