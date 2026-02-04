package edu.comillas.icai.gitt.pat.spring.pract2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ControladorREST {
    private static final HashMap<Integer, Carrito> carritos = new HashMap<>();

    public static HashMap<Integer, Carrito> getCarritos() {
        return carritos;
    }

    
    @PostMapping("/crear")
    public Carrito crearCarrito(@Valid @RequestBody Carrito carrito) {
        if (carritos.containsKey(carrito.idCarrito())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Carrito con ID " + carrito.idCarrito() + " ya existe.");
        }
        Carrito nuevoCarrito = new Carrito(carrito.idCarrito(), carrito.idArticulo(), carrito.descripcion(), carrito.unidades());
        carritos.put(carrito.idCarrito(), nuevoCarrito);
        return nuevoCarrito;
    }

    @PutMapping("/modificar/{idCarrito}")
    public Carrito modificarCarrito(@PathVariable int idCarrito, @RequestBody CarritoActualizacion actualizacion) {
        Carrito carritoExistente = carritos.get(idCarrito);
        if (carritoExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito con ID " + idCarrito + " no encontrado.");
        }
        
        // Solo actualizar los campos que no sean null
        int nuevoIdArticulo = actualizacion.getIdArticulo() != null ? actualizacion.getIdArticulo() : carritoExistente.idArticulo();
        String nuevaDescripcion = actualizacion.getDescripcion() != null ? actualizacion.getDescripcion() : carritoExistente.descripcion();
        int nuevasUnidades = actualizacion.getUnidades() != null ? actualizacion.getUnidades() : carritoExistente.unidades();
        
        Carrito carritoModificado = new Carrito(idCarrito, nuevoIdArticulo, nuevaDescripcion, nuevasUnidades);
        carritos.put(idCarrito, carritoModificado);
        return carritoModificado;
    }

    @GetMapping("/consultar/{idCarrito}")
    public Carrito consultarCarrito(@PathVariable int idCarrito) {
        Carrito carrito = carritos.get(idCarrito);
        if (carrito == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito con ID " + idCarrito + " no encontrado.");
        }
        return carrito;
    }

    @DeleteMapping("/eliminar/{idCarrito}")
    public String eliminarCarrito(@PathVariable int idCarrito) {
        if (carritos.remove(idCarrito) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito con ID " + idCarrito + " no encontrado.");
        }
        return "Carrito con ID " + idCarrito + " eliminado exitosamente.";
    }
}
