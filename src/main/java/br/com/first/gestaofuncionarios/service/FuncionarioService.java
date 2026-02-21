package br.com.first.gestaofuncionarios.service;

import br.com.first.gestaofuncionarios.domain.Funcionario;
import br.com.first.gestaofuncionarios.exception.FuncionarioDuplicadoException;
import br.com.first.gestaofuncionarios.exception.FuncionarioNaoEncontradoException;
import br.com.first.gestaofuncionarios.handler.APIException;
import br.com.first.gestaofuncionarios.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;

    public Funcionario adicionar(Funcionario funcionario) {
        if(funcionarioRepository.existsByTelefone(funcionario.getTelefone())){
            throw new FuncionarioDuplicadoException(funcionario.getTelefone());
        }

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        log.info("Funcionário cadastrado com sucesso: ID: {} , Nome: {}", funcionarioSalvo.getIdFuncionario(), funcionarioSalvo.getNome());
        return funcionarioSalvo;
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
    }

    public Collection<Funcionario> listarTodos() {
        Collection<Funcionario> funcionarios = funcionarioRepository.findAll();
        log.debug("Listando {} funcionário(s)", funcionarios.size());
        return funcionarios;
    }

    public Funcionario atualizar(Long id, Funcionario funcionarioAtualizado) {
        Funcionario existente = buscarPorId(id);

        if(!existente.getTelefone().equals(funcionarioAtualizado.getTelefone())
        && funcionarioRepository.existsByTelefone(funcionarioAtualizado.getTelefone())){
            throw new FuncionarioDuplicadoException(funcionarioAtualizado.getTelefone());
        }

        existente.setNome(funcionarioAtualizado.getNome());
        existente.setFuncao(funcionarioAtualizado.getFuncao());
        existente.setSalario(funcionarioAtualizado.getSalario());
        existente.setTelefone(funcionarioAtualizado.getTelefone());
        existente.setEndereco(funcionarioAtualizado.getEndereco());

        funcionarioRepository.save(existente);
        log.info("Funcionário atualizado - ID: {}, Nome: {}", existente.getIdFuncionario(), existente.getNome());
        return existente;
    }

    public void deletar(Long id) {
        Funcionario funcionario = buscarPorId(id);

        if ("Gerente".equalsIgnoreCase(funcionario.getFuncao())){
            throw APIException.build(
                    HttpStatus.FORBIDDEN,
                    "Exclusão não permitida",
                    "Funcionários com função 'Gerente' não podem ser excluídos diretamente"
            );
        }
        funcionarioRepository.deleteById(id);
        log.info("Funcionário deletado - ID: {}, Nome: {}", id, funcionario.getNome());
    }
}
