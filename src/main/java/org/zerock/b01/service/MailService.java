package org.zerock.b01.service;

import org.zerock.b01.dto.MailDTO;

public interface MailService {
    String sendSimpleMessage(String email) throws Exception;
}
