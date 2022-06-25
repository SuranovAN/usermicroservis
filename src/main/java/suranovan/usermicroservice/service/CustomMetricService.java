package suranovan.usermicroservice.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CustomMetricService {

    private final MeterRegistry meterRegistry;
    private Gauge gauge;

    @SneakyThrows
    @PostConstruct
    public void init() {
//        gauge = Gauge.builder("cachedUser_counter",
//                        meterRegistry, cacheManager ->
//                                cacheManager.getMeters().size())
//                .description("count cached user")
//                .register(meterRegistry);
    }
}
