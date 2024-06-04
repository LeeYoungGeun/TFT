package org.zerock.b01.service;

import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberJoinDTO;

public interface MemberService {

    static class MidExistException extends Exception {

        public MidExistException() {}

        public MidExistException(String message) {
            super(message);
        }
    }

    static class MnickExistException extends Exception {

        public MnickExistException() {}

        public MnickExistException(String message) {
            super(message);
        }
    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException,MnickExistException;
    void modify(MemberJoinDTO memberJoinDTO);
    void remove(String mid);
    Member getDetail(String mid);

}
