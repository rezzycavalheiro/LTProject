package exception;

import model.Erro;

import java.util.List;

public class ValidationException extends RuntimeException {

    private List<Erro> erros;

    public ValidationException(List<Erro> erros) {
        this.erros = erros;
    }

    public List<Erro> getErros() {
        return erros;
    }
}
