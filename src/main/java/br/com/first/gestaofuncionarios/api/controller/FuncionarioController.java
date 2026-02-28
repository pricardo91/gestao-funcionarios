package br.com.first.gestaofuncionarios.api.controller;

import br.com.first.gestaofuncionarios.api.dto.FuncionarioCreateRequest;
import br.com.first.gestaofuncionarios.api.dto.FuncionarioResponse;
import br.com.first.gestaofuncionarios.api.dto.FuncionarioUpdateRequest;
import br.com.first.gestaofuncionarios.api.mapper.FuncionarioMapper;
import br.com.first.gestaofuncionarios.domain.Funcionario;
import br.com.first.gestaofuncionarios.handler.ErrorApiResponse;
import br.com.first.gestaofuncionarios.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Funcionários",
        description = "Com os endpoints abaixo é possível cadastrar, atualizar, consultar e deletar funcionários")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final FuncionarioMapper funcionarioMapper;

    @Operation(
            summary = "Cadastrar funcionário",
            description = "Cria um novo funcionário no sistema. Não permite telefone duplicado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = FuncionarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Telefone já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<FuncionarioResponse> criar(@RequestBody @Valid FuncionarioCreateRequest funcionarioNovo) {
        Funcionario funcionario = funcionarioMapper.toDomain(funcionarioNovo);
        Funcionario salvo = funcionarioService.adicionar(funcionario);
        FuncionarioResponse funcionarioResponse = funcionarioMapper.toResponse(salvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioResponse);
    }

    @Operation(
            summary = "Buscar funcionário por ID",
            description = "Retorna os dados de um funcionário cadastrado com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado",
                    content = @Content(schema = @Schema(implementation = FuncionarioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(funcionarioMapper.toResponse(funcionario));
    }

    @Operation(
            summary = "Busca todos os funcionários",
            description = "Busca a lista completa de funcionários cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de funcionários cadastrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FuncionarioResponse.class)))
            )
    })
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

    @Operation(
            summary = "Atualiza funcionário por ID",
            description = "Atualiza os dados de um funcionário cadastrado com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = FuncionarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content())
    })
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid FuncionarioUpdateRequest request) {
        Funcionario funcionario = funcionarioMapper.toDomain(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(funcionarioMapper.toResponse(funcionarioService.atualizar(id, funcionario)));
    }

    @Operation(
            summary = "Deleta funcionario por ID",
            description = "Apaga um funcionario cadastrado com base no ID informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário excluído com sucesso",
                    content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
