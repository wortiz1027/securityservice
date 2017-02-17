package co.com.security.config.servlet;

import co.com.security.config.security.SecurityConfig;
import co.com.security.service.utils.Constantes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.annotation.Resource;

@EnableWebMvc
@Configuration
@ComponentScan({ "co.com.security.*" })
@Import({ SecurityConfig.class })
public class RestViewConfig {

    @Resource
    private Environment env;

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(env.getRequiredProperty(Constantes.BASE_MESSAGES_KEY));
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

}