package br.com.first.gestaofuncionarios.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FuncionarioUpdateRequest {

    @Schema(description = "Nome completo do funcionário", example = "Ricardo Silva")
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 carateres")
    private String nome;

    @Schema(example = "Analista de Sistemas")
    @NotBlank(message = "O cargo do funcionário é obrigatório")
    @Size(min = 3, max = 100, message = "A função deve conter entre 3 e 100 carateres")
    private String funcao;

    @Schema(example = "3566.25")
    @NotNull(message = "O salário do funcionário é obrigatório")
    @Positive(message = "O salário deve ser positivo")
    @DecimalMin(value = "1621.00", message = "O valor não atinge o salário mínimo vigênte no Brasil (R$1.621,00)")
    private BigDecimal salario;

    @Schema(description = "Número de telefone do usuário, não pode ser repetido!", example = "11982971929")
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;

    @Schema(example = "Rua América Latina, 135 - São Paulo/SP")
    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 3, max = 250, message = "O endereço deve conter entre 3 à 250 caracteres!")
    private String endereco;

}
