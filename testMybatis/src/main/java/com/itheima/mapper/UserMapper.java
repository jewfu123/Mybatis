package com.itheima.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.itheima.pojo.User;

public interface UserMapper {

	List<User> selectAll();
	
	@Select("select * from tb_user where id = #{id}")
	User selectById(int id);
	
	//@Select("select * from tb_user where username = #{username} and password={#password}")
	User select(String username, String password);
	
	//@Select("select * from tb_user where username=#{username}")
	//User select(String username);
	
}
