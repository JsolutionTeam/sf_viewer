import kr.co.jsol.domain.entity.ingsystem.InGSystemRepository;
import kr.co.jsol.domain.entity.ingsystem.InGSystemService;
import org.slf4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SocketServer.class);

    public static void main(String[] args) {
        serve(2332);
    }

    public static void serve(int port) {
        Socket socket = null;
        try {
            log.info("before serverSocket");
            ServerSocket serverSocket = new ServerSocket(port);
            log.info("after serverSocket");

            while (true) {
                socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
//            InputStreamReader reader = new InputStreamReader(input);
//            int character = reader.read();  // reads a single character

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();    // reads a line of text

                log.info("line : {}", line);

                OutputStream output = socket.getOutputStream();

                output.write("This is a message sent to the server".getBytes());
            }
        } catch (Exception e) {
            log.error("error!! {}", e.getMessage());
//            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (Exception ignored) {
            }
        }
    }
}
