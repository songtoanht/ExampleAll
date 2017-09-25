package com.zalologin.ebook;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zalologin.R;
import com.zalologin.databinding.ActivityEbookBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * EbookActivity
 * <p>
 * Created by HOME on 9/6/2017.
 */

public class EbookActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEbookBinding binding;
    private List<TOCReference> tocReferences;
    private String desFolder;
    private Book book;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ebook);
        binding.recyclerChapter.setLayoutManager(new LinearLayoutManager(this));
        BookUtils.Companion.bindAllBook(this);

        String filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "EPUB" + File.separator;
        String zipName = "book000006.epub";

        desFolder = filePath + "file" + File.separator;

        try {
            FileUtilsssssss.unzip(filePath + zipName, desFolder, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        initReadBook();

    }

    private void initReadBook() {
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/EPUB/";
        File file = new File(path, "book000006.epub");
        try {
            // find InputStream for book
//            InputStream epubInputStream = assetManager
//                    .open("hpmor.epub");

            InputStream epubInputStream = new FileInputStream(file);

            // Load Book from inputStream
            book = (new EpubReader()).readEpub(epubInputStream);

            // Log the book's authors
            Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());

            // Log the book's title
            Log.i("epublib", "title: " + book.getTitle());

            book.getSpine().getSpineReferences().get(0).getResource().getHref();

            // Log the book's coverimage property
            Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage()
                    .getInputStream());
            Log.i("epublib", "Coverimage is " + coverImage.getWidth() + " by "
                    + coverImage.getHeight() + " pixels");

            binding.setBook(book);
            binding.imgBook.setImageBitmap(coverImage);

            // Log the tale of contents
            System.out.println("epublib read");
//            logContentOfChapter(book, 0);
            tocReferences = book.getTableOfContents().getTocReferences();
            logTableOfContents(book.getTableOfContents().getTocReferences(), 0);
        } catch (IOException e) {
            System.out.println("epublib error");
            Log.e("epublib", e.getMessage());
        }
    }

    /**
     * Recursively Log the Table of Contents
     *
     * @param tocReferences
     * @param depth
     */
    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return;
        }

        List<String> chapters = new ArrayList<>();
        for (TOCReference tocReference : tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());

            chapters.add(tocString.toString());
            Log.i("epublib", tocString.toString());
        }
        binding.recyclerChapter.setAdapter(new ListBookAdapter(this, chapters, this));
    }


    /**
     * Recursively Log the Table of Contents
     *
     * @param book
     * @param depth
     */
    private void logContentOfChapter(Book book, int depth) {
        try {
            String data = new String(book.getContents().get(depth).getData());
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Toast.makeText(this, tocReferences.get(position).getTitle(), Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(this, DetailBookActivity.class);
//        intent.putExtra("Chapter", tocReferences.get(position));
//        intent.putExtra("path", desFolder);
//        startActivity(intent);

        Intent intent = new Intent(this, DetailAllBookActivity.class);
        intent.putExtra("path", desFolder);
        startActivity(intent);
    }
}
