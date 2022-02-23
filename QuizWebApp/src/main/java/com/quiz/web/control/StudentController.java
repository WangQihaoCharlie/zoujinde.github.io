package com.quiz.web.control;

import javax.servlet.http.HttpServletRequest;

import com.quiz.web.model.DataManager;
import com.quiz.web.model.SqlAction;
import com.quiz.web.model.Student;
import com.quiz.web.util.WebUtil;

public class StudentController {

    private static final StudentController INSTANCE = new StudentController();

    private StudentController() {
    }

    // Single Instance
    public static StudentController instance() {
        return INSTANCE;
    }

    // Get JSON string of student
    // http://localhost:8080/student?act=select
    public String getStudent(HttpServletRequest req) {
        String sql = "select * from students where id < ?";
        Object[] values = new Object[]{100};
        DataManager dm = DataManager.instance();
        String jsonStr = "";
        try {
            jsonStr = dm.select(sql, values);
        } catch (Exception e) {
            jsonStr = e.toString();
        }
        return jsonStr;
    }

    // Add a student using below test URL:
    // http://localhost:8080/student?act=insert&name=Cat&gender=true&grace=1&score=100
    public String addStudent(HttpServletRequest req) {
        String result = null;
        Student student = WebUtil.buildObject(req, Student.class);
        if (student != null) {
            SqlAction a1 = new SqlAction(student, WebUtil.ACT_INSERT);
            SqlAction a2 = new SqlAction(student, WebUtil.ACT_INSERT);
            result = DataManager.instance().runSql(new SqlAction[]{a1, a2});
        }
        return "addStudent=" + result;
    }

    // Update a student
    // http://localhost:8080/student?act=update&id=15&name=Bob&gender=true&grace=2&score=90
    public String updateStudent(HttpServletRequest req) {
        String result = null;
        Student student = WebUtil.buildObject(req, Student.class);
        if (student != null) {
            SqlAction a = new SqlAction(student, WebUtil.ACT_UPDATE);
            result = DataManager.instance().runSql(new SqlAction[]{a});
        }
        return "updateStudent=" + result;
    }

    // Delete a student
    // http://localhost:8080/student?act=delete&id=999
    public String deleteStudent(HttpServletRequest req) {
        String result = null;
        Student student = WebUtil.buildObject(req, Student.class);
        if (student != null) {
            SqlAction a1 = new SqlAction(student, WebUtil.ACT_DELETE);
            SqlAction a2 = new SqlAction("delete from students where id=?", new Object[]{13});
            result = DataManager.instance().runSql(new SqlAction[]{a1, a2});
        }
        return "deleteStudent=" + result;
    }

}