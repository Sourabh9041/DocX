package com.example.docpro_version2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

public class Files_Activity extends AppCompatActivity {

    PDFView view_pdf;
    TextView pages,pagesChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        getWindow().setFlags
                (WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        view_pdf = findViewById(R.id.pdf_view);
        pages = findViewById(R.id.pages);
        pagesChange=findViewById(R.id.pagesChange);


        Bundle bundle = getIntent().getExtras();
        Uri pdf = (Uri) bundle.get("data");

        if (pdf != null) {
            loadPdf(pdf);


        }
    }

        private void loadPdf(Uri pdf) {
            view_pdf.fromUri(pdf)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(false)
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            pages.setText(String.valueOf(nbPages));
                        }
                    })
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            // Handle page change if needed
                            pagesChange.setText(String.valueOf(String.valueOf(page+1+ " /")));
                        }
                    })
                    .load();
        }

    }