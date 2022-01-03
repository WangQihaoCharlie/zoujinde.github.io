package com.jinde.web.view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jinde.web.controller.DataController;
import com.jinde.web.util.JsonUtil;
import com.jinde.web.util.LogUtil;
import com.jinde.web.util.WebUtil;

@WebServlet(urlPatterns = "/data")
public class DataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = DataServlet.class.getSimpleName();

    // Only define doPost
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = null;
        String body = WebUtil.getPostBody(req);
        String tab = req.getParameter("tab");
        String act = JsonUtil.getString(body, WebUtil.ACT);
        if (tab == null || act == null) {
            result = "tab or act is null";
            LogUtil.println(TAG, result);
        } else if (act.equals(WebUtil.ACT_SELECT)) {
            result = DataController.instance().select(body, tab);
        } else if (act.equals(WebUtil.ACT_INSERT)) {
            result = DataController.instance().insert(body, tab);
        } else if (act.equals(WebUtil.ACT_UPDATE)) {
            result = DataController.instance().update(body, tab);
        } else if (act.equals(WebUtil.ACT_DELETE)) {
            result = DataController.instance().delete(body, tab);
        } else if (act.equals("show_path")) {
            result = showPath(req);
        } else {
            result = "Unknown";
        }

        // Write response
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter pw = resp.getWriter();
        pw.write(result);
        pw.flush();
    }

    // Show path files
    private String showPath(HttpServletRequest req) {
        StringBuilder builder = new StringBuilder();
        String base = System.getProperty("catalina.base");
        String home = System.getProperty("catalina.home");
        builder.append(base).append(",\n");
        builder.append(home).append(",\n");

        //String path = req.getServletContext().getRealPath("/");
        //String path = getClass().getResource("/").getPath();
        //Check below AWS tomcat path
        String path = "/usr/share/tomcat";
        builder.append(path).append(",\n");
        //buildFiles(builder, new File(path));
        builder.append(copyJar() + ""); // Copy jar to LIB
        return builder.toString();
    }

    // build files
    @SuppressWarnings("unused")
    private void buildFiles(StringBuilder builder, File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    builder.append(f.getAbsolutePath()).append(",\n");
                    buildFiles(builder, f);
                } else {
                    builder.append(f.getAbsolutePath()).append(",\n");
                }
            }
        }
    }

    //Cp mysql jar to AWS tomcat lib
    private String copyJar() {
        String error = null;
        String src = "/usr/share/tomcat/webapps/ROOT/WEB-INF/lib/";
        String dst = "/usr/share/tomcat/lib/";
        //There are 2 jars in lib
        //mysql-connector-java-8.0.27.jar,
        //protobuf-java-3.11.4.jar
        //Copy these jars to /usr/share/tomcat/lib/
        //Then we don't need upload the big jars to AWS
        File[] srcPath = new File(src).listFiles();
        if (srcPath != null) {
            for (File f : srcPath) {
                File newJar = new File(dst + f.getName());
                if (newJar.exists()) {
                    LogUtil.println(TAG, newJar + " exists");
                } else {
                    LogUtil.println(TAG, newJar + " copied");
                    error = WebUtil.copyFile(f, newJar);
                    if (error != null) {
                        break;
                    }
                }
            }
        }
        return error;
    }

}
