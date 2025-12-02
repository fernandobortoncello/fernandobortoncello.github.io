package sistema_tcc.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_tcc.dominio.Usuario;
import sistema_tcc.dominio.tipos.Papel;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    List<Usuario> findAllByPapel(Papel papel);
}