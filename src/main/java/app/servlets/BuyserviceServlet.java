package app.servlets;

import app.Servise;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


public class BuyserviceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        List<String> goodsArticl = Arrays.asList(req.getParameter("desiredProduct").split(","));

        if (Servise.validateArticl(req.getParameter("desiredProduct")).isEmpty()) {
            List<String> order = Servise.getOrderList(goodsArticl);
            if (Servise.createOrderFile(order)) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot create order file. No access. " +
                        "Give the program permission to create a file or change data directory.");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Your order list includes not exist goods.");
        }
        resp.setContentType("text/plain");
    }
}
