package edu.comillas.icai.gitt.pat.spring.practcarritos.servicio;

import edu.comillas.icai.gitt.pat.spring.practcarritos.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.practcarritos.entity.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.practcarritos.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.practcarritos.repositorio.RepoLineaCarrito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicioCarrito {

    @Autowired
    RepoCarrito repoCarrito;

    @Autowired
    RepoLineaCarrito repoLineaCarrito;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Crear un carrito nuevo
    @Transactional
    public Carrito crearCarrito(Carrito carritoNuevo) {
        logger.info("ServicioCarrito: Creando carrito para usuario: " + carritoNuevo.getIdUsuario());
        Carrito carritoGuardado = repoCarrito.save(carritoNuevo);
        logger.info("ServicioCarrito: Carrito creado con ID: " + carritoGuardado.getIdCarrito());
        return carritoGuardado;
    }

    // Consulta un carrito por su ID
    public Carrito consultarCarrito(Long idCarrito) {
        logger.info("ServicioCarrito: Consultando carrito con ID: " + idCarrito);
        Carrito carrito = repoCarrito.findById(idCarrito).orElse(null);
        if (carrito == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito con ID " + idCarrito + " no encontrado.");
        }
        return carrito;
    }

    // Lista todos los carritos
    public List<Carrito> listarCarritos() {
        logger.info("ServicioCarrito: Listando todos los carritos");
        return (List<Carrito>) repoCarrito.findAll();
    }

    // Modifica los datos de un carrito existente (email, idUsuario)
    @Transactional
    public Carrito modificarCarrito(Long idCarrito, Carrito datosActualizados) {
        logger.info("ServicioCarrito: Modificando carrito con ID: " + idCarrito);
        Carrito carritoExistente = consultarCarrito(idCarrito);

        if (datosActualizados.getIdUsuario() != null) {
            carritoExistente.setIdUsuario(datosActualizados.getIdUsuario());
        }
        if (datosActualizados.getEmailUsuario() != null) {
            carritoExistente.setEmailUsuario(datosActualizados.getEmailUsuario());
        }

        return repoCarrito.save(carritoExistente);
    }

    // Elimina un carrito por su ID
    @Transactional
    public void eliminarCarrito(Long idCarrito) {
        logger.info("ServicioCarrito: Eliminando carrito con ID: " + idCarrito);
        Carrito carrito = consultarCarrito(idCarrito);
        repoCarrito.delete(carrito);
        logger.info("ServicioCarrito: Carrito con ID " + idCarrito + " eliminado.");
    }

    // Busca un carrito por email de usuario
    public Carrito buscarPorEmail(String email) {
        logger.info("ServicioCarrito: Buscando carrito para email: " + email);
        Carrito carrito = repoCarrito.findFirstByEmailUsuario(email);
        if (carrito == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay carrito para el email: " + email);
        }
        return carrito;
    }

    // ========== Operaciones sobre Líneas de Carrito

    // Añade una línea de artículo a un carrito existente
    @Transactional
    public LineaCarrito añadirLinea(Long idCarrito, LineaCarrito lineaNueva) {
        logger.info("ServicioCarrito: Añadiendo línea al carrito con ID: " + idCarrito);
        Carrito carrito = consultarCarrito(idCarrito);

        lineaNueva.setCarrito(carrito);
        LineaCarrito lineaGuardada = repoLineaCarrito.save(lineaNueva);
        logger.info("ServicioCarrito: Línea añadida con ID: " + lineaGuardada.getIdLineaCarrito());
        return lineaGuardada;
    }

    // Consulta las líneas de un carrito
    public List<LineaCarrito> consultarLineas(Long idCarrito) {
        logger.info("ServicioCarrito: Consultando líneas del carrito con ID: " + idCarrito);
        Carrito carrito = consultarCarrito(idCarrito);
        return carrito.getLineas();
    }

    // Modifica una línea de carrito existente
    @Transactional
    public LineaCarrito modificarLinea(Long idLineaCarrito, LineaCarrito datosActualizados) {
        logger.info("ServicioCarrito: Modificando línea con ID: " + idLineaCarrito);
        LineaCarrito lineaExistente = repoLineaCarrito.findById(idLineaCarrito).orElse(null);
        if (lineaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea con ID " + idLineaCarrito + " no encontrada.");
        }

        if (datosActualizados.getIdArticulo() != null) {
            lineaExistente.setIdArticulo(datosActualizados.getIdArticulo());
        }
        if (datosActualizados.getPrecioUnitario() > 0) {
            lineaExistente.setPrecioUnitario(datosActualizados.getPrecioUnitario());
        }
        if (datosActualizados.getNumUnidades() > 0) {
            lineaExistente.setNumUnidades(datosActualizados.getNumUnidades());
        }

        LineaCarrito lineaGuardada = repoLineaCarrito.save(lineaExistente);
        return lineaGuardada;
    }

    // Elimina una línea de carrito
    @Transactional
    public void eliminarLinea(Long idLineaCarrito) {
        logger.info("ServicioCarrito: Eliminando línea con ID: " + idLineaCarrito);
        LineaCarrito linea = repoLineaCarrito.findById(idLineaCarrito).orElse(null);
        if (linea == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea con ID " + idLineaCarrito + " no encontrada.");
        }
        repoLineaCarrito.delete(linea);
        logger.info("ServicioCarrito: Línea con ID " + idLineaCarrito + " eliminada.");
    }
}
