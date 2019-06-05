package app.servlets;

import app.Servise;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

//@WebServlet("/buyservice")
public class BuyserviceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        List<String> goodsArticl = Arrays.asList(req.getParameter("desiredProduct").split(","));

        if (Servise.validate(req.getParameter("desiredProduct")).isEmpty()) {
            List<String> order = Servise.orderList(goodsArticl);
            File directory = new File(Servise.dataDirectoryPath
                    + File.separator + "order" + File.separator);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            int solt = 1;
            synchronized (this) {
                Path file = Paths.get(directory + File.separator +
                        new SimpleDateFormat("yyyy_MM_dd HH-mm").format(Calendar.getInstance().getTime()) + ".csv");
                while (Files.exists(file)) {
                    file = Paths.get(directory + File.separator +
                            new SimpleDateFormat("yyyy_MM_dd HH-mm").format(Calendar.getInstance().getTime()) + "_" + solt + ".csv");
                    solt++;
                }
                try {
                    Files.write(file, order, Charset.forName("UTF-8"));
                } catch (NoSuchFileException ex) {
                    System.err.println("Can not create order file. Give the program access to the directory "
                            +Servise.dataDirectoryPath+" or change data directory.");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (IOException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    e.printStackTrace();
                }
            }
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "No such goods.");
        }
        resp.setContentType("text/plain");
    }
}
