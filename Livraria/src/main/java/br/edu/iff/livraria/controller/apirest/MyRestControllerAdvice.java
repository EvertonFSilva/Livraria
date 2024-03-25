package br.edu.iff.livraria.controller.apirest;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.edu.iff.livraria.exception.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;

public class MyRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorDetails handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorDetails(req.getRequestURI(), ex.getMessage());
    }

}
