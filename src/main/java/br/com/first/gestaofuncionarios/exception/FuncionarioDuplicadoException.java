package br.com.first.gestaofuncionarios.exception;

import br.com.first.gestaofuncionarios.handler.APIException;
import org.springframework.http.HttpStatus;

public class FuncionarioDuplicadoException extends APIException {

    public FuncionarioDuplicadoException(String telefone) {
        super(
                HttpStatus.CONFLICT,
                "Funcionário já cadastrado",
                "Já existe um funcionário com o telefone: " + telefone,
                null
        );
    }
}
