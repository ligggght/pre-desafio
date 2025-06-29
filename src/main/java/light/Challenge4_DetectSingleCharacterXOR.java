package light;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Challenge4_DetectSingleCharacterXOR {
    
    private static final String INPUT_FILE = "input/4.txt";
    
    // Classe para armazenar resultado de uma linha com seu número
    public static class LineResult {
        public int lineNumber;
        public DecryptResult decryptResult;
        
        public LineResult(int lineNumber, DecryptResult decryptResult) {
            this.lineNumber = lineNumber;
            this.decryptResult = decryptResult;
        }
        
        @Override
        public String toString() {
            return String.format("Linha %3d | %s", lineNumber, decryptResult.toString());
        }
    }
    
    // // Analisa uma string hex aplicando análise de frequência para todas as 256 chaves
    // public static DecryptResult analyzeHexString(String hexString) {
    //     try {
    //         byte[] encrypted = Challenge1_HexToBase64.hexToBytes(hexString.trim());
    //         DecryptResult best = new DecryptResult(0, "", Double.NEGATIVE_INFINITY, "FREQUENCY");
            
    //         // Testa todas as 256 chaves possíveis
    //         for (int key = 0; key < 256; key++) {
    //             String decrypted = Challenge3_SingleByteXORCipher.decrypt(encrypted, (byte) key);
    //             double score = Challenge3_SingleByteXORCipher.calculateFrequencyScore(decrypted);
                
    //             if (score > best.score) {
    //                 best = new DecryptResult(key, decrypted, score, "FREQUENCY");
    //             }
    //         }
    //         return best;
    //     } catch (Exception e) {
    //         return new DecryptResult(0, "", 0, "ERROR");
    //     }
    // }
    
    // Encontra a linha do arquivo que possui o maior score de frequência
    public static LineResult findEncryptedLine() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
            LineResult best = null;
            
            // Analisa cada linha do arquivo
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    DecryptResult result = analyzeHexString(line);
                    LineResult lineResult = new LineResult(i + 1, result);
                    
                    // Mantém a linha com melhor score
                    if (best == null || result.score > best.decryptResult.score) {
                        best = lineResult;
                    }
                }
            }
            
            return best;
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            return null;
        }
    }
    
    // Analisa todas as linhas e retorna lista ordenada por score
    public static List<LineResult> analyzeAllLines() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
            List<LineResult> results = new ArrayList<>();
            
            // Processa cada linha
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    DecryptResult result = analyzeHexString(line);
                    results.add(new LineResult(i + 1, result));
                }
            }
            
            // Ordena por score (maior primeiro)
            results.sort((a, b) -> Double.compare(b.decryptResult.score, a.decryptResult.score));
            return results;
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== DETECT SINGLE-CHARACTER XOR CIPHER ===");
        System.out.println("Arquivo: " + INPUT_FILE);
        System.out.println();
        
        // Encontra a linha criptografada e mede o tempo
        long startTime = System.nanoTime();
        LineResult result = findEncryptedLine();
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;
        
        if (result != null) {
            System.out.println("Linha criptografada encontrada:");
            System.out.println(result);
            System.out.printf("\nTempo de execução: %.2f ms%n", timeMs);
            
            // Mostra os top 5 candidatos para comparação
            System.out.println("\n=== TOP 5 CANDIDATOS ===");
            List<LineResult> top5 = analyzeAllLines();
            for (int i = 0; i < Math.min(5, top5.size()); i++) {
                System.out.printf("%d. %s%n", i+1, top5.get(i));
            }
        } else {
            System.out.println("Erro ao processar o arquivo.");
        }
    }
}
