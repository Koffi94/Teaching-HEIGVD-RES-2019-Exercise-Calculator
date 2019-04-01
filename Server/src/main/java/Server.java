import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator Server
 */
public class Server {

    static final Logger LOG = Logger.getLogger(Server.class.getName());

    private final int testDuration = 15000;
    private final int pauseDuration = 1000;
    private final int numberOfIterations = testDuration / pauseDuration;
    private final int listenPort = 12345;

    /**
     * This method does the entire processing.
     */
    public void start() {
        System.out.println("Starting server...");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            LOG.log(Level.INFO, "Creating a server socket and binding it on any of the available network interfaces and on port {0}", new Object[]{Integer.toString(listenPort)});
            serverSocket = new ServerSocket(listenPort, 50);
            logServerSocketAddress(serverSocket);

            LOG.log(Level.INFO, "Waiting (blocking) for a connection request on {0} : {1}", new Object[]{serverSocket.getInetAddress(), Integer.toString(serverSocket.getLocalPort())});
            clientSocket = serverSocket.accept();

            LOG.log(Level.INFO, "A client has arrived. We now have a client socket with following attributes:");
            logSocketAddress(clientSocket);

            LOG.log(Level.INFO, "Getting a Reader and a Writer connected to the client socket...");
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream());

            LOG.log(Level.INFO, "Starting my job... sending current time to the client for {0} ms", testDuration);

            // Informations for client
            writer.println(String.format("Write the calcul you want to do (if you want to quite the application write STOP "));
            writer.println(String.format("The calcul must be like this number1 operation number2 and you can do +-/*"));
            writer.println(String.format("Example: 2 + 2 "));
            writer.flush();

            while(true){
                // Read what the client is sending
                String clientString = reader.readLine();
                System.out.println(clientString);
                String[] client = clientString.split(" ");

                // Exit the programm
                if(clientString.equals("STOP")){
                    break;
                }

                int result, tmp;

                // Check if it's a number
                if(client[0].matches("\\d+") && client[2].matches("\\d+")){
                    result = Integer.parseInt(client[0]);
                    tmp = Integer.parseInt(client[2]);

                    switch(client[1].charAt(0)){
                        case '+':
                            result += tmp;
                            break;
                        case '-':
                            result -= tmp;
                            break;
                        case '*':
                            result *= tmp;
                            break;
                        case '/':
                            result /= tmp;
                            break;
                    }

                    writer.println("Result:" + result);
                } else {
                    writer.println(String.format("ERROR: Wrong value"));
                }
                //writer.flush();
                Thread.sleep(pauseDuration);
                writer.println("Write the next calcul");
                writer.flush();
            }


        } catch (IOException  ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch(InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } finally {
            LOG.log(Level.INFO, "We are done. Cleaning up resources, closing streams and sockets...");
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.close();
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * A utility method to print server socket information
     *
     * @param serverSocket the socket that we want to log
     */
    private void logServerSocketAddress(ServerSocket serverSocket) {
        LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{serverSocket.getLocalSocketAddress()});
        LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(serverSocket.getLocalPort())});
        LOG.log(Level.INFO, "               is bound: {0}", new Object[]{serverSocket.isBound()});
    }

    /**
     * A utility method to print socket information
     *
     * @param clientSocket the socket that we want to log
     */
    private void logSocketAddress(Socket clientSocket) {
        LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{clientSocket.getLocalAddress()});
        LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(clientSocket.getLocalPort())});
        LOG.log(Level.INFO, "  Remote Socket address: {0}", new Object[]{clientSocket.getRemoteSocketAddress()});
        LOG.log(Level.INFO, "            Remote port: {0}", new Object[]{Integer.toString(clientSocket.getPort())});
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

        Server server = new Server();
        server.start();
    }

}

