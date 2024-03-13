package org.example.carpooling.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private static final String SEND_MAIL_EXCEPTION_MESSAGE = "Failed to send mail.";
    public static final String SENDGRID_SEND_ENDPOINT = "mail/send";

    public static final String VERIFY_LINK = "%sapi/users/%d/verify?securityCode=%d";
    public static final String TEMPLATE_VAR_FIRST_NAME = "first_name";
    public static final String EMAIL_SUBJECT = "CarpoolX verify account";
    public static final String TEMPLATE_VAR_LINK = "user_verify_link";


    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.sendgrid.api.key}")
    private String sendgridApiKey;

    @Value("${mail.sendgrid.template.id}")
    private String templateId;

    @Value("${project.url}")
    private String projectUrl;


    public String sendConformationEmail(User receiver, long userSecurityCode) throws SendMailException {
        Mail mail = buildMailTemplate(receiver, userSecurityCode);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(SENDGRID_SEND_ENDPOINT);
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info(String.format("Sent mail response with status code: %d and body %s",
                    response.getStatusCode(), response.getBody()));
            return response.getBody();
        } catch (IOException ex) {
            logger.error("Sent mail error:{}", ex.getMessage());
            throw new SendMailException(SEND_MAIL_EXCEPTION_MESSAGE);
        }
    }

    private Mail buildMailTemplate(User receiver, long userSecurityCode) {
        Email from = new Email(mailFrom);
        Email to = new Email(receiver.getEmail());
        Mail mail = new Mail();
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        mail.setFrom(from);
        mail.setSubject(EMAIL_SUBJECT);
        personalization.addDynamicTemplateData(TEMPLATE_VAR_FIRST_NAME, receiver.getFirstName());
        personalization.addDynamicTemplateData
                (TEMPLATE_VAR_LINK, String.format(VERIFY_LINK, projectUrl, receiver.getUserId(), userSecurityCode));
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId);
        return mail;
    }
}