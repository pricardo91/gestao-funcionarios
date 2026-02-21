package br.com.first.gestaofuncionarios.controller;

import br.com.first.gestaofuncionarios.domain.Funcionario;
import br.com.first.gestaofuncionarios.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/v1/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<Funcionario> criar(@RequestBody @Valid Funcionario funcionarioNovo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(funcionarioService.adicionar(funcionarioNovo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(funcionarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Funcionario>> buscarTodos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(funcionarioService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id,
                                                 @RequestBody @Valid Funcionario funcionario) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(funcionarioService.atualizar(id, funcionario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
