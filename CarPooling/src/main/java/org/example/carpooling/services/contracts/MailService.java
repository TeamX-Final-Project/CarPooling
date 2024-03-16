package org.example.carpooling.services.contracts;

import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.models.User;

public interface MailService {

    void sendConformationEmail(User receiver, long userSecurityCode) throws SendMailException;
}
