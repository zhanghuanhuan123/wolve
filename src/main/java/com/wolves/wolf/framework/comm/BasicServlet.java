package com.wolves.wolf.framework.comm;


import com.wolves.wolf.Startup;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-8-12
 * Time: 9:35:20
 */

public class BasicServlet extends HttpServlet {
    private static DIceCommStationI dIceCommStationI;


    public void init(ServletConfig config)
            throws ServletException {
        try {
            Startup.main(new String[]{config.getInitParameter("initial_file")});
            dIceCommStationI = new DIceCommStationI();
        } catch (Exception e) {
            System.exit(-1);
        }

    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        byte[] request = IOUtils.toByteArray(req.getInputStream());
        byte[] response = dIceCommStationI._do(request, null);
        IOUtils.write(response, resp.getOutputStream());
    }

}
