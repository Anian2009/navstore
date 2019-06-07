package app.servlets;

import app.entities.Item;
import app.model.Model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import static app.Servise.initColllection;


public class ItemsLoaderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        List<Item> list = Model.getInstance().getList();
        if (list.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "The product list is currently unavailable");
        } else {
            resp.getWriter().write(String.valueOf(list));
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        String directoryPath = null;
        Properties properties = new Properties();

        try {
            properties.load(config.getServletContext().getResourceAsStream("config.properties"));
            directoryPath = properties.getProperty("boot.file.path");
            System.out.println("User set data directory - "+directoryPath);
        } catch (Exception e) {
            System.err.println("No properties file. How to solve this problem see In the README file.");
        } finally {
            System.out.println("User is not defined data directory or input incorrect path.");
            directoryPath = System.getProperty("catalina.base");
            if (directoryPath == null){
                System.out.println("The program can not determine the data directory using \"CATALINA_BASE\".");
                directoryPath = System.getProperty("user.home", "/");
            }

            System.out.println("Directory for data is defined \"" + directoryPath + File.separator + "data\". " +
                    "How change data directory see in README file.");

            initColllection(directoryPath + File.separator + "data");
            String finalDirectoryPath = directoryPath;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    initColllection(finalDirectoryPath + File.separator + "data");
                }
            };
            new Timer().schedule(timerTask, 300000, 300000);
        }

//        if (!Files.exists(Paths.get(directoryPath))) {
//            System.out.println("User is not defined data directory.");
//            directoryPath = System.getProperty("catalina.base");
//            if (directoryPath == null){
//                System.out.println("The program can not determine the data directory using \"CATALINA_BASE\".");
//                directoryPath = System.getProperty("user.home", "/");
//            }
//
//        }
//
//        System.out.println("Directory for data is defined \"" + directoryPath + File.separator + "data\". " +
//                "How change data directory see in README file.");
//
//        initColllection(directoryPath + File.separator + "data");
//        String finalDirectoryPath = directoryPath;
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                initColllection(finalDirectoryPath + File.separator + "data");
//            }
//        };
//        new Timer().schedule(timerTask, 300000, 300000);
    }
}
