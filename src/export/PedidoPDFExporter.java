package export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.PedidoDAO;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Pedido;
import model.StatusPedido;

public class PedidoPDFExporter {
    
    public void gerarPDF(String statusSelecionado) {
        PedidoDAO pedidoDAO = new PedidoDAO();
        List<Pedido> pedidos;

        if (statusSelecionado.equals("Todos")) {
            pedidos = pedidoDAO.listarTodos(); // lista todos os pedidos
        } else {
            StatusPedido status = StatusPedido.valueOf(statusSelecionado);
            pedidos = pedidoDAO.listarPorStatus(status); // lista filtrando pelo status
        }

        String nomeArquivo = "pedidos_" + statusSelecionado.toLowerCase() + ".pdf";
        exportarPedidos(pedidos, nomeArquivo);
        
        // Usando o FileChooser para escolher o local para salvar o arquivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("pedidos_" + statusSelecionado.toLowerCase() + ".pdf");

        Window primaryStage = null;
		// Abrir a janela para o usuário escolher onde salvar o arquivo
        File arquivoEscolhido = fileChooser.showSaveDialog(primaryStage);

        if (arquivoEscolhido != null) {
            // Se o usuário escolher um arquivo, exporta os pedidos para o PDF no local escolhido
            exportarPedidos(pedidos, arquivoEscolhido.getAbsolutePath());
        }
    }

    public void exportarPedidos(List<Pedido> pedidos, String nomeArquivo) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));
            document.open();

            // Título
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Lista de Pedidos", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // Tabela
            PdfPTable tabela = new PdfPTable(7); // Corrigido para 7 colunas
            tabela.setWidthPercentage(100);
            tabela.setSpacingBefore(10);
            
         // Definindo larguras das colunas
            float[] columnWidths = {1f, 2f, 3f, 2f, 2f, 2f, 2f};
            tabela.setWidths(columnWidths);

            // Cabeçalhos
            adicionarCelulaCabecalho(tabela, "ID");
            adicionarCelulaCabecalho(tabela, "Cliente");
            adicionarCelulaCabecalho(tabela, "Produto");
            adicionarCelulaCabecalho(tabela, "Data Pedido");
            adicionarCelulaCabecalho(tabela, "Data Entrega");
            adicionarCelulaCabecalho(tabela, "Valor Total");
            adicionarCelulaCabecalho(tabela, "Status");

            // Conteúdo
            for (Pedido pedido : pedidos) {
                tabela.addCell(String.valueOf(pedido.getId()));
                tabela.addCell(pedido.getCliente().getNome());
                tabela.addCell(pedido.getProduto().getNome());
                tabela.addCell(pedido.getDataPedido());
                tabela.addCell(pedido.getDataEntrega());
                tabela.addCell(String.format("R$ %.2f", pedido.getValorTotal()));
                tabela.addCell(pedido.getStatus().toString().replace("_", " "));
            }

            document.add(tabela);

        } catch (Exception e) {
            System.err.println("Erro ao exportar PDF de pedidos:");
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private void adicionarCelulaCabecalho(PdfPTable tabela, String texto) {
        Font fonteCabecalho = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonteCabecalho));
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabela.addCell(celula);
    }
}
