package com.example.test.integration.web.i18n;

import com.example.web.i18n.I18NService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {WebappApplication.class, DevelopmentConfig.class})
@SpringBootTest
public class I18NServiceIntegrationTest {

    @Autowired
    I18NService i18NService;

    @Test
    public void shouldReturnAppropriateMessageFromMessageSource() {
        //given
        final String expected = "Bootstrap starter template";
        final String messageId = "index.main.callout";
        //when
        final String actual = i18NService.getMessage(messageId);
        //then
        Assert.assertEquals("Given message does not match", expected, actual);
    }
}
