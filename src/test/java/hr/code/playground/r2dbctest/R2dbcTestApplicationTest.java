package hr.code.playground.r2dbctest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContainerizedReactivePostgresTestConfiguration.class)
public class R2dbcTestApplicationTest {

    @Autowired
    private DatabaseClient database;

    @Autowired
    private TestPersistableRepository repository;

    @Before
    public void setUp() throws Exception {
        List<String> statements = Arrays.asList(//
                "DROP TABLE IF EXISTS test_persistables;",
                "CREATE TABLE test_persistables (id BIGSERIAL PRIMARY KEY, test_value VARCHAR(100) NOT NULL);");

        statements.forEach(it -> database.execute()
                .sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .verifyComplete());
    }

    @Test
    public void testSave() {
        repository.saveAll(Arrays.asList(TestPersistable.builder()
                .testValue("Test Value")
                .build()))
                .as(StepVerifier::create) //
                .expectNextCount(1) //
                .verifyComplete();
        log.info("After saving entity.");
    }

}

