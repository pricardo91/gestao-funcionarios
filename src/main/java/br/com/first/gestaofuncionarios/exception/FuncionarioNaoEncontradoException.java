package br.com.first.gestaofuncionarios.exception;

import br.com.first.gestaofuncionarios.handler.APIException;
import org.springframework.http.HttpStatus;

public class FuncionarioNaoEncontradoException extends APIException {

    public FuncionarioNaoEncontradoException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                "Funcionário não encontrado com o ID: " + id,
                "Verifique se o ID informado está correto e tente novamente",
                null
        );
    }
}