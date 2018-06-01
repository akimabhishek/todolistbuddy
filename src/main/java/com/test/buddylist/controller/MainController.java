package com.test.buddylist.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@RequestMapping(value="/welcome", method=RequestMethod.GET)
	public ModelAndView viewpage() {
		ModelAndView model = new ModelAndView("success");
		return model;
	}
	
	@RequestMapping(value="/formpage", method=RequestMethod.GET)
	public ModelAndView formpage() {
		ModelAndView model = new ModelAndView("form");
		return model;
	}
	
	@RequestMapping(value="/formsave", method=RequestMethod.POST)
	public ModelAndView formsave(@RequestParam("emailid") String emailid) { 
		ModelAndView model = new ModelAndView("form");
		MySQLConnect mySQLConnect = new MySQLConnect();
		ArrayList<TaskList> tasks = mySQLConnect.getTask(emailid);
		System.out.println(tasks);
		model.addObject("tasklist",tasks);
		return model;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView deletetask(@PathVariable("id") int id) {
		ModelAndView model = new ModelAndView("form");
		MySQLConnect mySQLConnect = new MySQLConnect();
		mySQLConnect.deletetask(id);
		return model;
	}
	
	@RequestMapping(value="/edit/{id}/{user_id}", method=RequestMethod.GET)
	public ModelAndView editform(@PathVariable("id") int id, @PathVariable("user_id") int user_id) {
		ModelAndView model = new ModelAndView("update");
		MySQLConnect mySQLConnect = new MySQLConnect();
		TaskList task = mySQLConnect.getTaskbyId(id);
		model.addObject(task);
		return model;
	}
	
	@RequestMapping(value="/new_task/{user_id}", method=RequestMethod.GET) 
	public ModelAndView newtask(@PathVariable("user_id") int user_id) {
		ModelAndView model = new ModelAndView("newtask");
		model.addObject("user_id",user_id);
		return model;
	}
	
	@RequestMapping(value="/new_task/new/{user_id}", method=RequestMethod.POST)
	public String inserttask(@RequestParam("task_name") String task_name, @PathVariable("user_id") int user_id) throws Exception {
		//ModelAndView model = new ModelAndView("form");
		MySQLConnect mySQLConnect = new MySQLConnect();
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/buddytodo?useSSL=true", "root","root");
		String Query = "select max(id) from tasktodo";
		PreparedStatement ps = con.prepareStatement(Query);
		ResultSet resultSet = ps.executeQuery(Query);
		int id=0;
		if(resultSet.next()) {
			id = resultSet.getInt(1);
		}
		id = id+1;
		TaskList task = new TaskList(task_name,id,true,user_id);
		mySQLConnect.NewTask(task_name,id,user_id);
		return "redirect:/formpage";
	}
	
}
