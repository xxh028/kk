package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.BjMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Bj;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentExample;
import com.xiaoshu.entity.StudentExample.Criteria;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.User;

@Service
public class StudentService {

	@Autowired
	UserMapper userMapper;
@Autowired
StudentMapper studentMapper;
@Autowired
BjMapper bjMapper;
	/*// 查询所有
	public List<User> findUser(User t) throws Exception {
		return userMapper.select(t);
	};

	// 数量
	public int countUser(User t) throws Exception {
		return userMapper.selectCount(t);
	};

	// 通过ID查询
	public User findOneUser(Integer id) throws Exception {
		return userMapper.selectByPrimaryKey(id);
	};

	// 新增
	public void addUser(User t) throws Exception {
		userMapper.insert(t);
	};

	// 修改
	public void updateUser(User t) throws Exception {
		userMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	};

	// 登录
	public User loginUser(User user) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andPasswordEqualTo(user.getPassword()).andUsernameEqualTo(user.getUsername());
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过用户名判断是否存在，（新增时不能重名）
	public User existUserWithUserName(String userName) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(userName);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过角色判断是否存在
	public User existUserWithRoleId(Integer roleId) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleidEqualTo(roleId);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	}*/

	public PageInfo<StudentVo> findUserPage(StudentVo studentVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"userid";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		StudentExample example=new StudentExample();
		Criteria criteria = example.createCriteria();
		List<StudentVo> slist=studentMapper.findPage(studentVo);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(slist);
		return pageInfo;
	}

	public List<Bj> findByBj() {
		// TODO Auto-generated method stub
		return bjMapper.selectAll();
	}
	// 通过用户名判断是否存在，（新增时不能重名）
		public Student existUserWithUserName(String userName) throws Exception {
			StudentExample example = new StudentExample();
			 Criteria criteria = example.createCriteria();
			 criteria.andSnameEqualTo(userName);
			List<Student> userList = studentMapper.selectByExample(example);
			return userList.isEmpty()?null:userList.get(0);
		};
	
		// 新增
		public void addStudent(Student t) throws Exception {
			studentMapper.insert(t);
		};

		// 修改
		public void updateStudent(Student t) throws Exception {
			studentMapper.updateByPrimaryKeySelective(t);
		};

		// 删除
		public void deleteStudent(Integer id) throws Exception {
			studentMapper.deleteByPrimaryKey(id);
		}

		public List<StudentVo> findPage(StudentVo studentVo) {
			// TODO Auto-generated method stub
			
			List<StudentVo> slist=studentMapper.findPage(studentVo);
			
			return slist;
		};

}
