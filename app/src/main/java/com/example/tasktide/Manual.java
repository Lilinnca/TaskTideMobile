package com.example.tasktide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Manual extends AppCompatActivity {

    private ImageView pdfImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        pdfImageView = findViewById(R.id.pdfImageView);

        // Carregar e renderizar o PDF
        try {
            renderPDF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderPDF() throws IOException {
        // Acesse o arquivo PDF na pasta assets
        ParcelFileDescriptor fileDescriptor = getAssets().openFd("Manual do Usuário TaskTide2024.pdf").getParcelFileDescriptor();
        PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);

        // O arquivo PDF tem páginas, vamos pegar a primeira página (index 0)
        PdfRenderer.Page page = pdfRenderer.openPage(0);

        // Criar um Bitmap com o tamanho da página
        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);

        // Renderizar a página no Bitmap
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Exibir o Bitmap na ImageView
        pdfImageView.setImageBitmap(bitmap);

        // Fechar a página e o PdfRenderer
        page.close();
        pdfRenderer.close();
    }
}
