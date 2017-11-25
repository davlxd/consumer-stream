package co.nz.solnet.demo.kafka.streamer;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Properties;

@Service
public class CapitalizeStreamer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private KafkaStreams streams;

    public CapitalizeStreamer(@Autowired Properties kafkaProperties) {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream(kafkaProperties.getProperty("first.topic"));
        source.mapValues(v -> v.toUpperCase())
                .groupByKey()
                .aggregate(
                        () -> "",
                        (k, v, agg) -> agg + v)
                .toStream()
                .foreach((k, v) -> System.out.println(k + " : " + v));


        Topology topology = builder.build();
        logger.info("Topology: " + topology.describe());

        this.streams = new KafkaStreams(topology, kafkaProperties);
    }

    public KafkaStreams getStreams() {
        return streams;
    }

    @PreDestroy
    private void kafkaProducerClose() {
        streams.close();
        logger.info("KafkaStreams closed");
    }
}
