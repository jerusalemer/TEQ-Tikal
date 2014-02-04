package spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Art on 2/2/14.
 */
@Configuration
@ComponentScan({"dao", "service"})
public class SpringConfig {
}
