package org.zerock.b01.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Member extends BaseEntity{

    @Id
    private String mid;
    private String mname;
    private String mnick;
    private String mpw;
    private String memail;
    private String mpno;
    private boolean del;

    //패스워드 변경 (세터 대신)
    public  void changePassword(String mpw) {
        this.mpw = mpw;
    }

}
