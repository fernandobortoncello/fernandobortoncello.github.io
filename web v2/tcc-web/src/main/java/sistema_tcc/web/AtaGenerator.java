package sistema_tcc.web;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import sistema_tcc.dominio.Tcc;
import sistema_tcc.dominio.Usuario;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Gerador de ATA em PDF.
 * Localizado no pacote 'web' pois serve como um auxiliar para a resposta HTTP do Controller.
 */
public class AtaGenerator {

    public static byte[] gerarAtaPdf(Tcc tcc) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("ATA DE DEFESA DE TCC", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n")); // Espaço

            // Corpo do Texto
            Font fontCorpo = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Formatação da Data
            String dataFormatada = "[DATA INDEFINIDA]";
            if (tcc.getDataBanca() != null) {
                dataFormatada = tcc.getDataBanca().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            String texto = String.format(
                    "Aos %s, reuniu-se a banca examinadora para avaliar o Trabalho de Conclusão de Curso intitulado \"%s\", " +
                            "de autoria do aluno(a) %s, sob orientação do professor(a) %s.\n\n" +
                            "A banca foi composta pelos seguintes membros:\n",
                    dataFormatada,
                    tcc.getTitulo(),
                    tcc.getAutor().getNome(),
                    (tcc.getOrientador() != null ? tcc.getOrientador().getNome() : "N/A")
            );

            Paragraph paragrafo1 = new Paragraph(texto, fontCorpo);
            paragrafo1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragrafo1);
            document.add(new Paragraph("\n"));

            // Lista de Membros da Banca
            com.lowagie.text.List lista = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED);
            if (tcc.getBancaMembros() != null) {
                for (Usuario membro : tcc.getBancaMembros()) {
                    lista.add(new ListItem(membro.getNome()));
                }
            }
            document.add(lista);
            document.add(new Paragraph("\n"));

            // Resultado
            String statusAprovacao = "REPROVADO";
            if (tcc.getNotaFinal() != null && tcc.getNotaFinal() >= 6.0) {
                statusAprovacao = "APROVADO";
            }

            String resultado = String.format(
                    "Após a apresentação e arguição, a banca atribuiu a nota final %.2f. " +
                            "Considerando os critérios de avaliação, o aluno foi considerado %s.\n\n" +
                            "Parecer Final da Banca:\n%s",
                    (tcc.getNotaFinal() != null ? tcc.getNotaFinal() : 0.0),
                    statusAprovacao,
                    (tcc.getParecerFinal() != null ? tcc.getParecerFinal() : "")
            );

            Paragraph paragrafo2 = new Paragraph(resultado, fontCorpo);
            paragrafo2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragrafo2);

            document.add(new Paragraph("\n\n\n____________________________________"));
            Paragraph assinatura = new Paragraph("Assinatura do Orientador", fontCorpo);
            document.add(assinatura);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}