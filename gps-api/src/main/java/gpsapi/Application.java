package gpsapi;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import commons.*;

@SpringBootApplication
//Need to add the "commons" subproject to component scanning for the shared @ControllerAdvice
//otherwise as it is outside the spring boot main class it will not be scanned automatically:
@ComponentScan(basePackages={"gpsapi","commons"})
public class Application {

    public static void main(String[] args) {
    	
    	//we have a bug in external jar GpsUtils due to String.format("%.6f", new Object[] { Double.valueOf(longitude) }))
		//format uses Locale.getDefault() that creates string Double with "," (Locale=FR) instead of "."
    	//For this reason i need to change the default Locale.
    	Locale.setDefault(Locale.US);
        SpringApplication.run(Application.class, args);
    }

}
