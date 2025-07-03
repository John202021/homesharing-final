package gr.hua.dit.HomeSharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync; 
// import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableAsync
@SpringBootApplication
public class HomeSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeSharingApplication.class, args);
	}

}