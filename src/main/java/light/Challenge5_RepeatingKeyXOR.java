package light;

import java.nio.charset.StandardCharsets;

public class Challenge5_RepeatingKeyXOR {
        public static String encryptRepeatingKeyXOR(String text, String key) {
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = new byte[textBytes.length];

        for (int i = 0; i < textBytes.length; i++) {
            encrypted[i] = (byte) (textBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return Challenge2_FixedXOR.bytesToHex(encrypted);
    }

    public static void main(String[] args) {
        String text = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
        String key = "ICE";

        String encryptedHex = encryptRepeatingKeyXOR(text, key);
        System.out.println(encryptedHex);
    }
}
