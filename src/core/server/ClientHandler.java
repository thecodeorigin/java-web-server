package core.server;

import core.utils.Utilities;

import java.io.*;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class ClientHandler extends Thread implements Runnable {

    private Socket connection;

    private BufferedReader reader;
    private PrintWriter writer;
    private BufferedOutputStream dataOut;

    public Utilities utils = Utilities.getInstance();

    private final File WEB_ROOT = new File("./www");
    private final File NOT_FOUND = new File(WEB_ROOT, "404.html");
    private final File NOT_IMPLEMENTED = new File(WEB_ROOT, "501.html");


    // Constructor
    public ClientHandler(HTTPServer server) {
        try {
            System.out.println("[CONSTRUCTOR] New client connected: " + server.getRequest());
            this.connection = server.getRequest();
            // Initialize reader and writer

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            writer = new PrintWriter(connection.getOutputStream());
            dataOut = new BufferedOutputStream(connection.getOutputStream());
        } catch (Exception e) {
            System.out.println("[CONSTRUCTOR] An error has occurred during the start of client thread");
        }
    }

    // This main function will run whenever a new thread is created
    @Override
    public void run() {

        try {
            // read the first header.
            String header = reader.readLine();
            if (header != null) {
                // Read the headers
                StringTokenizer tokenizer = new StringTokenizer(header);
                String method = tokenizer.nextToken().toUpperCase();
                String resource = tokenizer.nextToken().toLowerCase();
                String protocol = tokenizer.nextToken();

                String status;
                File file;

                if (method.equals("GET")) {
                    // Check if is URL path or file path
                    // If not file path, then it's directory with an index.html
                    if (!resource.contains(".")) {
                        file = new File("./www" + resource, "index.html");
                        status = " 200 OK";
                    } else {
                        try {
                            file = new File("./www" + resource);
                            status = " 200 OK";
                        } catch (Exception error) {
                            file = NOT_FOUND;
                            status = " 404 Not Found";
                        }
                    }
//                    if (resource.endsWith("/")) {
//                        file = new File("./www" + resource, "index.html");
//                        status = " 200 OK";
//                    } else {
//
//                    }
                } else {
                    file = NOT_IMPLEMENTED;
                    status = " 501 Not Implemented";
                }

                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
                String date = now.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z"));

                System.out.printf("%s %s%s %s\n", method, resource, status, date);
                byte[] data = utils.readFile(file);

                // write the headers
                writer.println(protocol + status);
                writer.println("Server: HttpServer v1.0");
                writer.println("Date: " + date);
                writer.println("Content-Type: text/html; charset=utf-8");
                writer.println("Content-Length: " + data.length);
                writer.println();
                writer.flush();

                // write the file contents (The website)
                dataOut.write(data, 0, data.length);
                dataOut.flush();

            }

            this.disconnect();

        } catch (IOException e) {

            this.disconnect();
            System.out.println("[RUN] " + e.getMessage());

        }
    }

    // This function is used to disconnect current client
    public void disconnect() {
        try {
            System.out.println("[DISCONNECT] Client with port " + connection.getPort() + " has disconnected");
            // Close connection, reader and writer to prevent resource leak
            this.connection.close();
            this.writer.close();
            this.reader.close();
        } catch(Exception e) {
            System.out.println("[DISCONNECT] An error has occurred during client disconnect");
        }
    }

}
