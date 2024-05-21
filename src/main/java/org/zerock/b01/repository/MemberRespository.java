package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Member;

public interface MemberRespository extends JpaRepository<Member, String> {

}
