package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Processor of HTTP request.
 */
public class Processor {
    private final Socket socket;
    private final HttpRequest request;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    public void process(List<String> items) throws IOException {
        // Print request that we received.
        System.out.println("Got request:");
        System.out.println(request.toString());
        System.out.flush();
        // To send response back to the client.
        PrintWriter output = new PrintWriter(socket.getOutputStream());
        // We are returning a simple web page now.
        String reqLine = request.getRequestLine();
        String endpoint = reqLine.substring(reqLine.indexOf("/") + 1,reqLine.indexOf("H") - 1);
        if(endpoint.contains("create")){
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Add</title></head>");
            output.println("<body><form method='get' action='/create'>" +
                    "<input type='text' name='name'><input type='submit' value='Add'></form><br><br>" +
                    "</body>");
            output.println("</html>");
            output.flush();
            if(request.getRequestLine().indexOf("=") != -1){
                String name = request.getRequestLine().substring(request.getRequestLine().indexOf("=") + 1, request.getRequestLine().indexOf("H") - 1);
                System.out.println(name);
                items.add(name);
                System.out.println(items);
            }
        }
        else if(endpoint.contains("delete")){
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Delete</title></head>");
            output.println("<body><form method='get' action='/delete'>" +
                    "<input type='text' name='name'><input type='submit' value='delete'></form><br><br>" +
                    "</body>");
            output.println("</html>");
            output.flush();
            if(request.getRequestLine().indexOf("=") != -1){
                String name = request.getRequestLine().substring(request.getRequestLine().indexOf("=") + 1, request.getRequestLine().indexOf("H") - 1);
                System.out.println(name);
                items.remove(name);
                System.out.println(items);
            }
        }
        else if(endpoint.contains("exec")){
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Delete</title></head>");
            output.println("<body><table>");
            for(int i = 0; i < items.size(); i++) {
                output.println("<tr>" +
                        "<td>" +
                        String.valueOf(i)
                        +
                        "</td>" +
                        "<td>" +
                        items.get(i)
                        +
                        "</td>" +
                        "</tr>");
            }
            output.println("</table><br><br>" +
                    "</body>");
            output.println("</html>");
            output.flush();
        }
        else{
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Error</title></head>");
            output.println("<body><p>Hello, guy!<br>But there is no another pages!</p><br><br></body>");
            output.println("</html>");
            Thread.sleep(6000);
            output.flush();
        }
        System.out.println();
        socket.close();
    }
}
