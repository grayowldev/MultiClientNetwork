import javafx.application.Application;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by kwasi on 4/18/2017.
 */
public class Client extends Application {

    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage){
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5,5,5,5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter radius"));

        TextField textField = new TextField();
        textField.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(textField);

        BorderPane mainPane = new BorderPane();

        TextArea textArea = new TextArea();
        mainPane.setCenter(new ScrollPane(textArea));
        mainPane.setTop(paneForTextField);

        Scene scene = new Scene(mainPane, 500, 300);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        textField.setOnAction(event -> {
            try {
                double radius = Double.parseDouble(textField.getText().trim());

                toServer.writeDouble(radius);
                toServer.flush();

                double area = fromServer.readDouble();

                textArea.appendText("radius is: " + radius + '\n' );
                textArea.appendText("Area from server is: " + area
                + '\n');


            }
            catch (IOException ex){
                System.err.print(ex);
            }

        });
        try {
            Socket socket = new Socket("localHost",8000);
            System.out.println("local port: " + socket.getLocalPort());
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException exception){
            textArea.appendText(exception.toString() + '\n');
        }
    }
}
