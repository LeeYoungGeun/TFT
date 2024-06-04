package org.zerock.b01.service;

import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MailDTO;

public interface MailService {
    void createMail(MailDTO mailDTO);
}
