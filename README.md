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

4. 主键返回Id的设定：
	```xml
	<insert id="addOrder" useGeneratedKeys="true" keyProperty="id">
		insert into ...
	</insert>
	```
	by getId() can get auto made id.

5. 动态修改字段：
	```xml
	<update id="update">
		update tb_brand
		<set>
			<if test="brandName!=null and brandName!="">
				brand_name = #{brandName},
			</if>
			<if test="companyName!=null and companyName!="">
				company_name = #{companyName},
			</if>
			...
		</set>
		where id=#{id};
	</update>
	```
6. 删除记录：
	```xml
	<deleted id="deleteById">
		delete from tb_brand where id=#{id}
	</deleted>

	批量删除，动态生成问号和id:
	```xml
	<delete id="deleteByIds">
		delete from tb_brand
		where id in (?,?,?..)
	</delete>

	-----

	<delete id="deleteByIds">
		delete from tb_brand
		where id in
		<foreach collection="ids" item="id" separator="," open="(" close=")">
			#{id}
		</foreach>
	</delete>
	```
	*this ids used error, if it equal java's array name,it will back error message: can not found ids.
	right is use "array" or set @Param -> change the map's default key name.

	@Param("username") String username,
	*this username must equal sql's vars name.


7. 使用注解开发，比配置文件xml方式更方便：
	```java
	@Select("select * from tb_user where id=#{id}")
	public User selectById(int id);

	@Update
	@Insert
	@Delete
	```
	*注解仅仅完成简单的操作，复杂的操作，还是需要用配置文件的方式。

8. Mybatis 代码生成器
	1) 添加依赖：
	```xml
	<dependency>
		<groupId>com.baomidou</groupId>
		<artifactId>mybatis-plus-generator</artifactId>
		<version>3.4.1</version>
	</dependency>
	```
	2) 添加模板依赖：
	```xml
	<dependency>
		<groupId>org.apache.velocity</groupId>
		<artifactId>velocity-engine-core</artifactId>
		<version>2.3</version>
	</dependency>
	```
	3) create Generator.java to execute:
	```java
	public class Generator {
		public static void main(String[] args) {
			AutoGenerator autoGenerator = new AutoGenerator();

			autoGenerator.set...
							setPackageInfo
							setGlobalConfig
							setDataSource
							setStrategy
							setTemplateEngine
							setTemplate
							setConfig
							setCfg
			DataSourceConfig dataSource = new DataSourceConfig();
			dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://localhost:3306/mybatisplus_db?serverTimezone=UTC");
			dataSource.setUsername("root");
			dataSource.setPassword("");
			autoGenerator.setDataSource(dataSource);

			//set global
			GlobalConfig globalConfig = new GlobalConfig();
			globalConfig.setOutDir(System.getProperty("user.dir")+"/mybatisplus_04_generator/src/main/java");
			globalConfig.setOpen(false);
			globalConfig.setAuthor("jewfu");
			globalConfig.setFileOverride(true);
			globalConfig.setMapperName("%sDao");
			globalConfig.setIdType(IdType.ASSIGN_ID);
			autoGenerator.setGlobalConfig(globalConfig);

			//set package info:
			PackageConfig packageInfo = new PackageConfig();
			packageInfo.setParent("com.itheima");
			packageInfo.setEntity("domain");
			packageInfo.setMapper("dao");
			autoGenerator.setPackageInfo(packageInfo);

			// policy set:
			StrategyConfig strategyConfig = new StrategyConfig();
			strategyConfig.setInclude("tbl_user");
			strategyConfig.setTablePrefix("tbl_");
			strategyConfig.setRestControllerStyle(true);
			strategyConfig.setVersionFieldName("version");
			strategyConfig.setLogicDeleteFieldName("deleted");
			strategyConfig.setEntityLombokModel("true");
			autoGenerator.setStrategy(strategyConfig);

			autoGenerator.execute();
		}
	}
	```

	*baseMapper封装了数据表的基本操作，baseService也是同样，封装了基本操作，其对应的 impl文件，也继承了相应的操作，所以，大大降低了代码量。
	```java
	@Service
	public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
		public void save() {
			// ????  -> 当书写其他逻辑时，这些代码就没有用了，全部交由自己控制，所以，常常会删除掉。
			dao.save();
		}
	}


	private IUserService userService;

	@Test
	void contextLoads() {
		userService.get.../update/remove/delete..
	}
	```
-------------------------------- Mybatis Plus -------------------------------
## Mybatis Plus
1. 引入依赖 Mybatis-Plus-Starter

2. 创建Dao文件
	in interface java file:
	```java
	@Mapper
	public interface UserDao extends BaseMapper<User> {

	}
	```
	
	in import Dao java file:
	```java
	@Autowired
	private UserDao userDao;

	userDao -> CRUD Lamder select; paginate show;

	```

## 引入Lombok坐标
	*lombok = getter() + setter()
	@lombok = @Getter + @Setter()
	@Data = 除了构造方法外的所有注解，很高效
	```XML
	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
	</dependency>

	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<version>1.18.12</version>
		<scope>provided</scope>
	</dependency>
	```

## 分页查询
	```java
	void testGetByPage() {
		IPage page = new Page(currentPage, PageSize); *IPage is interface file, Page is class file.
		userDao.selectPage(page, queryWrapper:null);
	}
	```
	*创建MybatisPlus拦截器：

	com.itheima.config.MpConfig.java
	```java
	@Configuration
	public class MpConfig {
		@Bean
		public MybatisPlusInterceptor myInterceptor() {
			MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
			mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
			return mpInterceptor;
		}
	}
	```
#### 配置文件中加入输出日志到控制台的配置：
	in application.yml
	```yaml
	mybatis-plus:
	  configuration:
	    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
	```

#### Mybatis与JPT JDBC连接池结合
	1. 引入依赖
	```pom
	<dependencies>
    <!-- MyBatis -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.9</version>
    </dependency>

    <!-- MyBatis-Spring (如果使用Spring框架) -->
    <dependency>
        <groupId>org.mybatis.spring</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>2.0.7</version>
    </dependency>

    <!-- HikariCP -->
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>5.0.1</version>
    </dependency>

    <!-- MySQL Connector (假设使用MySQL数据库) -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.27</version>
    </dependency>
	</dependencies>
	```

	2.配置连接池和MyBatis
	```xml
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE configuration
	    PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
	    "http://mybatis.org/dtd/mybatis-3-config.dtd">
	<configuration>
	    <environments default="development">
	        <environment id="development">
	            <transactionManager type="JDBC" />
	            <dataSource type="POOLED">
	                <property name="driver" value="com.mysql.cj.jdbc.Driver" />
	                <property name="url" value="jdbc:mysql://localhost:3306/mydatabase" />
	                <property name="username" value="root" />
	                <property name="password" value="password" />
	                <property name="poolMaximumActiveConnections" value="10" />
	            </dataSource>
	        </environment>
	    </environments>
	    <mappers>
	        <mapper resource="org/mybatis/example/BlogMapper.xml" />
	    </mappers>
	</configuration>
	```
	如果使用Spring框架，可以在application.properties或application.yml中配置HikariCP：
	```properties
	# application.properties
	spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
	spring.datasource.username=root
	spring.datasource.password=password
	spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
	spring.datasource.type=com.zaxxer.hikari.HikariDataSource
	spring.datasource.hikari.maximum-pool-size=10
	```
	3. 在Spring配置类中配置MyBatis：
	```java
	import org.apache.ibatis.session.SqlSessionFactory;
	import org.mybatis.spring.SqlSessionFactoryBean;
	import org.mybatis.spring.annotation.MapperScan;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

	import javax.sql.DataSource;

	@Configuration
	@MapperScan("com.example.mapper")
	public class MyBatisConfig {

	    @Bean
	    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
	        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
	        sessionFactory.setDataSource(dataSource);
	        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
	        return sessionFactory.getObject();
	    }
	}
	```

	4. 使用MyBatis进行数据库操作
	定义Mapper接口和对应的XML映射文件。例如，定义一个简单的UserMapper接口：
	```java
	public interface UserMapper {
	    User selectUser(int id);
	}
	```
	对应的XML映射文件UserMapper.xml：
	```xml
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE mapper
	    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
	    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	<mapper namespace="com.example.mapper.UserMapper">
	    <select id="selectUser" parameterType="int" resultType="com.example.domain.User">
	        SELECT * FROM users WHERE id = #{id}
	    </select>
	</mapper>
	```
	然后在服务类中使用Mapper：
	```java
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

	@Service
	public class UserService {
	    @Autowired
	    private UserMapper userMapper;

	    public User getUserById(int id) {
	        return userMapper.selectUser(id);
	    }
	}
	```

	通过这种方式，您可以将MyBatis与HikariCP等JDBC连接池结合使用，以实现高效的数据库操作。





























