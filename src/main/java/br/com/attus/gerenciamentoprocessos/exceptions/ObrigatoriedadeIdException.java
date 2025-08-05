package br.com.attus.gerenciamentoprocessos.exceptions;

public class ObrigatoriedadeIdException extends RuntimeException {
    public ObrigatoriedadeIdException() {
        super("O 'id' é obrigatório constar no corpo da requisição para alteração.");
    }
}
