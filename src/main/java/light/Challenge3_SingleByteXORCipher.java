package light;

import java.util.*;

public class Challenge3_SingleByteXORCipher {

    // Frequências das letras em inglês em porcentagem (consultar as referências)
    private static final Map<Character, Double> ENGLISH_FREQUENCIES = new HashMap<>();
    static {
        ENGLISH_FREQUENCIES.put('a', 8.12); ENGLISH_FREQUENCIES.put('b', 1.49);
        ENGLISH_FREQUENCIES.put('c', 2.71); ENGLISH_FREQUENCIES.put('d', 4.32);
        ENGLISH_FREQUENCIES.put('e', 12.02); ENGLISH_FREQUENCIES.put('f', 2.30);
        ENGLISH_FREQUENCIES.put('g', 2.03); ENGLISH_FREQUENCIES.put('h', 5.92);
        ENGLISH_FREQUENCIES.put('i', 7.31); ENGLISH_FREQUENCIES.put('j', 0.10);
        ENGLISH_FREQUENCIES.put('k', 0.69); ENGLISH_FREQUENCIES.put('l', 3.98);
        ENGLISH_FREQUENCIES.put('m', 2.61); ENGLISH_FREQUENCIES.put('n', 6.95);
        ENGLISH_FREQUENCIES.put('o', 7.68); ENGLISH_FREQUENCIES.put('p', 1.82);
        ENGLISH_FREQUENCIES.put('q', 0.11); ENGLISH_FREQUENCIES.put('r', 6.02);
        ENGLISH_FREQUENCIES.put('s', 6.28); ENGLISH_FREQUENCIES.put('t', 9.10);
        ENGLISH_FREQUENCIES.put('u', 2.88); ENGLISH_FREQUENCIES.put('v', 1.11);
        ENGLISH_FREQUENCIES.put('w', 2.09); ENGLISH_FREQUENCIES.put('x', 0.17);
        ENGLISH_FREQUENCIES.put('y', 2.11); ENGLISH_FREQUENCIES.put('z', 0.07);
        ENGLISH_FREQUENCIES.put(' ', 13.0); // Espaço é muito comum
    }

    private static final String TARGET_HEX = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";

    // Método 1: Análise de frequência de caracteres
    public static DecryptedResult solveByFrequencyAnalysis(String TARGET_HEX) {
        /*
         * @param TARGET_HEX: String hexadecimal criptografada
         * @return: DecryptedResult com a melhor chave, texto descriptografado e score
         */
        byte[] encrypted = Challenge1_HexToBase64.hexToBytes(TARGET_HEX);
        DecryptedResult best = new DecryptedResult(0, "", Double.NEGATIVE_INFINITY, "FREQUENCY");
        
        for (int key = 0; key < 256; key++) {
            String decrypted = decrypt(encrypted, (byte) key);
            double score = calculateFrequencyScore(decrypted);
            
            if (score > best.score) {
                best = new DecryptedResult(key, decrypted, score, "FREQUENCY");
            }
        }
        return best;
    }

    // Método 2: Busca por palavras comuns em inglês (ver referências)
    public static DecryptedResult solveByCommonWords(String TARGET_HEX) {
        byte[] encrypted = Challenge1_HexToBase64.hexToBytes(TARGET_HEX);
        String[] commonWords = {
            "the", "be", "to", "of", "and", "a", "in", "that", "have", "I",
            "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
            "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
            "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
            "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
            "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
            "person", "into", "year", "your", "good", "some", "could", "them", "see", "other",
            "than", "then", "now", "look", "only", "come", "its", "over", "think", "also",
            "back", "after", "use", "two", "how", "our", "work", "first", "well", "way",
            "even", "new", "want", "because", "any", "these", "give", "day", "most", "us"
        };
        
        DecryptedResult best = new DecryptedResult(0, "", 0, "COMMON_WORDS");
        
        for (int key = 0; key < 256; key++) {
            String decrypted = decrypt(encrypted, (byte) key).toLowerCase();
            int wordCount = 0;
            
            for (String word : commonWords) {
                if (decrypted.contains(word)) {
                    wordCount++;
                }
            }
            
            if (wordCount > best.score) {
                best = new DecryptedResult(key, decrypt(encrypted, (byte) key), wordCount, "COMMON_WORDS");
            }
        }
        return best;
    }

    // Método 3: Brute force com visualização
    public static List<DecryptedResult> bruteForceSolution(String TARGET_HEX) {
        byte[] encrypted = Challenge1_HexToBase64.hexToBytes(TARGET_HEX);
        List<DecryptedResult> results = new ArrayList<>();
        
        for (int key = 0; key < 256; key++) {
            String decrypted = decrypt(encrypted, (byte) key);
            double freqScore = calculateFrequencyScore(decrypted);
            results.add(new DecryptedResult(key, decrypted, freqScore, "BRUTE_FORCE"));
        }
        
        results.sort((a, b) -> Double.compare(b.score, a.score));
        return results;
    }

