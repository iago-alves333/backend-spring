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

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ResourceNotFoundException → retorna HTTP 404
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

    // MethodArgumentNotValidException → retorna HTTP 400 + lista de campos
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

    // EmailJaCadastradoException → retorna HTTP 409 com mensagem específica
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

    // 3. DataIntegrityViolationException → retorna HTTP 409 (Conflict)
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

    // Tratamento para Entidades Não encontradas + retorna http 404
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
    //Lançamento de Exeption caso o saldo seja insuficiente + retorna HTTP 400
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

    //Tratamento para jogo já adquirido + retorna HTTP 409 (Conflict)
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

    // Tratamento para preço inválido + retorna HTTP 422 (Unprocessable Entity)
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

    // Exception genérica → retorna HTTP 500 (Catch-all)
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