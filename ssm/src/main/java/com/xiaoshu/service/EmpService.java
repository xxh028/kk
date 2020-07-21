package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.DeptMapper;
import com.xiaoshu.dao.EmpMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Dept;
import com.xiaoshu.entity.Emp;
import com.xiaoshu.entity.EmpExample;
import com.xiaoshu.entity.EmpExample.Criteria;
import com.xiaoshu.entity.EmpVo;
import com.xiaoshu.entity.User;

@Service
public class EmpService {

	/*@Autowired
	UserMapper userMapper;

	// 查询所有
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
	@Autowired
	EmpMapper empMapper;
	@Autowired
	DeptMapper deptMapper;
	public PageInfo<EmpVo> findUserPage(EmpVo empVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"eid";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		EmpExample example=new EmpExample();
	Criteria criteria = example.createCriteria();
		
		List<EmpVo> eList=empMapper.findPage(empVo);
		PageInfo<EmpVo> pageInfo = new PageInfo<EmpVo>(eList);
		return pageInfo;
	}
	public List<Dept> findDept() {
		// TODO Auto-generated method stub
		return deptMapper.selectAll();
	}
	// 新增
		public void addEmp(Emp t) throws Exception {
			empMapper.insert(t);
		};

		// 修改
		public void updateEmp(Emp t) throws Exception {
			empMapper.updateByPrimaryKeySelective(t);
		};

		// 删除
		public void deleteEmp(Integer id) throws Exception {
			empMapper.deleteByPrimaryKey(id);
		};
		// 通过用户名判断是否存在，（新增时不能重名）
		public Emp existUserWithUserName(String userName) throws Exception {
			EmpExample example = new EmpExample();
			Criteria criteria = example.createCriteria();
			criteria.andEnameEqualTo(userName);
			List<Emp> userList = empMapper.selectByExample(example);
			return userList.isEmpty()?null:userList.get(0);
		};
		public List<EmpVo> findPage(EmpVo empVo) {
			
			
			List<EmpVo> eList=empMapper.findPage(empVo);
			
			return eList;
		}
}
