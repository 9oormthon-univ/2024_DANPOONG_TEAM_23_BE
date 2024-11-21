package com.doctorgc.doctorgrandchild.repository;

import com.doctorgc.doctorgrandchild.entity.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
