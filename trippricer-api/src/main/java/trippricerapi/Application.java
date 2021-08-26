package trippricerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
//Need to add the "commons" subproject to component scanning for the shared @ControllerAdvice
//otherwise as it is outside the spring boot main class it will not be scanned automatically:
@ComponentScan(basePackages={"trippricerapi","commons"})
//@EnableSwagger2 needed to avoid some swagger unexpected bugs : 
//https://stackoverflow.com/questions/47425048/why-does-springfox-swagger2-ui-tell-me-unable-to-infer-base-url
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
    	
        SpringApplication.run(Application.class, args);
    }

}
