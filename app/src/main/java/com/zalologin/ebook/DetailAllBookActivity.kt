package com.zalologin.ebook

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zalologin.R
import com.zalologin.databinding.ActivityDetailAllBinding
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.domain.TOCReference

/**
 * //Todo
 *
 * Created by HOME on 9/20/2017.
 */
class DetailAllBookActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityDetailAllBinding
    private var desFolder: String = "";
    private var book: Book? = null
    private var tocReferences: List<TOCReference>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_all)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_book)

        book = intent.extras["Chapter"] as Book
        desFolder = intent.extras["path"] as String

        setData()
    }

    private fun setData() {
        tocReferences = book!!.getTableOfContents().tocReferences

        if (tocReferences == null) {
            return
        }

        var web: HorizontalWebView
        tocReferences?.forEach { it ->
            run {
                web = HorizontalWebView(this, null)
                web.settings.javaScriptEnabled = true
                setWebViewListener(web)
            }
        }
    }

    private fun setWebViewListener(webView: HorizontalWebView) {
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                injectJavascript(webView)
            }
        })

        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                val pageCount = Integer.parseInt(message)
                webView.setPageCount(pageCount)
                result!!.confirm()
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
}