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

        if (Servise.validateArticl(req.getParameter("desiredProduct")).isEmpty()) {
            List<String> order = Servise.getOrderList(goodsArticl);
            if (Servise.createOrderFile(order)) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot create order file.");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "No such goods.");
        }
        resp.setContentType("text/plain");
    }
}
