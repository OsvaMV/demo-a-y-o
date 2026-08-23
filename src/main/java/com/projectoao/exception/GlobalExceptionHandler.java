package com.projectoao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para traducirlas a respuestas HTTP controladas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ProblemDetail manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
	}

}
