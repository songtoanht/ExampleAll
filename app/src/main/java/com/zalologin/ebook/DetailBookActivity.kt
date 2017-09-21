package com.zalologin.ebook

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.zalologin.R
import com.zalologin.ScreenUtil
import com.zalologin.databinding.ActivityDetailBookBinding
import nl.siegmann.epublib.domain.TOCReference


/**
 * DetailBookActivity
 *
 * Created by HOME on 9/13/2017.
 */
class DetailBookActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityDetailBookBinding
    private var desFolder: String = ""
    private var fontSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_book)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_book)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        val tocReference = intent.extras["Chapter"] as TOCReference
        desFolder = intent.extras["path"] as String

        tocReference.resource.reader

        setData(tocReference)
        Log.d("ttt", tocReference.title)
    }

    fun setData(tocReference: TOCReference) {
        val s = String(tocReference.resource.data)
        mBinding.web.settings.javaScriptEnabled = true

        setWebViewListener()


        mBinding.web.loadUrl("file://" + desFolder + tocReference.resource.href)
    }

    private fun setWebViewListener() {
        mBinding.web.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                injectJavascript()
            }
        })

        mBinding.web.setWebChromeClient(object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                if (!(message?.contains("get") == true)) {
                    val pageCount = Integer.parseInt(message)
                    mBinding.web.setPageCount(pageCount)
                    mBinding.tvNumberOfPage.text = "1/" + pageCount
                    result!!.confirm()
                } else {
                    if (message != null) {
                        val size = message.substring(0, message.indexOf("px"))
                        try {
                            fontSize = Integer.parseInt(size)
                        } catch (e: NumberFormatException) {
                            Log.d("ttt", "Error");
                        }
                    }
                }
                return true
            }
        })

        mBinding.web.setOnSwipeListener(object : OnSwipeListener {
            override fun onTouch() {

            }

            override fun onPage(current: Int, total: Int) {
                mBinding.tvNumberOfPage.text = current.toString() + "/" + total.toString()
            }
        })

        mBinding.btnSize.setOnClickListener { getSize() }

        mBinding.btnFont.setOnClickListener {
            fontSize = fontSize + 2
            changeSize(fontSize)
        }
    }

    private fun injectJavascript() {
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
        mBinding.web.loadUrl("javascript:" + js)
        mBinding.web.loadUrl("javascript:alert(initialize())")
    }


    private fun changeCss() {
        val js = "function changeColor() {\n" +
                "document.getElementsByTagName('body')[0].style.color = 'red'; \n" +
                "}"
        mBinding.web.loadUrl("javascript:" + js)
        mBinding.web.loadUrl("javascript:changeColor()")
    }


    private fun changeSize(size: Int) {
        val js = "function changeSize() {\n" +
                "document.getElementsByTagName('body')[0].style.fontSize ='" + size + "px'; \n" +
                "}"
        mBinding.web.loadUrl("javascript:" + js)
        mBinding.web.loadUrl("javascript:changeSize()")
    }

    private fun getSize() {
        val js = "function getFontSize() {\n" +
                "var el = document.getElementsByTagName('body')[0];\n" +
                "var style = window.getComputedStyle(el, null).getPropertyValue('font-size');\n" +
                "var size = 'get';\n" +
                " return [style, size];\n" +
                "}"
        mBinding.web.loadUrl("javascript:" + js)
        mBinding.web.loadUrl("javascript:alert(getFontSize())")
    }
}