package com.example.docpro_version2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;

public class Link_Activity extends AppCompatActivity {

    PDFView link_view;
    TextView pages_link,pagesChange_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        link_view = findViewById(R.id.pdf_view_link);
        pages_link = findViewById(R.id.pages_link);
        pagesChange_link=findViewById(R.id.pagesChange_link);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String fileUrl = bundle.getString("link");

      if(fileUrl!=null){

          File file=new File(fileUrl);
          loadPdf(file);
      }else {
          Toast.makeText(this, "Invalid File Link", Toast.LENGTH_SHORT).show();
          finish();
      }


    }

    private void loadPdf(File pdf) {

        link_view.fromFile(pdf)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableAnnotationRendering(false)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        pages_link.setText(String.valueOf(nbPages));
                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        // Handle page change if needed
                        pagesChange_link.setText(String.valueOf(String.valueOf(page+1+ " /")));
                    }
                })
                .load();
    }
}