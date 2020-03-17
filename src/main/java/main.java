import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class main {

    public static void main(String[] args) {

        /*
         * Start configuration Section
         * If the file location is not stored in ProjectResources please provide absolute Paths
         */
        final int KeySize = 1024;
        final String PublicKeyFile = "pk.txt";
        final String PrivateKeyFile = "sk.txt";
        final String EncryptedMessageFile = "chiffre.txt";
        final String MessageFile = "message.txt";

        /*
         * End configuration Section
         */

        RSA.RSAKeyPair keyPair = RSA.genRSAKeyPair(KeySize);

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(PublicKeyFile, StandardCharsets.UTF_8));
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
