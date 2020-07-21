package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.dao.BjMapper;
import com.xiaoshu.entity.Attachment;
import com.xiaoshu.entity.Bj;
import com.xiaoshu.entity.Log;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.StudentService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("student")
public class StudentController extends LogController{
	static Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	@Autowired
	StudentService StudentService;
	
	@RequestMapping("studentIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Bj> blist=StudentService.findByBj();
		request.setAttribute("blist", blist);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "student";
	}
	
	
	@RequestMapping(value="studentList",method=RequestMethod.POST)
	public void userList(StudentVo studentVo,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			String order = request.getParameter("order");
			String ordername = request.getParameter("ordername");
			
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StudentVo> userList= StudentService.findUserPage(studentVo,pageNum,pageSize,ordername,order);
			
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",userList.getTotal() );
			jsonObj.put("rows", userList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveStudent")
	public void reserveUser(HttpServletRequest request,Student student,HttpServletResponse response,MultipartFile picFile) throws Exception, Exception{
		Integer sid = student.getSid();
		JSONObject result=new JSONObject();
		
		String filename = picFile.getOriginalFilename();
		String suffix=filename.substring(filename.indexOf("."));
		String newName=System.currentTimeMillis()+suffix;
		File file=new File("F://photo//"+newName);
		picFile.transferTo(file);
		student.setPic(newName);
		try {
			if (sid != null) {   // userId不为空 说明是修改
				Student userName = StudentService.existUserWithUserName(student.getSname());
				if(userName != null && userName.getSid().compareTo(sid)==0){
					student.setSid(sid);
					StudentService.updateStudent(student);
					result.put("success", true);
				}else{
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
				
			}else {   // 添加
				if(StudentService.existUserWithUserName(student.getSname())==null){ 
					// 没有重复可以添加
					StudentService.addStudent(student);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deleteStudent")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				StudentService.deleteStudent(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("editPassword")
	public void editPassword(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		if(currentUser.getPassword().equals(oldpassword)){
			User user = new User();
			user.setUserid(currentUser.getUserid());
			user.setPassword(newpassword);
			try {
				userService.updateUser(user);
				currentUser.setPassword(newpassword);
				session.removeAttribute("currentUser"); 
				session.setAttribute("currentUser", currentUser);
				result.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("修改密码错误",e);
				result.put("errorMsg", "对不起，修改密码失败");
			}
		}else{
			logger.error(currentUser.getUsername()+"修改密码时原密码输入错误！");
			result.put("errorMsg", "对不起，原密码输入错误！");
		}
		WriterUtil.write(response, result.toString());
	}
	//导出
	@RequestMapping("exportStudent")
	public void backup(HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		try {
			String time = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss");
		    String excelName = "手动备份"+time;
			List<StudentVo> slist=StudentService.findPage(new StudentVo());
			String[] handers = {"序号","学生姓名","学生性别","学生年龄","学生生日","学生班级","学生头像"};
			// 1导入硬盘
			ExportExcelToDisk(request,handers,slist, excelName);
			
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("", "对不起，备份失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	
	// 导出到硬盘
	@SuppressWarnings("resource")
	private void ExportExcelToDisk(HttpServletRequest request,
			String[] handers, List<StudentVo> list, String excleName) throws Exception {
		
		try {
			HSSFWorkbook wb = new HSSFWorkbook();//创建工作簿
			HSSFSheet sheet = wb.createSheet("操作记录备份");//第一个sheet
			HSSFRow rowFirst = sheet.createRow(0);//第一个sheet第一行为标题
			rowFirst.setHeight((short) 500);
			for (int i = 0; i < handers.length; i++) {
				sheet.setColumnWidth((short) i, (short) 4000);// 设置列宽
			}
			//写标题了
			for (int i = 0; i < handers.length; i++) {
			    //获取第一行的每一个单元格
			    HSSFCell cell = rowFirst.createCell(i);
			    //往单元格里面写入值
			    cell.setCellValue(handers[i]);
			}
			for (int i = 0;i < list.size(); i++) {
			    //获取list里面存在是数据集对象
			    StudentVo studentVo = list.get(i);
			    //创建数据行
			    HSSFRow row = sheet.createRow(i+1);
			    //设置对应单元格的值
			    row.setHeight((short)400);   // 设置每行的高度
			    //"序号","操作人","IP地址","操作时间","操作模块","操作类型","详情"
			    row.createCell(0).setCellValue(i+1);
			    row.createCell(1).setCellValue(studentVo.getSname());
			    row.createCell(2).setCellValue(studentVo.getSex());
			    row.createCell(3).setCellValue(studentVo.getAge());
			    row.createCell(4).setCellValue(TimeUtil.formatTime(studentVo.getBir(), "yyyy-MM-dd"));
			    row.createCell(5).setCellValue(studentVo.getBid());
			    row.createCell(6).setCellValue(studentVo.getPic());
			    
			}
			//写出文件（path为文件路径含文件名）
				OutputStream os;
				File file = new File("F://photo//"+excleName+".xls");
				
				if (!file.exists()){//若此目录不存在，则创建之  
					file.createNewFile();  
					logger.debug("创建文件夹路径为："+ file.getPath());  
	            } 
				os = new FileOutputStream(file);
				wb.write(os);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	}

	
	
	
	
	
	
	@RequestMapping("importStudent")
	public void importStudent(HttpServletRequest request,HttpServletResponse response,MultipartFile excelFile){
		JSONObject result=new JSONObject();
		try {
			
			HSSFWorkbook wb=new HSSFWorkbook(excelFile.getInputStream());
			HSSFSheet sheet = wb.createSheet();//获取SHEET页
			int rowNum = sheet.getLastRowNum();
			for (int i = 1; i <= rowNum; i++) {
				HSSFRow row = sheet.getRow(i);
				String sname = row.getCell(0).getStringCellValue();
				String sex = row.getCell(1).getStringCellValue();
				String age = row.getCell(2).getStringCellValue();
				Date date = row.getCell(3).getDateCellValue();
				String bname = row.getCell(4).getStringCellValue();
				String pic = row.getCell(5).getStringCellValue();
				
				Integer bid=findByBj(bname);
				Student student=new Student();
				student.setSname(sname);
				student.setSex(sex);
				student.setAge(age);
				student.setBir(date);
				student.setPic(pic);
				
				StudentService.addStudent(student);
			}
			
			
			
			
			
			
			
			result.put("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}

@Autowired
BjMapper bjMapper;
	private Integer findByBj(String bname) {
		Bj bj=new Bj();
		bj.setBname(bname);
		Bj one=bjMapper.selectOne(bj);
		return one.getBid();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
