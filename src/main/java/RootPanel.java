import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RootPanel extends GridPane {

    private String pkFile;
    private String skFile;
    private String chiffreFile;
    private String messageFile;

    private RSA.RSAKeyPair rsaKeyPair = new RSA.RSAKeyPair(BigInteger.ZERO,BigInteger.ZERO,BigInteger.ZERO);

    private Button genKeyPair = new Button("Generate RSA Key Pair");
    private Button encryptMessage = new Button("Encrypt Message");
    private Button decryptMessage = new Button("Decrypt Message");
    private Button writeToFiles = new Button("Write to Files");
    private Button readFromFiles = new Button("Read from Files");
    private Button selectDir = new Button("Select File Dir");
    private Button clear = new Button("Clear all fields");

    private TextArea message = new TextArea();
    private TextArea encryptedMessage = new TextArea();
    private TextArea keyPair = new TextArea();
    private Spinner<Integer> keySize = new Spinner<>(512, 4096, 1024);
    private Label lpkFile = new Label("Pk File:\t");
    private Label lskFile = new Label("Sk File:\t");
    private Label lchiffreFile = new Label("Chiffre File:\t");
    private Label lmessageFile = new Label("Message File:\t");
    private Label lMessage = new Label("Message");
    private Label lKeys = new Label("Key Pair");
    private Label lKeySize = new Label("Key Size");
    private Label lEncryptedMessage = new Label("Encrypted Message");
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private ColumnConstraints cGrow = new ColumnConstraints();
    private ColumnConstraints cNoGrow = new ColumnConstraints();
    private RowConstraints rGrow = new RowConstraints();
    private RowConstraints rNoGrow = new RowConstraints();

    public RootPanel() {
        directoryChooser.titleProperty().setValue("File Directory for Key and Message Files");
        keyPair.editableProperty().setValue(false);
        encryptedMessage.editableProperty().setValue(false);
        encryptedMessage.setWrapText(true);
        keySize.setEditable(true);
        layoutControls();
        setupEventHandlers();
    }

    public void layoutControls() {
        setMinHeight(400);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(5));
        cGrow.setHgrow(Priority.ALWAYS);
        cNoGrow.setHgrow(Priority.NEVER);
        rGrow.setVgrow(Priority.ALWAYS);
        rNoGrow.setVgrow(Priority.NEVER);

        getColumnConstraints().addAll(cGrow, cGrow, cNoGrow, cGrow);
        getRowConstraints().addAll(rNoGrow, rNoGrow, rNoGrow, rGrow, rGrow, rGrow, rNoGrow, rGrow, rGrow, rGrow, rNoGrow, rGrow);

        add(selectDir, 0, 0);
        add(lpkFile, 1, 0);
        add(lskFile, 1, 1);
        add(lchiffreFile, 2, 0);
        add(lmessageFile, 2, 1);

        add(genKeyPair, 0, 2);
        add(lKeySize, 0, 3);
        add(keySize, 0, 4);
        add(encryptMessage, 0, 5);
        add(decryptMessage, 0, 6);
        add(writeToFiles, 0, 7);
        add(readFromFiles, 0, 8);
        add(clear, 0, 9);

        add(lMessage, 1, 2);
        add(message, 1, 3, 3, 3);
        add(lEncryptedMessage, 1, 6);
        add(encryptedMessage, 1, 7, 3, 3);
        add(lKeys, 1, 10);
        add(keyPair, 1, 11, 3, 1);

    }

    public void setupEventHandlers() {
        selectDir.setOnAction(event -> {
            File dir = directoryChooser.showDialog(null);
            pkFile = new File(dir, "pk.txt").getPath();
            skFile = new File(dir, "sk.txt").getPath();
            chiffreFile = new File(dir, "chiffre.txt").getPath();
            messageFile = new File(dir, "text.txt").getPath();

            lpkFile.textProperty().setValue(lpkFile.getText() + pkFile);
            lskFile.textProperty().setValue(lskFile.getText() + skFile);
            lchiffreFile.textProperty().setValue(lchiffreFile.getText() + chiffreFile);
            lmessageFile.textProperty().setValue(lmessageFile.getText() + messageFile);
        });

        genKeyPair.setOnAction(event -> {
            rsaKeyPair = RSA.genRSAKeyPair(keySize.getValue());
            keyPair.setText("Pk: (" + rsaKeyPair.getPk().x + "," + rsaKeyPair.getPk().y + ")\n" +
                    "Sk: (" + rsaKeyPair.getSk().x + "," + rsaKeyPair.getSk().y + ")\n");
        });

        encryptMessage.setOnAction(event -> {
            encryptedMessage.setText("");
            BigInteger[] encrypted = RSA.encryptMessage(message.getText(), rsaKeyPair.getPk());
            for (int i = 0; i < encrypted.length; i++) {
                if (i < encrypted.length - 1) {
                    encryptedMessage.setText(encryptedMessage.getText() + encrypted[i].toString() + ",");
                } else {
                    encryptedMessage.setText(encryptedMessage.getText() + encrypted[i].toString());
                }
            }
        });

        decryptMessage.setOnAction(event -> {
            String encryptedString = encryptedMessage.getText();
            message.setText("");
            String[] strArray = encryptedString.split(",");
            BigInteger[] encrypted = new BigInteger[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                encrypted[i] = new BigInteger(strArray[i]);
            }
            String decrypted = RSA.decrypttMessage(encrypted, rsaKeyPair.getSk());
            message.setText(decrypted);

        });

        writeToFiles.setOnAction(event -> {
            writeFile(pkFile, "(" + rsaKeyPair.getPk().x + "," + rsaKeyPair.getPk().y + ")");
            writeFile(skFile, "(" + rsaKeyPair.getSk().x + "," + rsaKeyPair.getSk().y + ")");
            writeFile(messageFile, message.getText());
            writeFile(chiffreFile, encryptedMessage.getText());
        });

        readFromFiles.setOnAction(event ->
        {
            if(Files.exists(Paths.get(pkFile))){
                try {
                    String[] pk = Files.readString(Paths.get(pkFile)).split(",");
                    rsaKeyPair.setPk(new Tuple<>(new BigInteger(pk[0].replace("(","")),new BigInteger(pk[1].replace(")",""))));
                    keyPair.setText("Pk: (" + rsaKeyPair.getPk().x + "," + rsaKeyPair.getPk().y + ")\n" +
                            "Sk: (" + rsaKeyPair.getSk().x + "," + rsaKeyPair.getSk().y + ")\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(Files.exists(Paths.get(skFile))){
                try {
                    String[] sk = Files.readString(Paths.get(skFile)).split(",");
                    rsaKeyPair.setSk(new Tuple<>(new BigInteger(sk[0].replace("(","")),new BigInteger(sk[1].replace(")",""))));
                    keyPair.setText("Pk: (" + rsaKeyPair.getPk().x + "," + rsaKeyPair.getPk().y + ")\n" +
                            "Sk: (" + rsaKeyPair.getSk().x + "," + rsaKeyPair.getSk().y + ")\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(Files.exists(Paths.get(chiffreFile))){
                try {
                    encryptedMessage.setText(Files.readString(Paths.get(chiffreFile)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(Files.exists(Paths.get(messageFile))){
                try {
                    message.setText(Files.readString(Paths.get(messageFile)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        clear.setOnAction(event ->
        {
            message.setText("");
            encryptedMessage.setText("");
            keyPair.setText("");
        });
    }


    private void writeFile(String path, String content) {
        try {
            File f = new File(path);
            FileOutputStream os = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            Writer writer = new BufferedWriter(osw);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
