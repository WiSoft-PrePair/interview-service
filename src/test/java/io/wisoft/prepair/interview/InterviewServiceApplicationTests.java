package io.wisoft.prepair.interview;

import io.wisoft.prepair.interview.support.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class InterviewServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
