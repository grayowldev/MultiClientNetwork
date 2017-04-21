import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by GrayOwl on 4/20/17.
 */
public class MultiThreadServer extends Application {
    // Text area for displaying contents
    private TextArea ta = new TextArea();

    private int clientNo = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("MultiThreadServer");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() ->{
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                ta.appendText("MultiThreadServer started at " + new Date() + '\n');

                while (true){
                    Socket socket = serverSocket.accept();

                    clientNo++;

                    Platform.runLater(() -> {
                        ta.appendText("Started thread for client at " + new Date() + '\n' );

                        InetAddress inetAddress = socket.getInetAddress();
                        ta.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                        ta.appendText("Client " + clientNo + "'s IP address is " + inetAddress.getHostAddress() + "\n");

                    });

                    new Thread(new HandleAClient(socket)).start();
                }
            }
            catch (IOException ex){
                System.err.println(ex);
            }
        }).start();
    }

    class HandleAClient implements Runnable {
        private Socket socket;

        public HandleAClient(Socket socket){
            this.socket = socket;
        }

        public void run(){
            try {
                DataInputStream clientInput = new DataInputStream(socket.getInputStream());
                DataOutput clientOutput = new DataOutputStream(socket.getOutputStream());

                while (true){
                    double radius = clientInput.readDouble();
                    double area = radius * radius * Math.PI;

                    clientOutput.writeDouble(area);

                    Platform.runLater(() -> {
                        ta.appendText("radius receive from client: " + radius + '\n');
                        ta.appendText("Area found: " + area  + '\n');
                    });
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
