import core.server.HTTPServer;

public class Main {

    public static void main(String[] args) {
        try {
            // Initialize server
            HTTPServer.init(8000);
        } catch (Exception err) {
            System.out.println(err);
        }
    }

}
