package edu.comillas.icai.gitt.pat.spring.pract2;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Carrito (
    @NotNull(message = "El id del carrito no puede estar vacío")
    Integer idCarrito,
    
    @NotNull(message = "El id del artículo no puede estar vacío")
    Integer idArticulo,
    
    @NotBlank(message = "La descripción no puede estar vacía")
    String descripcion,
    
    @NotNull(message = "Las unidades no pueden estar vacías")
    @Min(value = 1, message = "Las unidades deben ser al menos 1")
    Integer unidades){

        @JsonProperty
        public float precioFinal() {
            return unidades * 10.0f;
        }
}
