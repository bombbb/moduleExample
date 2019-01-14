package wzp.moduleExample.util.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import wzp.moduleExample.util.mybatis.bo.User;

public interface UserMapper {
	@Select("SELECT * FROM moni_user WHERE user_id = #{userId,jdbcType=INTEGER}")
	@Results({ @Result(column = "user_descname", property = "userName") })
	User getUser(@Param("userId") int userId);

	@Select("SELECT * FROM moni_user order by user_id")
	@Results({ @Result(column = "user_descname", property = "userName") })
	List<User> getUsers();

	@Update("UPDATE moni_user SET user_descname=#{user.userName,jdbcType=VARCHAR} where user_id=#{user.user_id,jdbcType=INTEGER}")
	int updateUser(@Param("user") User user);
}
