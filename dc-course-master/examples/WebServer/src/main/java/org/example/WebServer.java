package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple web server.
 */
public class WebServer {
    public static void main(String[] args) {
        // Port number for http request
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        // The maximum queue length for incoming connection
        int queueLength = args.length > 2 ? Integer.parseInt(args[2]) : 50;;
        List<Worker> names = new ArrayList<>();
        ThreadSafeQueue<String> queue = new ThreadSafeQueue<>();
        int numOfThreads = (args.length > 1 ? Integer.parseInt(args[1]) : 12);
        Consumer<String> cons;
        for (int i = 0; i < numOfThreads; i++) {
            cons = new Consumer<>(1, queue);
            cons.start();
        }
        try (ServerSocket serverSocket = new ServerSocket(port, queueLength)) {
            System.out.println("Web Server is starting up, listening at port " + port + ".");
            System.out.println("You can access http://localhost:" + port + " now.");

            while (true) {
                // Make the server socket wait for the next client request
                Socket socket = serverSocket.accept();



                System.out.println("Got connection!");

                // To read input from the client
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                try {
                    // Get request
                    HttpRequest request = HttpRequest.parse(input);

                    // Process request
                    Processor proc = new Processor(socket, request);
                    proc.process(queue, names, numOfThreads);
                    System.out.println();
                } catch (IOException ex) {
                    ex.printStackTrace();                   
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            System.out.println("Server has been shutdown!");
        }
    }
}