    // Função auxiliar para descriptografar usando XOR
    public static String decrypt(byte[] encrypted, byte key) {
        /*
         * @param encrypted: Array de bytes criptografados
         * @param key: Byte da chave de criptografia (0-255)
         * @return: Texto descriptografado como String
         */
        StringBuilder result = new StringBuilder();
        for (byte b : encrypted) {
            result.append((char) (b ^ key));
        }
        return result.toString();
    }

    // Calcula score baseado na frequência de letras em inglês
    public static double calculateFrequencyScore(String text) {
        /*
         * @param text: Texto descriptografado
         * @return: Score baseado na frequência de letras comparado com o inglês
         */
        Map<Character, Integer> charCount = new HashMap<>();
        int totalChars = 0;
        
        // Conta apenas caracteres válidos
        for (char c : text.toLowerCase().toCharArray()) {
            if (Character.isLetter(c) || c == ' ') {
                charCount.put(c, charCount.getOrDefault(c, 0) + 1);
                totalChars++;
            }
        }
        
        if (totalChars == 0) return 0; // Evita divisão por zero
        
        double score = 0.0;
        
        // Chi-squared inverso (quanto menor a diferença, maior o score)
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            char c = entry.getKey();
            double observedFreq = (entry.getValue() * 100.0) / totalChars;
            Double expectedFreq = ENGLISH_FREQUENCIES.get(c);
            
            if (expectedFreq != null) {
                // Soma pontos - quanto mais próximo da frequência esperada, melhor
                double difference = Math.abs(observedFreq - expectedFreq);
                score += Math.max(0, expectedFreq - difference);
            }
        }
        
