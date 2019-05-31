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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        if (!Files.exists(Paths.get(config.getServletContext().getRealPath("data"+File.separator+"items.csv")))){
            new File(config.getServletContext().getRealPath("data")).mkdir();
            try {
                new File(config.getServletContext().getRealPath("data"+File.separator+"items.csv")).createNewFile();
            } catch (IOException e) {
                System.err.println("Can not crate file \"data"+File.separator+"items.csv\"");
            }
        }
        initColllection(config);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                initColllection(config);
            }
        };
        new Timer().schedule(timerTask,300000,300000);
    }
}
