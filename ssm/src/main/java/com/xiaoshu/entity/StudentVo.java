package com.xiaoshu.entity;

public class StudentVo extends Student {
private String bname;

public String getBname() {
	return bname;
}

public void setBname(String bname) {
	this.bname = bname;
}

public StudentVo() {
	super();
	// TODO Auto-generated constructor stub
}

public StudentVo(String bname) {
	super();
	this.bname = bname;
}

}
