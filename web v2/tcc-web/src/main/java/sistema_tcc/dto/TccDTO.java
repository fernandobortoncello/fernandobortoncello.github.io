package sistema_tcc.dto;

import sistema_tcc.dominio.Tcc;
import sistema_tcc.dominio.Usuario;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record TccDTO(
        String id,
        String titulo,
        String descricao,
        String status,
        String autor,
        String orientador,
        List<String> orientacoes,
        Double nota,
        String parecer,
        String dataBanca,
        List<String> bancaMembros
) {
    public static TccDTO from(Tcc t) {
        return new TccDTO(
                t.getId(),
                t.getTitulo(),
                t.getDescricao(),
                t.getStatus().name(),
                t.getAutor().getNome(),
                t.getOrientador() != null ? t.getOrientador().getNome() : "Aguardando",
                t.getOrientacoes(),
                t.getNotaFinal(),
                t.getParecerFinal(), // Precisamos garantir que Tcc.java tenha getParecerFinal()
                t.getDataBanca() != null ? t.getDataBanca().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null,
                t.getBancaMembros().stream().map(Usuario::getNome).collect(Collectors.toList())
        );
    }
}