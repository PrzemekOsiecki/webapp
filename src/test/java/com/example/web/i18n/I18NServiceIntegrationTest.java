package com.example.web.i18n;

import com.example.WebappApplication;
import com.example.config.I18nConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebappApplication.class)
public class I18NServiceIntegrationTest {
    @Autowired
    I18NService i18NService;

    @Test
    public void shouldReturnAppropriateMessageFromMessageSource() {
        //given
        final String expected = "Bootstrap Strona Startowa";
        final String messageId = "index.main.callout";
        //when
        final String actual = i18NService.getMessage(messageId);
        //then
        Assert.assertEquals("Given message does not match", expected, actual);
    }
}
