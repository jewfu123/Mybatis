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

#### interface pass var
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

#### Mybatis Search by SQL.
1. muti search.
	in interface java.
	```JAVA
	List<Brand> selectByCondition(@Param("status")int status, @Param("companyName")String companyName, @Param("brandName")String brandName);

	List<Brand> selectByCondition(Brand brand);

	List<Brand> selectByCondition(Map map);
	```
	in Mapper XML file
	```xml
	<select id="selectByCondition" resultMap="brandResultMap">
		select * from tb_brand where
			status=#{status}
			and company_name like #{companyName}
			and brand_name like #{brandName}
	</select>
	```

2. SQL语句设置多个参数有几种方式？
	1）散装参数：
		需要使用 @Param("sql中的参数占位符名称")

	2）实体类封装参数：
		*只需要保证SQL中的参数名和实体类属性名对应上，即可设置成功

	3）map集合：
		*只需要保证SQL中的参数名和map集合的键的名称对应上，即可设置成功

3. 动态SQL
	>> 多条件动态查询：
	```xml
	<select id="selectByCondition" resultMap="brandResultMap">
		select * from tb_brand where
			status=#{status}
			and company_name like #{companyName}
			and brand_name like #{brandName}
	</select>
	```
	> if
	```xml
		<select id="selectByCondition" resultMap="brandResultMap">
			select * from tb_brand //where 1=1
			<where>
				<if test="status != null">
					status=#{status}
				</if>
				<if test="company_name != null and company_name != ''">
					and company_name like #{companyName}
				</if>
				<if test="brand_name != null and brand_name != ''">
					and brand_name like #{brandName}
				</if>
			</where>
		</select>
		```
		in java file
		```java
		Map map = new HashMap();
		map.put('status', status);
		map.put('companyName', companyName);
		map.put('brandName', brandName);
		```
	> chose(when, otherwise)
	> trim(where, set)
	> foreach

	>> 单条件动态查询：
	```xml
	<select id="selectByConditionSingle" resultMap="brandResultMap">
		select * from tb_brand //where
		<where>
			<choose>
				<when test="status!=null">
					status=#{status}
				</when>
				<when test="companyName!=null and companyName!=''">
					company_name like #{companyName}
				</when>
				<when test="brandName!=null and brandName!=''">
					brand_name like #{brandName}
				</when>
				<otherwise>
					brand_name like #{brandName}
				</otherwise>
			</choose>
		</where>
	</select>
	```
	```java
	List<Brand> selectByConditionSingle(Brand brand);
	```


























