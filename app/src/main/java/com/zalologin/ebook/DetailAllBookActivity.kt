package com.zalologin.ebook

import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.Toast
import com.zalologin.R
import com.zalologin.ScreenUtil
import com.zalologin.databinding.ActivityDetailAllBinding
import com.zalologin.databinding.ActivityDetailAllBookBinding
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.domain.SpineReference
import nl.siegmann.epublib.domain.TOCReference
import nl.siegmann.epublib.epub.EpubReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.ArrayList

/**
 * //Todo
 *
 * Created by HOME on 9/20/2017.
 */
class DetailAllBookActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityDetailAllBookBinding
    private var desFolder: String = "";
    private var book: Book? = null
    private var tocReferences: List<TOCReference>? = null
    private var spineReferences: List<SpineReference>? = null
    private var horizontals = ArrayList<HorizontalWebView>()
    private var totalPage = 0
    private var pages = ArrayList<PageBook>()
    private var d = 0
    lateinit var web: HorizontalWebView
    private var chapters = ArrayList<Chapter>()
    private var ch = 1
    private var isClick = false
    private var coordinateX = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_all)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_all_book)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        desFolder = intent.extras["path"] as String
        initReadBook()

        mBinding.btnSize.setOnClickListener {
            chapters
                    .filter { it.id == ch }
                    .forEach {
                        isClick = true
                        web.refreshWebView()
                        web.loadUrl(it.firstPage?.link)
                        coordinateX = it.firstPage?.coordinateX!!
                        ch++
                    }

        }

        web.setOnSwipeListener(object : OnSwipeListener {
            override fun onPreviousChapter() {
                ch = ch - 2
                if (ch > 0) {
                    chapters
                            .filter { it.id == ch }
                            .forEach {
                                isClick = true
                                web.refreshWebView()
                                web.loadUrl(it.lastPage?.link)
                                coordinateX = it.lastPage?.coordinateX!!
                                ch++
                            }
                }
            }

            override fun onNextChapter() {
                if (ch <= chapters.size) {
                    chapters
                            .filter { it.id == ch }
                            .forEach {
                                isClick = true
                                web.refreshWebView()
                                web.loadUrl(it.firstPage?.link)
//                                web.goTopPage(it.firstPage?.coordinateX!!)
                                coordinateX = it.firstPage?.coordinateX!!
                                ch++
                            }
                }
            }

            override fun onPage(current: Int, total: Int) {
                System.out.println("$current : $total")
            }

            override fun onTouch() {
                System.out.println("Touch")
            }
        })
    }

    private fun initReadBook() {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.toString() + "/EPUB/"
        val file = File(path, "ebook000001.epub")
        try {
            // find InputStream for book
            //            InputStream epubInputStream = assetManager
            //                    .open("hpmor.epub");

            val epubInputStream = FileInputStream(file)

            // Load Book from inputStream
            book = EpubReader().readEpub(epubInputStream)

            // Log the book's authors
            Log.i("epublib", "author(s): " + book!!.getMetadata().authors)

            // Log the book's title
            Log.i("epublib", "title: " + book!!.getTitle())

            // Log the book's coverimage property
            val coverImage = BitmapFactory.decodeStream(book!!.getCoverImage()
                    .inputStream)
            Log.i("epublib", "Coverimage is " + coverImage.width + " by "
                    + coverImage.height + " pixels")


            // Log the tale of contents
            println("epublib read")
            //            logContentOfChapter(book, 0);
            if (book != null) {
                tocReferences = book!!.tableOfContents.tocReferences
                logTableOfContents(book!!.tableOfContents.tocReferences, 0)

                getContentBook(book!!)

//                if (tocReferences != null) {
//                    web = HorizontalWebView(this, null)
//                    addView(tocReferences!![d])
//                }
            }
        } catch (e: IOException) {
            println("epublib error")
            Log.e("epublib", e.message)
        }
    }

    private fun getContentBook(book: Book) {
        spineReferences = book.spine.spineReferences

        if (spineReferences != null) {
            web = HorizontalWebView(this, null)
            addView(spineReferences!![d])
        }
    }

    private fun logTableOfContents(tocReferences: List<TOCReference>?, depth: Int) {
        if (tocReferences == null) {
            return
        }

        val chapters = ArrayList<String>()
        for (tocReference in tocReferences) {
            val tocString = StringBuilder()
            for (i in 0..depth - 1) {
                tocString.append("\t")
            }
            tocString.append(tocReference.title)

            chapters.add(tocString.toString())
            Log.i("epublib", tocString.toString())
        }
    }

    private fun addView(spineReference: SpineReference) {
//        var web = HorizontalWebView(this, null)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mBinding.rlWeb.addView(web, layoutParams)
        web.settings.javaScriptEnabled = true
        setWebViewListener(web)
//        spineReference.resource.href
        web.loadUrl("file://" + desFolder + spineReference.resource.href)
//        horizontals.add(web)
//        horizontals.add(web)
    }

    private fun setWebViewListener(webView: HorizontalWebView) {
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                injectJavascript(webView)
            }
        })

        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                var pageCount = 0
                try {
                    pageCount = Integer.parseInt(message)
                    if (!isClick) {
                        var chapter = Chapter()
                        for (i in 0 until pageCount) {
                            var page = PageBook()
                            page.chapter = d + 1
                            page.coordinateX = i * webView.width
                            page.order = totalPage + i + 1
                            page.link = url!!
                            pages.add(page)
                            if (i == 0) {
                                chapter.id = d + 1
                                chapter.firstPage = page
                            }
                            if (i == pageCount - 1) {
                                chapter.lastPage = page
                            }
                        }
                        chapters.add(chapter)
                    }
                } catch (e: NumberFormatException) {
                    System.out.println("Error");
                    e.printStackTrace()
                }
                webView.setPageCount(pageCount)
                if (isClick) {
                    webView.goTopPage(coordinateX)
                }
                result!!.confirm()
                d++
                totalPage += pageCount;
                System.out.println("chuong: $d | so trang: $pageCount | tong so: $totalPage")
                if (d < tocReferences!!.size) {
                    mBinding.rlWeb.removeAllViews()
                    addView(spineReferences!![d])
                } else {
                    System.out.println("chuong Da tao page : " + pages[0].link + " : " + chapters[0].id)
                }
                return true
            }
        })
    }

    private fun injectJavascript(webView: HorizontalWebView) {
        val js = "function initialize(){\n" +
                " var d = document.getElementsByTagName('body')[0];\n" +
                " var ourH = window.innerHeight;\n" +
                " var ourW = window.innerWidth;\n" +
                " var fullH = d.offsetHeight;\n" +
                " var pageCount = Math.floor(fullH/ourH)+1;\n" +
                " var currentPage = 0;\n" +
                " var newW = pageCount*ourW;\n" +
                " d.style.height = ourH+'px';\n" +
                " d.style.width = newW+'px';\n" +
                //                " d.style.margin = 0;\n" +
                " d.style.webkitColumnCount = pageCount;\n" +
                " return pageCount;\n" +
                "}"
        webView.loadUrl("javascript:" + js)
        webView.loadUrl("javascript:alert(initialize())")
    }

    private fun changeSize(webView: HorizontalWebView, size: Int) {
        val js = "function changeSize() {\n" +
                "document.getElementsByTagName('body')[0].style.fontSize ='" + size + "px'; \n" +
                "}"
        webView.loadUrl("javascript:" + js)
        webView.loadUrl("javascript:changeSize()")
    }


}