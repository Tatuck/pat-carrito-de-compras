package edu.comillas.icai.gitt.pat.spring.practcarritos;

import edu.comillas.icai.gitt.pat.spring.practcarritos.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.practcarritos.entity.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.practcarritos.servicio.ServicioCarrito;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorREST {

    @Autowired
    private ServicioCarrito servicioCarrito;

    // ========== Endpoints de Carrito ==========

    @PostMapping("/carritos")
    public Carrito crearCarrito(@Valid @RequestBody Carrito carrito) {
        return servicioCarrito.crearCarrito(carrito);
    }

    @GetMapping("/carritos")
    public List<Carrito> listarCarritos() {
        return servicioCarrito.listarCarritos();
    }

    @GetMapping("/carritos/{idCarrito}")
    public Carrito consultarCarrito(@PathVariable Long idCarrito) {
        return servicioCarrito.consultarCarrito(idCarrito);
    }

    @GetMapping("/carritos/buscar")
    public Carrito buscarCarritoPorEmail(@RequestParam String email) {
        return servicioCarrito.buscarPorEmail(email);
    }

    @PutMapping("/carritos/{idCarrito}")
    public Carrito modificarCarrito(@PathVariable Long idCarrito, @RequestBody Carrito datosActualizados) {
        return servicioCarrito.modificarCarrito(idCarrito, datosActualizados);
    }

    @DeleteMapping("/carritos/{idCarrito}")
    public String eliminarCarrito(@PathVariable Long idCarrito) {
        servicioCarrito.eliminarCarrito(idCarrito);
        return "Carrito con ID " + idCarrito + " eliminado exitosamente.";
    }

    // ========== Endpoints de Línea de Carrito ==========

    @PostMapping("/carritos/{idCarrito}/lineas")
    public LineaCarrito añadirLinea(@PathVariable Long idCarrito, @Valid @RequestBody LineaCarrito linea) {
        return servicioCarrito.añadirLinea(idCarrito, linea);
    }

    @GetMapping("/carritos/{idCarrito}/lineas")
    public List<LineaCarrito> consultarLineas(@PathVariable Long idCarrito) {
        return servicioCarrito.consultarLineas(idCarrito);
    }

    @PutMapping("/lineas/{idLineaCarrito}")
    public LineaCarrito modificarLinea(@PathVariable Long idLineaCarrito, @RequestBody LineaCarrito datosActualizados) {
        return servicioCarrito.modificarLinea(idLineaCarrito, datosActualizados);
    }

    @DeleteMapping("/lineas/{idLineaCarrito}")
    public String eliminarLinea(@PathVariable Long idLineaCarrito) {
        servicioCarrito.eliminarLinea(idLineaCarrito);
        return "Línea con ID " + idLineaCarrito + " eliminada exitosamente.";
    }
}
