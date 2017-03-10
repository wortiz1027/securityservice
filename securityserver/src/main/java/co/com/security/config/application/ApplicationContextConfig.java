package co.com.security.config.application;

import co.com.security.service.utils.Constantes;
import co.com.security.utils.aspects.LogginAspect;
import net.bull.javamelody.MonitoredWithSpring;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.http.HttpSessionListener;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"co.com.security.*"})
@PropertySource(value = {"classpath:jdbc.properties",
                         "classpath:application.properties",
                         "classpath:i18n/messages.properties"})
@EnableAspectJAutoProxy
public class ApplicationContextConfig {

    @Autowired
    private Environment env;

    /*@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        return dataSource;
    }*/

    @Bean
    @MonitoredWithSpring
    public DataSource dataSource() throws NamingException {
        return (DataSource) new JndiTemplate().lookup(env.getProperty(Constantes.JNDI_DATASOURCE_KEY));
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws NamingException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String [] {"co.com.security.model.entities"});
        sessionFactory.setHibernateProperties(getHibernateProperties());

        return sessionFactory;
    }

    private Properties getHibernateProperties() {
        Properties prop = new Properties();

        prop.put("hibernate.dialect", env.getProperty("oracle.dialect"));
        prop.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        prop.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        prop.put("hibernate.query.substitutions", env.getProperty("hibernate.query.substitutions"));
        prop.put("hibernate.connection.release_mode", env.getProperty("hibernate.connection.release_mode"));
        prop.put("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));

        return prop;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txtManager = new HibernateTransactionManager();
        txtManager.setSessionFactory(sessionFactory);

        return txtManager;
    }

    @Bean
    public LogginAspect myAspect() {
        return new LogginAspect();
    }

    @Bean
    public HttpSessionListener javaMelodyListener(){
        return new net.bull.javamelody.SessionListener();
    }

    @Bean
    public Filter javaMelodyFilter(){
        return new net.bull.javamelody.MonitoringFilter();
    }

}