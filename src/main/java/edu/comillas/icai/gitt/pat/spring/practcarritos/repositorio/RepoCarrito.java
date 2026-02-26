package edu.comillas.icai.gitt.pat.spring.practcarritos.repositorio;

import edu.comillas.icai.gitt.pat.spring.practcarritos.entity.Carrito;
import org.springframework.data.repository.CrudRepository;

public interface RepoCarrito extends CrudRepository<Carrito, Long> {
    Carrito findByIdUsuario(Long idUsuario);
    Carrito findByEmailUsuario(String emailUsuario);
}
