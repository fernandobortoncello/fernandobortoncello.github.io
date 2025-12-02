package sistema_tcc.dto;

import sistema_tcc.dominio.tipos.Papel;

public record UsuarioLogadoDTO(String id, String nome, Papel papel) {}