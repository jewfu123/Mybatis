## MyBatis
#### mybatis steps:
1. dependens:
	1) in pom.xml
	```xml
		<dependency>
		  <groupId>org.mybatis</groupId>
		  <artifactId>mybatis</artifactId>
		  <version>3.5.14</version>
		</dependency>
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>3.0.3</version>
		</dependency>
	```
	2) in application.properties
	```java
		spring.application.name=demo
		server.port=8081

		spring.datasource.url=jdbc:mysql:///localhost:3306/mybatis
		spring.datasource.username=root
		spring.datasource.password=
		spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

		mybatis.mapper-locations=classpath:mapper/*.xml
	```
	3) in Application.java
		```java
		String resource = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SqlSessionFactory sqlSessionFactory =
		  new SqlSessionFactoryBuilder().build(inputStream);
		
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		List<User> users = userMapper.selectAll();
		
		System.out.println(users.toString());
		
		sqlSession.close();
		```
	4) make mybatis-config.xml -> this is very important!!!!
	```xml
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE configuration
	  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
	  "https://mybatis.org/dtd/mybatis-3-config.dtd">
	  
	<configuration>
		<typeAliases>
			<package name="com.example.test202403.demo.pojo"/>
		</typeAliases>

	  <environments default="development">
	    <environment id="development">
	      <transactionManager type="JDBC"/>
	      <dataSource type="POOLED">
	        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
	        <property name="url" value="jdbc:mysql:///mybatis?useSSL=false"/>
	        <property name="username" value="root"/>
	        <property name="password" value=""/>
	      </dataSource>
	    </environment>
	  </environments>
	  <mappers>
	    <mapper resource="com/example/test202403/demo/mapper/UserMapper.xml"/>
	  </mappers>
	</configuration>
	```
2. make mapper xml and java class:
	1) in pojo/*.java -> mapper java class is Enties.
	2) in resources/com-abc-xxx-mapper/*.xml -> mapper file:
		```xml
		<mapper namespace="com.example.test202403.demo.mapper.UserMapper">

		    <insert id="insert">
		        INSERT INTO USER(USERNAME, SEX) VALUES(#{name}, #{sex})
		    </insert>
		    
		    <select id="selectAll" resultType="com.example.test202403.demo.pojo.User">
		    	select * from user;
		    </select>
		</mapper>
		```

#### alias fields alias name:
in Mapper.xml
```XML
<sql id="abc_column"> // sql split
	id, brand_name as brandName, company_name as companyName, ordered, description, status
</sql>

<resultMap id="brandResultMap" type="brand">  // result map（id->primary; result->normal field）column->field name;propery->class propery name
	<result column="brand_name" property="brandName"></result>
	<result column="company_name" property="companyName"></result>
</resultMap>

<select id="selectAll" resultType="brand">
	select
		<include refid="abc_column" />
	from tb_brand;
</select>

<select id="selectAll" resultType="brandResultMap">
	select
		*
	from tb_brand;
</select>
```

#### interfact pass var
in mapper.java
Brand selectBy(int id);

in mapper.xml
#{id}   // # can prevent SQL injection.

#### write sql in mapper xml.
```xml
&lt;

<![CDATA[                       // cdata area can insert plain text
	<
]]>

#{id};
```





































