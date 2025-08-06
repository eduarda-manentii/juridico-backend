package br.com.attus.gerenciamentoprocessos.exceptions;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException() {
        super("Token inválido");
    }
}
