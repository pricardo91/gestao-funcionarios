package br.com.first.gestaofuncionarios.domain;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {

    private Long idFuncionario;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 carateres")
    private String nome;

    @NotBlank(message = "O cargo do funcionário é obrigatório")
    private String funcao;

    @NotNull(message = "O salário do funcionário é obrigatório")
    @Positive(message = "O salário deve ser positivo")
    @Min(value = 1621, message = "O valor não atinge o salário mínimo vigênte no Brasil (R$1.621,00)")
    private BigDecimal salario;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 3, max = 250, message = "O endereço deve conter entre 3 à 250 caracteres!")
    private String endereco;
}
