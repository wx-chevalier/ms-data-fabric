package com.zhuxun.dmc.user;

import com.zhuxun.dmc.user.config.ApplicationConstants;
import com.zhuxun.dmc.user.config.DefaultProfileUtil;
import com.zhuxun.dmc.user.config.properties.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@ComponentScan
@SpringBootApplication
@Slf4j
public class Application {

  private final Environment env;

  private final ApplicationProperties applicationProperties;

  @Autowired
  public Application(Environment env,
                     ApplicationProperties applicationProperties) {
    this.env = env;
    this.applicationProperties = applicationProperties;
  }

  /**
   * Main method, used to run the application.
   *
   * @param args the command line arguments
   * @throws UnknownHostException if the local host name could not be resolved into an address
   */
  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplication(Application.class);
    DefaultProfileUtil.addDefaultProfile(app);
    Environment env = app.run(args).getEnvironment();
    String protocol = "http";
    if (!StringUtils.isEmpty(env.getProperty("server.ssl.key-store"))) {
      protocol = "https";
    }
    log.info("\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}\n\t" +
            "External: \t{}://{}:{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        protocol,
        env.getProperty("server.port"),
        protocol,
        InetAddress.getLocalHost().getHostAddress(),
        env.getProperty("server.port"),
        env.getActiveProfiles());
  }

  /**
   * Spring profile 可以使用启动参数设定：
   * <code>
   * --spring.profiles.active=your-active-profile
   * </code>
   */
  @PostConstruct
  public void initApplication() {
    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    if (activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT) &&
        activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_PRODUCTION)) {
      log.error("You have misconfigured your application! It should not run " +
          "with both the 'dev' and 'prod' profiles at the same time.");
    }
  }

  @Bean
  @SuppressWarnings("unchecked")
  public FilterRegistrationBean corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", applicationProperties.getCors());
    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }

}
