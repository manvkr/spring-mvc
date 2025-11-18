package com.example.mvc.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WebMvcConfigTest {

    private WebMvcConfig webMvcConfig;

    @BeforeEach
    void setUp() {
        webMvcConfig = new WebMvcConfig();
    }

    @Test
    void testViewResolverPrefixSuffix() throws Exception {
        InternalResourceViewResolver resolver = (InternalResourceViewResolver)
                new WebMvcConfig().jspViewResolver();

        Method getPrefix = resolver.getClass().getSuperclass().getDeclaredMethod("getPrefix");
        getPrefix.setAccessible(true);
        String prefix = (String) getPrefix.invoke(resolver);

        Method getSuffix = resolver.getClass().getSuperclass().getDeclaredMethod("getSuffix");
        getSuffix.setAccessible(true);
        String suffix = (String) getSuffix.invoke(resolver);

        assertEquals("/WEB-INF/jsp/", prefix);
        assertEquals(".jsp", suffix);
    }

    @Test
    void testJacksonMessageConverterBean() {
        assertNotNull(webMvcConfig.jackson2HttpMessageConverter());
    }

    @Test
    void testConfigureMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        webMvcConfig.configureMessageConverters(converters);
        assertFalse(converters.isEmpty());
        assertTrue(converters.stream().anyMatch(c -> c instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter));
    }

    @Test
    void testAddResourceHandlers() {
        WebMvcConfig config = new WebMvcConfig();

        StaticApplicationContext applicationContext = new StaticApplicationContext();
        ResourceHandlerRegistry registry = new ResourceHandlerRegistry(applicationContext, null);

        assertDoesNotThrow(() -> config.addResourceHandlers(registry));
    }
}

