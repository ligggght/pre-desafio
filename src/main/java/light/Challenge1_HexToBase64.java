package light;

import java.util.Base64;

public class Challenge1_HexToBase64 {

    public static String convert(String hex) {
        byte[] bytes = hexToBytes(hex);

        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        if (len % 2 != 0) {
            hex = "0" + hex; // Pad with leading zero if odd length
            System.out.println("Hex string was odd length, padded with leading zero: " + hex);
        }

        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int byteValue = Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[i / 2] = (byte) byteValue;
        }
        return bytes;
    }

    public static void main(String[] args) {
        String hex = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        String base64 = convert(hex);
        System.out.println("Hex: " + hex);
        System.out.println("Base64: " + base64);
    }
}
