package app.servlets;

import app.entities.Item;
import app.model.Model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static app.Servise.initColllection;

//@WebServlet("/shop/items")
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
        } catch (Exception e) {
            System.err.println("No data in properties file");
        }

        if (!Files.exists(Paths.get(directoryPath))) {
            directoryPath = System.getenv("CATALINA_HOME") +
                    File.separator + "webapps" + File.separator + "data";
        }

        initColllection(directoryPath);
        String finalDirectoryPath = directoryPath;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                initColllection(finalDirectoryPath);
            }
        };
        new Timer().schedule(timerTask, 300000, 300000);
    }
}
