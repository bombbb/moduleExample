package wzp.moduleExample.util.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import wzp.moduleExample.util.mybatis.bo.User;

public interface UserRepository extends MongoRepository<User, String> {

}
