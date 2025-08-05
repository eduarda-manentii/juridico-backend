package br.com.attus.gerenciamentoprocessos.exceptions;

public class DuplicidadeDocumentoException extends RuntimeException {
    public DuplicidadeDocumentoException(String message) {
        super("Documento já existente.");
    }
}
