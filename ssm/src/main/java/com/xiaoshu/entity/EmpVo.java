package com.xiaoshu.entity;

public class EmpVo extends Emp {
private String dname;

public EmpVo(String dname) {
	super();
	this.dname = dname;
}

public EmpVo() {
	super();
	// TODO Auto-generated constructor stub
}

public String getDname() {
	return dname;
}

public void setDname(String dname) {
	this.dname = dname;
}
}