        return score;
    }

    // Interface para o usuário interagir e decidir como quer resolver o problema 
    public static void runInteractiveInterface() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("======================================================");
        System.out.println("          SINGLE-BYTE XOR CIPHER SOLVER              ");
        System.out.println("======================================================");
        System.out.println("Target: " + TARGET_HEX);
        System.out.println();
        
        while (running) {
            System.out.println("-----------------------------------------------------");
            System.out.println("                    MENU PRINCIPAL                  ");
            System.out.println("-----------------------------------------------------");
            System.out.println(" 1. Análise de Frequência de Caracteres             ");
            System.out.println(" 2. Busca por Palavras Comuns                       ");
            System.out.println(" 3. Brute Force (Top 5 resultados)                  ");
            System.out.println(" 4. Comparar Todos os Métodos                       ");
            System.out.println(" 5. Testar uma Chave Específica                     ");
            System.out.println(" 6. Mostrar Todas as 256 Possibilidades             ");
            System.out.println(" 0. Sair                                             ");
            System.out.println("-----------------------------------------------------");
            System.out.print("Escolha uma opção: ");
            
            int choice = scanner.nextInt();
            System.out.println();
            
            switch (choice) {
                case 1:
                    System.out.println("=== ANÁLISE DE FREQUÊNCIA DE CARACTERES ===");
                    long startTime = System.nanoTime();
                    DecryptedResult freq = solveByFrequencyAnalysis(TARGET_HEX);
                    long endTime = System.nanoTime();
                    double timeMs = (endTime - startTime) / 1_000_000.0;
                    
                    System.out.printf("%s | Tempo: %6.2f ms%n", freq, timeMs);
                    System.out.println("\nEste método analisa a frequência de letras comparando com");
                    System.out.println("as frequências típicas do inglês (e=12.02%, t=9.10%, etc.)");
                    break;
                    
                case 2:
                    System.out.println("=== BUSCA POR PALAVRAS COMUNS ===");
                    startTime = System.nanoTime();
                    DecryptedResult words = solveByCommonWords(TARGET_HEX);
                    endTime = System.nanoTime();
                    timeMs = (endTime - startTime) / 1_000_000.0;
                    
                    System.out.printf("%s | Tempo: %6.2f ms%n", words, timeMs);
                    System.out.println("\nEste método procura por palavras comuns em inglês como");
                    System.out.println("'the', 'and', 'that', 'have', etc. no texto descriptografado");
                    break;
                    
                case 3:
                    System.out.println("=== BRUTE FORCE (TOP 5 RESULTADOS) ===");
                    startTime = System.nanoTime();
                    List<DecryptedResult> brute = bruteForceSolution(TARGET_HEX);
                    endTime = System.nanoTime();
                    timeMs = (endTime - startTime) / 1_000_000.0;
                    
                    for (int i = 0; i < Math.min(5, brute.size()); i++) {
                        System.out.printf("%d. %s%n", i+1, brute.get(i));
                    }
                    System.out.printf("\nTempo total: %6.2f ms%n", timeMs);
                    System.out.println("\nEste método testa todas as 256 chaves possíveis e");
                    System.out.println("ordena os resultados pelos melhores scores");
                    break;
                    
                case 4:
                    System.out.println("=== COMPARAÇÃO DE TODOS OS MÉTODOS ===");
                    System.out.println();
                    
                    long freqStart = System.nanoTime();
                    DecryptedResult freqResult = solveByFrequencyAnalysis(TARGET_HEX);
                    long freqEnd = System.nanoTime();
                    double freqTime = (freqEnd - freqStart) / 1_000_000.0;
                    
                    long wordsStart = System.nanoTime();
                    DecryptedResult wordsResult = solveByCommonWords(TARGET_HEX);
                    long wordsEnd = System.nanoTime();
                    double wordsTime = (wordsEnd - wordsStart) / 1_000_000.0;
                    
                    long bruteStart = System.nanoTime();
                    List<DecryptedResult> bruteResults = bruteForceSolution(TARGET_HEX);
                    long bruteEnd = System.nanoTime();
                    double bruteTime = (bruteEnd - bruteStart) / 1_000_000.0;
                    DecryptedResult bruteResult = bruteResults.isEmpty() ? 
                        new DecryptedResult(0, "", 0, "BRUTE_FORCE") : bruteResults.get(0);
                    
                    System.out.printf("1. %s | Tempo: %6.2f ms%n", freqResult, freqTime);
                    System.out.printf("2. %s | Tempo: %6.2f ms%n", wordsResult, wordsTime);
                    System.out.printf("3. %s | Tempo: %6.2f ms%n", bruteResult, bruteTime);
                    
                    String fastest = freqTime <= wordsTime && freqTime <= bruteTime ? "FREQUENCY" :
                                    wordsTime <= bruteTime ? "COMMON_WORDS" : "BRUTE_FORCE";
                    double fastestTime = freqTime <= wordsTime && freqTime <= bruteTime ? freqTime :
                                    wordsTime <= bruteTime ? wordsTime : bruteTime;
                    
                    DecryptedResult bestScore = freqResult.score >= wordsResult.score && freqResult.score >= bruteResult.score ? freqResult :
                                            wordsResult.score >= bruteResult.score ? wordsResult : bruteResult;
                    
                    System.out.printf("\nMais rápido: %s (%.2f ms)%n", fastest, fastestTime);
                    System.out.printf("Maior score: %s (%.2f pontos)%n", bestScore.method, bestScore.score);
                    break;
                    
                case 5:
                    System.out.print("Digite a chave (0-255): ");
                    int testKey = scanner.nextInt();
                    if (testKey >= 0 && testKey <= 255) {
                        long testStart = System.nanoTime();
                        byte[] encrypted = Challenge1_HexToBase64.hexToBytes(TARGET_HEX);
                        String result = decrypt(encrypted, (byte) testKey);
                        double score = calculateFrequencyScore(result);
                        long testEnd = System.nanoTime();
                        double testTime = (testEnd - testStart) / 1_000_000.0;
                        
                        System.out.println("\n=== TESTE DE CHAVE ESPECÍFICA ===");
                        System.out.printf("Chave: %d (0x%02X)%n", testKey, testKey);
                        System.out.printf("Resultado: %s%n", result);
                        System.out.printf("Score: %.2f%n", score);
                        System.out.printf("Tempo: %.3f ms%n", testTime);
                        
                    } else {
                        System.out.println("Chave inválida! Use um valor entre 0 e 255.");
                    }
                    break;
                    
                case 6:
                    System.out.println("=== TODAS AS 256 POSSIBILIDADES ===");
                    System.out.print("Isso vai mostrar muita informação. Continuar? (s/n): ");
                    scanner.nextLine();
                    String confirm = scanner.nextLine().toLowerCase();
                    
                    if (confirm.equals("s") || confirm.equals("sim")) {
                        long allStart = System.nanoTime();
                        List<DecryptedResult> allResults = bruteForceSolution(TARGET_HEX);
                        long allEnd = System.nanoTime();
                        
                        System.out.println("\nResultados ordenados por score (melhores primeiro):");
                        System.out.println("Pos | Key | Score    | Text Preview");
                        System.out.println("----|-----|----------|--------------------------------");
                        
                        for (int i = 0; i < allResults.size(); i++) {
                            DecryptedResult r = allResults.get(i);
                            String preview = r.plaintext.length() > 30 ? 
                                r.plaintext.substring(0, 30) + "..." : r.plaintext;
                            preview = preview.replaceAll("[\\p{Cntrl}]", "?");
                            
                            System.out.printf("%3d | %3d | %8.2f | %s%n", 
                                            i+1, r.key, r.score, preview);
                            
                            if ((i + 1) % 20 == 0 && i < allResults.size() - 1) {
                                System.out.print("\n--- Pressione ENTER para continuar ---");
                                scanner.nextLine();
                            }
                        }
                        
                        double allTime = (allEnd - allStart) / 1_000_000.0;
                        System.out.printf("\nTempo total: %.2f ms%n", allTime);
                    }
                    break;
                    
                case 0:
                    running = false;
                    System.out.println("======================================================");
                    System.out.println("              Obrigado por usar o                    ");
                    System.out.println("          Single-Byte XOR Cipher Solver!                         ");
                    System.out.println("======================================================");
                    break;
                    
                default:
                    System.out.println("Opção inválida! Escolha um número entre 0 e 6.");
            }
            
            if (running) {
                System.out.println("\n" + "=".repeat(60) + "\n");
            }
        }
        
        scanner.close();
    }

    public static void main(String[] args) {
        runInteractiveInterface();
    }
}
