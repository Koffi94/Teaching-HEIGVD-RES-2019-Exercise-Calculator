import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;


public class Client {

    final static int BUFFER_SIZE = 1024;
    static int counter = 0;

     public String makeRequest() {
        //Scanner reader = new Scanner(System.in);
        //String request = reader.nextLine();
        //reader.close();
        String request = "";
        switch(counter) {
            case 0:
                request = "2 + 3";
                break;
            case 1:
                request = "3 * 7";
                break;
            case 2:
                request = "10 - 4";
                break;
            case 3:
                request = "16 / 2";
                break;
            default:
                    break;
        }
        counter++;
        if(counter == 4)
            counter = 0;

        return request;
    }

    public void connect() {
        Socket clientSocket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        String request;
        String response;

        ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();

        try {
            clientSocket = new Socket("172.17.0.3", 12345);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());

            while(true) {


                request = makeRequest();
                System.out.println(request);
                if(request.equals("STOP"))
                    break;

                writer.println(request);
                writer.flush();

                System.out.println(reader.readLine());
                //System.out.println(reader.readLine());

            }

        } catch (IOException  ex) {
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.close();
            try {
                clientSocket.close();
            } catch (IOException ex) {
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
