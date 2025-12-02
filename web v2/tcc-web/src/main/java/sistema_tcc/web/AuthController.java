package sistema_tcc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sistema_tcc.dominio.Usuario;
import sistema_tcc.dto.UsuarioLogadoDTO;
import sistema_tcc.repositorio.UsuarioRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository repo;

    @PostMapping("/login")
    public UsuarioLogadoDTO login(@RequestBody Map<String, String> credenciais) {
        String id = credenciais.get("id");
        String senha = credenciais.get("senha");

        Usuario u = repo.findById(id).orElse(null);

        if (u != null && u.autenticar(senha)) {
            return new UsuarioLogadoDTO(u.getId(), u.getNome(), u.getPapel());
        }
        throw new RuntimeException("Login inválido ou usuário não encontrado.");
    }
}