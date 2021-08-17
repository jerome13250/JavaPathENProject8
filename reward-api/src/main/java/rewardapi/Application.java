package rewardapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//Need to add the "commons" subproject to component scanning for the shared @ControllerAdvice
//otherwise as it is outside the spring boot main class it will not be scanned automatically:
@ComponentScan(basePackages={"rewardapi","commons"})
public class Application {

    public static void main(String[] args) {
    	
        SpringApplication.run(Application.class, args);
    }

}
