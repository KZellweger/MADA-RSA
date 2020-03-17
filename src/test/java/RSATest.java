import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import static org.junit.Assert.*;

public class RSATest {

    @Test
    public void euklid(){
        //given
        Random rand = new SecureRandom();
        BigInteger p = RSA.genPrime(1024);
        BigInteger q = RSA.genPrime(1024);
        while (p.equals(q)) {
            q = RSA.genPrime(1024);
        }
        //When
        BigInteger n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537);
        BigInteger d = RSA.euklid(phiN, e);
        //Then
        assertEquals(BigInteger.ONE,e.multiply(d).mod(phiN));
    }

    @Test
    public void fastExponentiation()
    {
        //given
        BigInteger e = BigInteger.valueOf(13);
        BigInteger x = BigInteger.valueOf(7);
        BigInteger n = BigInteger.valueOf(11);
        BigInteger e1 = BigInteger.valueOf(25);
        BigInteger x1 = BigInteger.valueOf(9);
        BigInteger n1 = BigInteger.valueOf(11);
        BigInteger e2 = BigInteger.valueOf(36);
        BigInteger x2 = BigInteger.valueOf(5);
        BigInteger n2 = BigInteger.valueOf(11);
        BigInteger e3 = BigInteger.valueOf(20);
        BigInteger x3 = BigInteger.valueOf(8);
        BigInteger n3 = BigInteger.valueOf(30);
        BigInteger e4 = BigInteger.valueOf(13);
        BigInteger x4 = BigInteger.valueOf(6);
        BigInteger n4 = BigInteger.valueOf(77);

        //when
        RSA.RSAKeyPair keyPair = new RSA.RSAKeyPair(e,BigInteger.TEN,n);
        RSA.RSAKeyPair keyPair1 = new RSA.RSAKeyPair(e1,BigInteger.TEN,n1);
        RSA.RSAKeyPair keyPair2 = new RSA.RSAKeyPair(e2,BigInteger.TEN,n2);
        RSA.RSAKeyPair keyPair3 = new RSA.RSAKeyPair(e3,BigInteger.TEN,n3);
        RSA.RSAKeyPair keyPair4 = new RSA.RSAKeyPair(e4,BigInteger.TEN,n4);
        //then
        assertEquals(BigInteger.valueOf(2),RSA.fastExponentiation(x,keyPair.getPk()));
        assertEquals(BigInteger.valueOf(1),RSA.fastExponentiation(x1,keyPair1.getPk()));
        assertEquals(BigInteger.valueOf(5),RSA.fastExponentiation(x2,keyPair2.getPk()));
        assertEquals(BigInteger.valueOf(16),RSA.fastExponentiation(x3,keyPair3.getPk()));
        assertEquals(BigInteger.valueOf(62),RSA.fastExponentiation(x4,keyPair4.getPk()));
    }

    @Test
    public void encryptMessageTest(){
        //given
        String message = Character.toString(6);
        RSA.RSAKeyPair keyPair = new RSA.RSAKeyPair(BigInteger.valueOf(33),BigInteger.ONE,BigInteger.valueOf(77));

        //When
        BigInteger[] encrypted = RSA.encryptMessage(message,keyPair.getPk());

        //Then
        BigInteger[] expected = {BigInteger.valueOf(62)};
        assertArrayEquals(expected,encrypted);
    }


    @Test
    public void RSAImplementationTest(){
        //given
        String message = "Hello RSA";
        RSA.RSAKeyPair keyPair = RSA.genRSAKeyPair(1024);

        //When
        BigInteger[] encrypted = RSA.encryptMessage(message,keyPair.getPk());

        //Then
        assertEquals(message,RSA.decrypttMessage(encrypted,keyPair.getSk()));
    }
}
