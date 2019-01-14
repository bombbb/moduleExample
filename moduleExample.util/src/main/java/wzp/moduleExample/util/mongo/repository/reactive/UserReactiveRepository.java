package wzp.moduleExample.util.mongo.repository.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import wzp.moduleExample.util.mybatis.bo.User;

public interface UserReactiveRepository extends ReactiveMongoRepository<User, String> {
}
