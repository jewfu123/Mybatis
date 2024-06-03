package com.example.test202403.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.test202403.demo.pojo.User;

public interface UserMapper {
	int insert(@Param("username") String username, @Param("sex") Integer sex);
	
	List<User> selectAll();
}
