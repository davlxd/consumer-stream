package co.nz.solnet.demo.kafka;

import co.nz.solnet.demo.kafka.streamer.CapitalizeStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class ConsumerStreamApplication implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Properties kafkaProperties;

    @Autowired
    private CapitalizeStreamer capitalizeStreamer;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerStreamApplication.class, args);
    }

    public void run(String... args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                latch.countDown();
            }
        });

        try {
            capitalizeStreamer.getStreams().start();
            latch.await();
        } catch (Throwable e) {
            logger.error("Error", e);
            System.exit(1);
        }

        System.exit(0);
    }
}
