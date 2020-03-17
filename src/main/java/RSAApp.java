import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RSAApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent rootPanel = new RootPanel();
        Scene scene = new Scene(rootPanel);
        primaryStage.titleProperty().setValue("RSA Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){launch(args);}
}
