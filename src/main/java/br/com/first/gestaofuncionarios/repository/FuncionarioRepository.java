package br.com.first.gestaofuncionarios.repository;

import br.com.first.gestaofuncionarios.domain.Funcionario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class FuncionarioRepository {

    private final Map<Long, Funcionario> funcionarios = new ConcurrentHashMap<>();
    private final AtomicLong idAutomatico = new AtomicLong(1);

    public Funcionario save(Funcionario funcionario) {
        if (funcionario.getIdFuncionario() == null) {
            funcionario.setIdFuncionario(idAutomatico.getAndIncrement());
        }
        funcionarios.put(funcionario.getIdFuncionario(), funcionario);

        return funcionario;
    }

    public Optional<Funcionario> findById(Long id) {
        return Optional.ofNullable(funcionarios.get(id));
    }

    public Collection<Funcionario> findAll() {
        return funcionarios.values();
    }

    public void deleteById(Long id) {
        funcionarios.remove(id);
    }

    public boolean existsById(Long id) {
        return funcionarios.containsKey(id);
    }

    public boolean existsByTelefone(String telefone) {
        return funcionarios.values().stream()
                .anyMatch(f -> f.getTelefone().equals(telefone));
    }
}
