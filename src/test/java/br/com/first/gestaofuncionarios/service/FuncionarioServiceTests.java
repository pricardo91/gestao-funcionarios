package br.com.first.gestaofuncionarios.service;

import br.com.first.gestaofuncionarios.domain.Funcionario;
import br.com.first.gestaofuncionarios.exception.FuncionarioDuplicadoException;
import br.com.first.gestaofuncionarios.repository.FuncionarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTests {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private Funcionario funcionario;

    @BeforeEach
    void func() {
        funcionario = Funcionario.builder()
                .nome("Ricardo Silva")
                .salario(BigDecimal.valueOf(7400.50))
                .endereco("Rua Silvano Mendonça")
                .funcao("Desenvolvedor")
                .telefone("11982971929")
                .build();
    }

    @Test
    void deveSalvarQuandoSaveForChamado() {
        //modelo AAA
        //Arrange

        System.out.println("deveSalvarQuandoSaveForChamado | " + funcionario);
        when(funcionarioRepository.existsByTelefone(funcionario.getTelefone())).thenReturn(false);
        when(funcionarioRepository.save(any(Funcionario.class)))
                .thenReturn(funcionario);

        //Act
        Funcionario funcionarioSalvo = funcionarioService.adicionar(funcionario);

        //Assert
        Assertions.assertNotNull(funcionarioSalvo);
        Assertions.assertEquals("Ricardo Silva", funcionarioSalvo.getNome());

        verify(funcionarioRepository, times(1)).existsByTelefone(funcionario.getTelefone());
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void deveLancarExcessaoQuandoTelefoneJaExiste() {

        when(funcionarioRepository.existsByTelefone(funcionario.getTelefone())).thenReturn(true);

        //Act + assert

        FuncionarioDuplicadoException funcionarioDuplicadoException = assertThrows(
                FuncionarioDuplicadoException.class,
                () -> funcionarioService.adicionar(funcionario)
        );

        Assertions.assertTrue(funcionarioDuplicadoException.getBodyException().getDescription().contains(funcionario.getTelefone()));
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), funcionarioDuplicadoException.getBodyException().getStatus());

        verify(funcionarioRepository, times(1)).existsByTelefone(funcionario.getTelefone());
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }
}
