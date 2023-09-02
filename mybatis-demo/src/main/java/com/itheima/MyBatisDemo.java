package com.itheima;


import com.itheima.mapper.BrandMapper;
import com.itheima.mapper.TestMapper;
import com.itheima.pojo.Brand;
import com.itheima.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Mybatis 快速入门代码
 */
public class MyBatisDemo {

    public static void main(String[] args) throws IOException {

        //1. 加载mybatis的核心配置文件，获取 SqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2. 获取SqlSession对象，用它来执行sql
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //3. 执行sql
        //List<User> users = sqlSession.selectList("test.selectAll");
        BrandMapper brandMapper = sqlSession.getMapper(BrandMapper.class);
        TestMapper tt = sqlSession.getMapper(TestMapper.class);
        List<Brand> tts = tt.selectAll();
        List<Brand> brands = brandMapper.selectAll();
        System.out.println(brands);
        System.out.println(tts);
        //4. 释放资源
        sqlSession.close();

    }
}
