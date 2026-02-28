package br.com.first.gestaofuncionarios.api.mapper;

import br.com.first.gestaofuncionarios.api.dto.FuncionarioCreateRequest;
import br.com.first.gestaofuncionarios.api.dto.FuncionarioResponse;
import br.com.first.gestaofuncionarios.api.dto.FuncionarioUpdateRequest;
import br.com.first.gestaofuncionarios.domain.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

    public Funcionario toDomain(Long id, FuncionarioUpdateRequest dto) {

        return Funcionario.builder()
                .idFuncionario(id)
                .nome(dto.getNome())
                .funcao(dto.getFuncao())
                .salario(dto.getSalario())
                .telefone(dto.getTelefone())
                .endereco(dto.getEndereco())
                .build();
    }

    public Funcionario toDomain(FuncionarioCreateRequest dto){
        return Funcionario.builder()
                .nome(dto.getNome())
                .funcao(dto.getFuncao())
                .salario(dto.getSalario())
                .telefone(dto.getTelefone())
                .endereco(dto.getEndereco())
                .build();
    }

    public FuncionarioResponse toResponse(Funcionario funcionario){

        return new FuncionarioResponse(funcionario.getIdFuncionario(),
                funcionario.getNome(),
                funcionario.getFuncao(),
                funcionario.getSalario(),
                funcionario.getTelefone(),
                funcionario.getEndereco());
    }
}
