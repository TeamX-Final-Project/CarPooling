package org.example.carpooling.services;


import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.example.carpooling.Helpers;
import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.models.User;
import org.example.carpooling.services.contracts.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplImplTests {

    @Mock
    SendGrid mockSendGrid;

    @Mock
    Personalization mockPersonalization;

    @InjectMocks
    MailServiceImpl mailService;

    @BeforeEach
    public void setUp() {
 //       String mockedFiled = mock(mo9ckSendGrid)
        ReflectionTestUtils.setField(mailService, "sendgridApiKey", "gfdgdfgd345");
    }



    @Test
    public void sendConformationEmail_should_succeed() throws SendMailException {
        //Arrange
        User receiver = Helpers.createMockUser();
        long securityCode = 34343434;


        //Act
        mailService.sendConformationEmail(receiver, securityCode);

    }
}
