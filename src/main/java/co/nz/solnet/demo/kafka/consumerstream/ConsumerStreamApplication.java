package co.nz.solnet.demo.kafka.consumerstream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerStreamApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ConsumerStreamApplication.class, args);
    }

    public void run(String... args) throws Exception {
        System.out.println("Hey");
    }
}
