package websockets;

/**
 * Created by Thomas on 30/05/2015.
 */
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;

@WebServlet(name = "chatServlet", urlPatterns = "/chat")
public class socketTest extends HttpServlet
{
    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection");
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String action = request.getParameter("action");
        if("list".equals(action))
        {
            request.setAttribute("sessions", "ChatEndpoint.pendingSessions");
            request.getRequestDispatcher("/WEB-INF/jsp/view/chat/list.jsp")
                    .forward(request, response);
        }
        else
            response.sendRedirect("tickets");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setHeader("Expires", "Thu, 1 Jan 1970 12:00:00 GMT");
        response.setHeader("Cache-Control","max-age=0, must-revalidate, no-cache");

        String action = request.getParameter("action");
        String view = null;
        switch(action)
        {
            case "new":
                request.setAttribute("chatSessionId", 0);
                view = "chat";
                break;
            case "join":
                String id = request.getParameter("chatSessionId");
                if(id == null || !NumberUtils.isDigits(id))
                    response.sendRedirect("chat?list");
                else
                {
                    request.setAttribute("chatSessionId", Long.parseLong(id));
                    view = "chat";
                }
                break;
            default:
                response.sendRedirect("tickets");
                break;
        }

        if(view != null)
            request.getRequestDispatcher("/WEB-INF/jsp/view/chat/" + view + ".jsp")
                    .forward(request, response);
    }
}

