package light;

public class Challenge2_FixedXOR {
    public static String fixedXOR(String hex1, String hex2) {
        if (hex1.length() != hex2.length()) {
            throw new IllegalArgumentException("Hex strings must be of the same length");
        }

        byte[] bytes1 = Challenge1_HexToBase64.hexToBytes(hex1);
        byte[] bytes2 = Challenge1_HexToBase64.hexToBytes(hex2);

        byte[] result = new byte[bytes1.length];
        for (int i = 0; i < bytes1.length; i++) {
            result[i] = (byte) (bytes1[i] ^ bytes2[i]);
        }

        return bytesToHex(result);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hexString.append('0'); // Pad with leading zero
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        String hex1 = "1c0111001f010100061a024b53535009181c";
        String hex2 = "686974207468652062756c6c277320657965";
        String expected = "746865206b696420646f6e277420706c6179";

        try {
            String result = fixedXOR(hex1, hex2);
            System.out.println("Input Hex 1: " + hex1);
            System.out.println("Input Hex 2: " + hex2);
            System.out.println("XOR Result: " + result);
            System.out.println("Expected: " + expected);
            System.out.println("Test " + (result.equals(expected) ? "passed!" : "failed!"));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}