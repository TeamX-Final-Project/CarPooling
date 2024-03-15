package org.example.carpooling.services;


import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.example.carpooling.Helpers;
import org.example.carpooling.models.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTests {

    @Mock
    SendGrid mockSendGrid;

    @Mock
    Personalization mockPersonalization;


    public void sendConformationEmail_should_succeed(){
        //Arrange
        User receiver = Helpers.createMockUser();
        long securityCode = 34343434;

    }
}
