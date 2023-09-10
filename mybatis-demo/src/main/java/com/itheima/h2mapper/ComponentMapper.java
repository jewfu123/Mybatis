package com.itheima.h2mapper;

import java.util.List;

import com.itheima.h2pojo.Component;
import com.itheima.pojo.Brand;

public interface ComponentMapper {
	
	List<Component> selectAll();
	
	/**
     * 添加
     */
    void add(Component component);
    
    /**
     * 查看详情：根据Id查询
     */
    Component selectById(String id);

}
