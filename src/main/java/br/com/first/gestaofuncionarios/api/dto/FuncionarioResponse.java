package br.com.first.gestaofuncionarios.api.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class FuncionarioResponse {

    private Long idFuncionario;
    private String nome;
    private String funcao;
    private BigDecimal salario;
    private String telefone;
    private String endereco;
}