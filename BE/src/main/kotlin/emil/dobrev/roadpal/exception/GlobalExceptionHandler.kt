package emil.dobrev.roadpal.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Map<String, String>> {
        val errors = ex.constraintViolations.associate { it.propertyPath.toString() to it.message }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity("Unexpected error: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(RequestNotPermitted::class)
    fun handleRequestNotPermittedException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity("Unexpected error: ${ex.message}", HttpStatus.TOO_MANY_REQUESTS)
    }

}