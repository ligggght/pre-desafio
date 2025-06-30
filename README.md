## Repositório Público

Repositório disponibilizado publicamente **apenas após a entrega**:

[https://github.com/ligggght/pre-desafio](https://github.com/ligggght/pre-desafio)

# Pré-Desafio - Processo Seletivo PBAD / LabSEC

Este repositório contém minha solução para o pré-desafio do processo seletivo.

## Estrutura

- `src/main/java/light/`: Código-fonte dos desafios
- `input/`: Arquivos de entrada utilizados nos desafios
- `pom.xml`: Gerenciador de dependências (Maven)

## Como executar

Compile com Maven e execute com Maven ou com o próprio Java:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="light.Challenge3_SingleByteXORCipher"
```
ou
```bash
mvn compile
java -cp target/classes light.Challenge1_HexToBase64
