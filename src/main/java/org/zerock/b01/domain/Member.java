package org.zerock.b01.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
@Builder
@Table(name="Member", uniqueConstraints = {
        @UniqueConstraint(
                name="mnick_unique",
                columnNames="mnick"
        )})
public class Member extends BaseEntity{

    @Id
    private String mid; //mid를 email 형식으로 변경
    private String mname;
    private String mnick;
    private String mpw;
    /*private String memail;*/
    private String mpno;
    private boolean del;
    private boolean social;

    //열거형 처리 roleSet
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    //중복되면안되는이유로 set사용
    private Set<MemberRole> roleSet = new HashSet<>();

    //이메일 변경
    /*public void changeEmail(String memail) {
        this.memail = memail;
    }*/

    //패스워드 변경 (세터 대신)
    public  void changePassword(String mpw) {
        this.mpw = mpw;
    }


    //삭제 여부 변경 (바로삭제 x)
    public void changeDel(boolean del) {
        this.del = del;
    }

    //addRoll 역할 추가
    public void addRole(MemberRole role) {
        this.roleSet.add(role);
    }

    //역할 삭제를 위해서
    public void clearRoles(){
        this.roleSet.clear();
    }

    //소셜 여부 변경을 위해서
    public void changeSocial(boolean social) {
        this.social = social;
    }

}
