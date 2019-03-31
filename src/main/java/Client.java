import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;


public class Client {

    final static int BUFFER_SIZE = 1024;

    public String makeRequest() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter your calcul with the following exemple syntax : REQUEST,8,*,8");
        String request = reader.nextLine();
        reader.close();
        return request;
    }

    public void connect() {
        Socket clientSocket = null;
        OutputStream os = null;
        InputStream is = null;
        String request;
        ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int newBytes;

        try {
            clientSocket = new Socket("172.17.0.2", 12345);
            os = clientSocket.getOutputStream();
            is = clientSocket.getInputStream();

            while(true) {
                request = makeRequest();
                os.write(request.getBytes());

                while ((newBytes = is.read(buffer)) != -1) {
                    responseBuffer.write(buffer, 0, newBytes);
                }

                System.out.println(responseBuffer.toString());
            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Client client = new Client();
        client.connect();

    }
}
