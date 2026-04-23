package com.example.mybatis;

import com.example.mybatis.entity.BizCustomer;
import com.example.mybatis.entity.BizOrder;
import com.example.mybatis.entity.SysUser;
import com.example.mybatis.entity.Teacher;
import com.example.mybatis.mapper.BizCustomerMapper;
import com.example.mybatis.mapper.BizOrderMapper;
import com.example.mybatis.mapper.SysUserMapper;
import com.example.mybatis.mapper.TeacherMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@SpringBootTest
class MybatisApplicationTests {

    // private SqlSessionFactory getSqlSessionFactory() throws Exception {
    //     // 以输入流的形式加载 MyBatis 配置文件
    //     InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    //     // 创建 SqlSessionFactoryBuilder 对象，用于创建工厂对象 SqlSessionFactory
    //     SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    //     // 基于读取的 Mybatis 配置文件输入流创建 SqlSessionFactory 对象
    //     return builder.build(inputStream);
    // }
    //
    // @Test
    // public void testSelectById() throws Exception {
    //     SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
    //     // 使用 SqlSessionFactory 对象开启一个会话
    //     SqlSession sqlSession = sqlSessionFactory.openSession();
    //     try {
    //         // 根据 UserMapper 接口的 Class 对象获取 Mapper 接口类型的对象（动态代理技术）
    //         SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
    //         // 调用代理类方法既可以触发对应的 SQL 语句
    //         User user = userMapper.selectUserById(1L);
    //         System.out.println(user == null ? "null" : user);
    //     } finally {
    //         // 提交事务（DQL 不需要，其他需要）
    //         sqlSession.commit();
    //         // 关闭会话
    //         sqlSession.close();
    //     }
    // }

    private SqlSession sqlSession;

    // JUnit 会在每一个 @Test 方法前执行 @BeforeEach 方法
    @BeforeEach
    public void init() throws IOException {
        sqlSession = new SqlSessionFactoryBuilder()
                .build(Resources.getResourceAsStream("mybatis-config.xml"))
                .openSession();
    }

    // JUnit 会在每一个 @Test 方法后执行 @AfterEach 方法
    @AfterEach
    public void clear() {
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testSelectUserById() {
        // 根据 UserMapper 接口的 Class 对象获取 Mapper 接口类型的对象（动态代理技术）
        SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
        // 调用代理类方法既可以触发对应的 SQL 语句
        SysUser user = userMapper.selectUserById(1L);
        System.out.println(user == null ? "null" : user);
        // log.info("user = {}", user);
    }

    @Test
    public void testInsertUser() {
        SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
        SysUser user = new SysUser();
        user.setUsername("joi");
        user.setPassword("123456");
        user.setNickname("Joi");
        user.setEmail("joi@example.com");
        user.setPhone("13800000012");
        int userCount = userMapper.insertUser(user);
        System.out.println("userCount = " + userCount);
        System.out.println("userId = " + user.getId());
    }

    @Test
    public void testSelectUserList() {
        SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
        for (SysUser user : userMapper.selectUserList()) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelectUserCount() {
        SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
        System.out.println("count = " + userMapper.selectUserCount());
    }

    @Test
    public void testSelectUserMapById() {
        SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
        Map<String, Object> map = userMapper.selectUserMapById(1L);
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "=" + value);
        }
    }

    @Test
    public void testSelectUserByCondition() {
        SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
        Map<String, Object> map = new HashMap<>();
        map.put("phonePrefix", "138");
        map.put("emailKeyword", "example");
        List<SysUser> result = userMapper.selectUserByCondition(map);
        for (SysUser sysUser : result) {
            System.out.println(sysUser);
        }
    }

    @Test
    public void testSelectLatestLoginUserInfo() {
        SysUserMapper userMapper = sqlSession.getMapper(SysUserMapper.class);
        Map<String, Object> map = userMapper.selectLatestLoginUserInfo();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "=" + value);
        }
    }

    @Test
    public void testSelectOrderWithCustomer() {
        BizOrderMapper orderMapper = sqlSession.getMapper(BizOrderMapper.class);
        BizOrder order = orderMapper.selectOrderWithCustomer(1L);
        System.out.println(order);
    }

    @Test
    public void testSelectCustomerWithOrderList() {
        BizCustomerMapper customerMapper = sqlSession.getMapper(BizCustomerMapper.class);
        BizCustomer customer = customerMapper.selectCustomerWithOrderList(1L);
        System.out.println("customerId = " + customer.getCustomerId());
        System.out.println("customerName = " + customer.getCustomerName());
        for (BizOrder order : customer.getOrders()) {
            System.out.println("orderId = " + order);
        }
    }

    @Test
    public void testSelectTeacherList() {
        TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
        List<Teacher> teacherList = teacherMapper.selectAllTeacherList();
        for (Teacher teacher : teacherList) {
            System.out.println(teacher);
        }
    }

    @Test
    public void testSelectOrderById() {
        BizOrderMapper orderMapper = sqlSession.getMapper(BizOrderMapper.class);
        BizOrder order = orderMapper.selectOrderById(1L);
        System.out.println("order = " + order);
    }

    @Test
    public void testSelectCustomerList() {
        BizCustomerMapper customerMapper = sqlSession.getMapper(BizCustomerMapper.class);
        List<BizCustomer> customerList = customerMapper.selectCustomerList();
        for (BizCustomer customer : customerList) {
            System.out.println("customer = " + customer);
        }
    }

    @Test
    public void testLazyLoading() throws Exception {
        BizCustomerMapper customerMapper = sqlSession.getMapper(BizCustomerMapper.class);
        List<BizCustomer> customerList = customerMapper.selectCustomerList();

        // 输出主查询的属性
        System.out.println("customers.get(0).getCustomerName() = " + customerList.getFirst().getCustomerName());

        // 休眠
        Thread.sleep(5000);

        // 输出二次分步查询反馈的属性
        System.out.println("customers = " + customerList.getFirst().getOrders());
    }

    @Test
    public void testSelectTeacherPage() {
        TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);

        // 将当前页码和每页的行数告诉插件
        PageHelper.startPage(1, 2);

        // 在查询时插件和查询语句不需要建立任何联系，在 MyBatis 配置中配置的插件会自动拦截
        // 查询 Customer 对象同时将关联的 Order 集合查询出来放到 List 集合中

        List<Teacher> teacherList = teacherMapper.selectAllTeacherList();
        System.out.println("teacherList = " + teacherList);

        // 将查询到的结果封装到 PageInfo 中，获取更详细的信息
        PageInfo<Teacher> teacherPageInfo = new PageInfo<>(teacherList);
        System.out.println("teacherPageInfo = " + teacherPageInfo);

        // 获取总页数
        System.out.println("teacherPageInfo.getPages() = " + teacherPageInfo.getPages());
        // 获取总记录数
        System.out.println("teacherPageInfo.getTotal() = " + teacherPageInfo.getTotal());
        // 获取当前页码
        System.out.println("teacherPageInfo.getPageNum() = " + teacherPageInfo.getPageNum());
        // 获取每页显示记录数
        System.out.println("teacherPageInfo.getPageSize() = " + teacherPageInfo.getPageSize());
        // 获取查询页的数据集合
        List<Teacher> teacherPageInfoList = teacherPageInfo.getList();
        System.out.println("teacherPageInfo.getList() = " + teacherPageInfoList);
        teacherPageInfoList.forEach(System.out::println);
    }
}
