package org.example.carpooling.services;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.example.carpooling.Helpers;
import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplImplTests {

    public static final String MAIL_FROM = "mail@from.com";
    public static final String TEST_TEMPLATE_ID = "testTemplateId";
    @Mock
    SendGrid mockSendGrid;

    @InjectMocks
    MailServiceImpl mailService;

    @Captor
    ArgumentCaptor<Request> captor;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(mailService, "mailFrom", MAIL_FROM);
        ReflectionTestUtils.setField(mailService, "sendgridApiKey", "testApiKey");
        ReflectionTestUtils.setField(mailService, "templateId", TEST_TEMPLATE_ID);
        ReflectionTestUtils.setField(mailService, "projectUrl", "http://url");
    }


    @Test
    public void sendConformationEmail_should_succeed() throws SendMailException, IOException {
        //Arrange
        User receiver = Helpers.createMockUser();
        long securityCode = 34343434;
        Response response = new Response();

        Mockito.when(mockSendGrid.api(captor.capture())).thenReturn(response);

        //Act
        mailService.sendConformationEmail(receiver, securityCode);

        //Assert
        Mockito.verify(mockSendGrid, Mockito.times(1)).api(Mockito.any());
        Request actualRequest = captor.getValue();
        Assertions.assertEquals(Method.POST, actualRequest.getMethod());
        Assertions.assertTrue(actualRequest.getBody().contains(MAIL_FROM));
        Assertions.assertTrue(actualRequest.getBody().contains(TEST_TEMPLATE_ID));
    }
}
