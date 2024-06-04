package org.zerock.b01.dto;


import lombok.Data;

@Data
public class MemberJoinDTO {

    private String mid;
    private String mname;
    private String mnick;
    private String mpw;
    /*private String memail;*/
    private String mpno;
    private boolean del;

}
