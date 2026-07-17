package br.ufpb.dcx.iago.lojadejogos.backend.exception;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.ErrorResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Interceptador global de exceções da aplicação.
 * Captura as exceções lançadas pelas camadas de Service ou Controller
 * e as transforma em respostas HTTP padronizadas com a estrutura de ErrorResponseDTO.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepta ResourceNotFoundException (exceção genérica para itens não encontrados).
     * @return 404 Not Found acompanhado de um ErrorResponseDTO.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Intercepta erros de validação de argumentos (@Valid) nos DTOs.
     * @return 400 Bad Request com uma lista de campos que falharam na validação.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> erros = new ArrayList<>();

        // Coleta todos os erros de validação gerados pelo @Valid
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos dados enviados.",
                LocalDateTime.now(),
                erros
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Intercepta EmailJaCadastradoException durante a criação de usuários.
     * @return 409 Conflict, indicando que o e-mail não pode ser reutilizado.
     */
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Intercepta problemas de integridade de dados lançados pelo banco (ex: chaves duplicadas).
     * @return 409 Conflict alertando sobre o registro duplicado ou inconsistente.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflito de dados: Ocorreu uma violação de integridade (ex: registro duplicado).",
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Intercepta exceções específicas de entidades não encontradas.
     * @return 404 Not Found caso o usuário ou jogo não exista.
     */
    @ExceptionHandler({UsuarioNaoEncontradoException.class, JogoNaoEncontradoException.class})
    public ResponseEntity<ErrorResponseDTO> handleEntidadeNaoEncontrada(Exception ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    /**
     * Intercepta a tentativa de compra com saldo menor que o necessário.
     * @return 400 Bad Request, informando o saldo insuficiente.
     */
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponseDTO> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Intercepta a tentativa de compra de um jogo que já está na biblioteca do usuário.
     * @return 409 Conflict para evitar duplicidade de aquisição.
     */
    @ExceptionHandler(JogoJaAdquiridoException.class)
    public ResponseEntity<ErrorResponseDTO> handleJogoJaAdquiridoException(JogoJaAdquiridoException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Intercepta a criação/atualização de um jogo com preço numérico inválido (ex: negativo).
     * @return 422 Unprocessable Entity devido a violação de regra de negócio (preço).
     */
    @ExceptionHandler(PrecoInvalidoException.class)
    public ResponseEntity<ErrorResponseDTO> handlePrecoInvalidoException(PrecoInvalidoException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    /**
     * Intercepta quaisquer exceções não mapeadas para prevenir exposição de dados sensíveis.
     * @return 500 Internal Server Error sinalizando erro não esperado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor.",
                LocalDateTime.now(),
                null
        );
        // Oculta a stack trace do cliente, mas no mundo real você usaria um log.error(ex.getMessage(), ex) aqui
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}