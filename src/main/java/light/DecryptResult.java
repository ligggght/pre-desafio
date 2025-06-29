package light;

/*
 * Classe para armazenar o resultado da análise de uma string criptografada
 * encapsulando a chave, o texto decifrado, a pontuação e o método utilizado.
 */
public class DecryptResult {
    public int key;
    public String plaintext;
    public double score;
    public String method;
    
    public DecryptResult(int key, String plaintext, double score, String method) {
        this.key = key;
        this.plaintext = plaintext;
        this.score = score;
        this.method = method;
    }
    
    @Override
    public String toString() {
        return String.format("Chave: %3d (0x%02X) | Pontuacao: %8.2f | Método: %-15s | Texto: %s", 
                            key, key, score, method, plaintext);
    }
}
