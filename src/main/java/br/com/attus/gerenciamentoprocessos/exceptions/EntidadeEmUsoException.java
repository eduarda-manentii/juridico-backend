package br.com.attus.gerenciamentoprocessos.exceptions;

public class EntidadeEmUsoException extends RuntimeException {
    public EntidadeEmUsoException(String message) {
        super(message);
    }
}
