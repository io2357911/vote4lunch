package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.TestUtil;
import com.github.io2357911.vote4lunch.util.exception.ErrorInfo;
import com.github.io2357911.vote4lunch.util.exception.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring.xml",
        "classpath:spring/spring-mvc.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@WebAppConfiguration
public class AbstractRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    public ResultMatcher errorInfo(String url, ErrorType type, String... details) {
        return result -> {
            ErrorInfo error = TestUtil.readFromJsonMvcResult(result, ErrorInfo.class);
            assertEquals("http://localhost" + url, error.getUrl());
            assertEquals(type, error.getType());
            if (details.length != 0) {
                assertArrayEquals(details, error.getDetails());
            }
        };
    }
}
