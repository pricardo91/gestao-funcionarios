package br.com.first.gestaofuncionarios.api.controller;

import br.com.first.gestaofuncionarios.api.dto.FuncionarioCreateRequest;
import br.com.first.gestaofuncionarios.api.dto.FuncionarioResponse;
import br.com.first.gestaofuncionarios.api.dto.FuncionarioUpdateRequest;
import br.com.first.gestaofuncionarios.api.mapper.FuncionarioMapper;
import br.com.first.gestaofuncionarios.domain.Funcionario;
import br.com.first.gestaofuncionarios.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final FuncionarioMapper funcionarioMapper;

    @PostMapping
    public ResponseEntity<FuncionarioResponse> criar(@RequestBody @Valid FuncionarioCreateRequest funcionarioNovo) {
        Funcionario funcionario = funcionarioMapper.toDomain(funcionarioNovo);
        Funcionario salvo = funcionarioService.adicionar(funcionario);
        FuncionarioResponse funcionarioResponse = funcionarioMapper.toResponse(salvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(funcionarioMapper.toResponse(funcionario));
    }

    @GetMapping
    public ResponseEntity<Collection<FuncionarioResponse>> buscarTodos() {
        Collection<FuncionarioResponse> listaFuncionarios = funcionarioService.listarTodos()
                .stream()
                .map(funcionarioMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listaFuncionarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid FuncionarioUpdateRequest request) {

        Funcionario funcionario = funcionarioMapper.toDomain(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(funcionarioMapper.toResponse(funcionarioService.atualizar(id, funcionario)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
