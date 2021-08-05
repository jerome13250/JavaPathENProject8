package tourGuide;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	
    	//we have a bug in external jar GpsUtils due to String.format("%.6f", new Object[] { Double.valueOf(longitude) }))
		//format uses Locale.getDefault() that create string Double with "," (Locale=FR) instead of "."
    	//For this reason i need to change the default Locale.
    	Locale.setDefault(Locale.US);
        SpringApplication.run(Application.class, args);
    }

}
