package com.doctorgc.doctorgrandchild.repository;

import com.doctorgc.doctorgrandchild.entity.member.Member;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.isActive = false WHERE m.email = :email")
    void deactivateMember(String email);

    @Query("SELECT m.kakaoAccessToken FROM Member m WHERE m.email = :email")
    String findAccessTokenByEmail(@Param("email") String email);

}
