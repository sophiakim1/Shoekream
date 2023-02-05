package com.supreme.admin.repository;

import com.supreme.admin.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndMemberPw(String email, String memberPw);
    Member findByIdx(Long idx);
    Optional<Member> deleteByIdx(Long idx);
    Member getReferenceByIdx(Long idx);

    long countBy();
}