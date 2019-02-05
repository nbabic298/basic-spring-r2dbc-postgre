package hr.code.playground.r2dbctest;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TestPersistableRepository extends ReactiveCrudRepository<TestPersistable, Long> {
}
