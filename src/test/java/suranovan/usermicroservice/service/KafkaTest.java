package suranovan.usermicroservice.service;

import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import suranovan.usermicroservice.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Testcontainers
@TestPropertySource("/application-test.properties")
@Import(KafkaTest.KafkaTestContainerConfig.class)
@DirtiesContext
public class KafkaTest {

    @Container
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"))
                    .withEmbeddedZookeeper();
    @Autowired
    private ConsumeService consumer;
    @Autowired
    private ProducerService producer;

    @SneakyThrows
    @Test
    public void sendingToProducer_thenReceived() {
        User testUser = new User();
        testUser.setId(2);
        testUser.setName("testUser");
        testUser.setSurname("testSurname");
        testUser.setPatronymic("testPatronymic");
        testUser.setCreationDate("testCreationDate");

        String testTopic = "testTopic";
        producer.produce(testTopic, testUser);
        consumer.getLatch().await(10_000, TimeUnit.MILLISECONDS);

        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        assertThat(testUser.getName(), containsString(testUser.getName()));
    }

    @TestConfiguration
    public static class KafkaTestContainerConfig {
        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "testGroup");
            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            return props;
        }

        @Bean
        public ProducerFactory<String, User> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        ConcurrentKafkaListenerContainerFactory<String, User> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, User> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }

        @Bean
        public ConsumerFactory<String, User> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
        }

        @Bean
        public KafkaTemplate<String, User> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }

        @Bean
        public NewTopic topicTest() {
            return TopicBuilder.name("newUserTopic")
                    .partitions(1)
                    .replicas(1)
                    .build();
        }
    }
}
