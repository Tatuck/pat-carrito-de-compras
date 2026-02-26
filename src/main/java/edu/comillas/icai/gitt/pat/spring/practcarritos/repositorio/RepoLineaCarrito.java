package edu.comillas.icai.gitt.pat.spring.practcarritos.repositorio;

import edu.comillas.icai.gitt.pat.spring.practcarritos.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.practcarritos.entity.LineaCarrito;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepoLineaCarrito extends CrudRepository<LineaCarrito, Long> {
    List<LineaCarrito> findByCarrito(Carrito carrito);
    LineaCarrito findByCarritoAndIdArticulo(Carrito carrito, Long idArticulo);
}
