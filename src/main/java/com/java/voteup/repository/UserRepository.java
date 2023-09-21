package com.java.voteup.repository;

import com.java.voteup.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("update User u set u.validated = ?1 where u.userId = ?2")
    int updateValidatedByUserIdEquals(Boolean validated, Integer userId);
    User findByUserIdEquals(Integer userId);
    @Query("select u from User u where u.email like ?1")
    User findByEmailLike(String email);
}
