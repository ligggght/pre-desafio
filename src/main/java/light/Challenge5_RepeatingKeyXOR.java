package light;

public class Challenge5_RepeatingKeyXOR {

        public static String encryptRepeatingKeyXOR(String text, String key) {
        /*
         * @param text Texto a ser criptografado
         * @param key Chave de criptografia
         * @return String resultante da criptografia XOR com chave repetida
         */
        byte[] textBytes = stringToBytes(text);
        byte[] keyBytes = stringToBytes(key);
        byte[] encrypted = new byte[textBytes.length];

        for (int i = 0; i < textBytes.length; i++) {
            encrypted[i] = (byte) (textBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return Challenge2_FixedXOR.bytesToHex(encrypted);
    }

    private static byte[] stringToBytes(String str) {
        /*
         * @param str String a ser convertida para bytes
         * @return Array de bytes representando os valores ASCII dos caracteres da string
         */
        byte[] result = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i); // valor ASCII
        }
        return result;
    }

    public static void main(String[] args) {
        String text = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
        String key = "ICE";

        String encryptedHex = encryptRepeatingKeyXOR(text, key);
        System.out.println(encryptedHex);
    }
}
