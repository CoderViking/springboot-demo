package com.viking.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.viking.mybatisplus.entity.Student;
import com.viking.mybatisplus.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMybatisPlusApplicationTests {

	@Autowired
	private StudentService service;

	@Test
	public void testListAll(){
		List<Student> list = service.list();
		System.out.println(list);
	}

	@Test
	public void contextLoads() {
		/**
		 * 根据ID获取学生信息
		 * @Param id 学生ID
		 * @Return Student 学生实体
		 */
		System.out.println(service.getStudentById(1L));

		/**
		 * 查询全部信息
		 * @Param id 学生ID
		 * @Return List<Student> 学生实体集合
		 */
		List<Student> list = service.list();
		System.out.println(list);

		/**
		 * 分页查询全部数据
		 * @Return IPage<Student> 分页数据
		 * *注:需要在spring容器中配置分页插件(本项目已在启动类中配置)
		 */
		IPage<Student> page = new Page<>();
		page.setCurrent(3); //当前页
		page.setSize(5); //每页条数
		page = service.page(page);
		List<Student> records = page.getRecords();
		System.out.println("当前页:"+page.getCurrent()+"\t总页数:"+page.getPages()+"\t总计:"+page.getTotal()+"\t每页条数"+page.getSize()+"\tlist:"+records);

		/**
		 * 根据指定字段查询学生信息集合
		 * @Return Collection<Student> 学生实体集合
		 */
		Map<String,Object> map = new HashMap<>();
		//kay是字段名 value是字段值
		map.put("age",20);
		Collection<Student> studentList = service.listByMap(map);
		System.out.println(studentList);

		/**
		 * 新增用户信息
		 */
		Student student = new Student();
		student.setName("小龙");
		student.setSkill("JAVA");
		student.setAge(18);
		student.setFraction(59L);
		student.setEvaluate("该学生是一个在改BUG的码农");
		service.save(student);

		/**
		 * 批量新增用户信息
		 */
		//创建对象
		Student sans = new Student();
		sans.setName("Sans");
		sans.setSkill("睡觉");
		sans.setAge(18);
		sans.setFraction(60L);
		sans.setEvaluate("Sans是一个爱睡觉,并且身材较矮骨骼巨大的骷髅小胖子");
		Student papyrus = new Student();
		papyrus.setName("papyrus");
		papyrus.setSkill("JAVA");
		papyrus.setAge(18);
		papyrus.setFraction(58L);
		papyrus.setEvaluate("Papyrus是一个讲话大声、个性张扬的骷髅，给人自信、有魅力的骷髅小瘦子");
		//批量保存
		List<Student> paramList =new ArrayList<>();
		paramList.add(sans);
		paramList.add(papyrus);
		service.saveBatch(paramList);

		/**
		 * 更新用户信息
		 */
		//根据实体中的ID去更新,其他字段如果值为null则不会更新该字段,参考yml配置文件
		Student userInfoEntity = new Student();
		userInfoEntity.setId(1L);
		userInfoEntity.setAge(19);
		service.updateById(userInfoEntity);

		/**
		 * 新增或者更新用户信息
		 */
		//传入的实体类userInfoEntity中ID为null就会新增(ID自增)
		//实体类ID值存在,如果数据库存在ID就会更新,如果不存在就会新增
		Student student1 = new Student();
		student1.setId(1L);
		student1.setAge(20);
		service.saveOrUpdate(student1);

		/**
		 * 根据ID删除用户信息
		 */
		service.removeById(13);

		/**
		 * 根据ID批量删除用户信息
		 */
		List<String> userIdlist = new ArrayList<>();
		userIdlist.add("12");
		userIdlist.add("13");
		service.removeByIds(userIdlist);

		/**
		 * 根据指定字段删除用户信息
		 */
		//kay是字段名 value是字段值
		Map<String,Object> map1 = new HashMap<>();
		map1.put("skill","删除");
		map1.put("fraction",10L);
		service.removeByMap(map1);
	}
	@Test
	public void testQueryWrapper(){
		//初始化返回类
		Map<String,Object> result = new HashMap<>();
		//查询年龄等于18岁的学生
		//等价SQL: SELECT id,name,age,skill,evaluate,fraction FROM user_info WHERE age = 18
		QueryWrapper<Student> queryWrapper1 = new QueryWrapper<>();
		queryWrapper1.lambda().eq(Student::getAge,18);
		List<Student> userInfoEntityList1 = service.list(queryWrapper1);
		result.put("studentAge18",userInfoEntityList1);
		//查询年龄大于5岁的学生且小于等于18岁的学生
		//等价SQL: SELECT id,name,age,skill,evaluate,fraction FROM user_info WHERE age > 5 AND age <= 18
		QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
		queryWrapper2.lambda().gt(Student::getAge,5);
		queryWrapper2.lambda().le(Student::getAge,18);
		List<Student> userInfoEntityList2 = service.list(queryWrapper2);
		result.put("studentAge5",userInfoEntityList2);
		//模糊查询技能字段带有"画"的数据,并按照年龄降序
		//等价SQL: SELECT id,name,age,skill,evaluate,fraction FROM user_info WHERE skill LIKE '%画%' ORDER BY age DESC
		QueryWrapper<Student> queryWrapper3 = new QueryWrapper<>();
		queryWrapper3.lambda().like(Student::getSkill,"画");
		queryWrapper3.lambda().orderByDesc(Student::getAge);
		List<Student> userInfoEntityList3 = service.list(queryWrapper3);
		result.put("studentAgeSkill",userInfoEntityList3);
		//模糊查询名字带有"小"或者年龄大于18的学生
		//等价SQL: SELECT id,name,age,skill,evaluate,fraction FROM user_info WHERE name LIKE '%小%' OR age > 18
		QueryWrapper<Student> queryWrapper4 = new QueryWrapper<>();
		queryWrapper4.lambda().like(Student::getName,"小");
		queryWrapper4.lambda().or().gt(Student::getAge,18);
		List<Student> userInfoEntityList4 = service.list(queryWrapper4);
		result.put("studentOr",userInfoEntityList4);
		//查询评价不为null的学生,并且分页
		//等价SQL: SELECT id,name,age,skill,evaluate,fraction FROM user_info WHERE evaluate IS NOT NULL LIMIT 0,5
		IPage<Student> page = new Page<>();
		page.setCurrent(1);
		page.setSize(5);
		QueryWrapper<Student> queryWrapper5 = new QueryWrapper<>();
		queryWrapper5.lambda().isNotNull(Student::getEvaluate);
		page = service.page(page,queryWrapper5);
		result.put("studentPage",page);
		System.out.println(result);
	}
}
