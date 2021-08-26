package gpsapi;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
//Need to add the "commons" subproject to component scanning for the shared @ControllerAdvice
//otherwise as it is outside the spring boot main class it will not be scanned automatically:
@ComponentScan(basePackages={"gpsapi","commons"})
//@EnableSwagger2 needed to avoid some swagger unexpected bugs : 
//https://stackoverflow.com/questions/47425048/why-does-springfox-swagger2-ui-tell-me-unable-to-infer-base-url
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
    	
    	//we have a bug in external jar GpsUtils due to String.format("%.6f", new Object[] { Double.valueOf(longitude) }))
		//format uses Locale.getDefault() that creates string Double with "," (Locale=FR) instead of "."
    	//For this reason i need to change the default Locale.
    	Locale.setDefault(Locale.US);
        SpringApplication.run(Application.class, args);
    }

}
