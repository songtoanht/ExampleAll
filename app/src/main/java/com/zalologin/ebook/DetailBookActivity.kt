package com.zalologin.ebook

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zalologin.R
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
    private var fontSize = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_book)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        val tocReference = intent.extras["Chapter"] as TOCReference
        desFolder = intent.extras["path"] as String

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
//                injectJavascript()
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
                            e.printStackTrace()
                        }
                    }
                }
                return true
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                injectJavascript()
                return super.onJsConfirm(view, url, message, result)
            }
        })

        mBinding.web.setOnSwipeListener(object : OnSwipeListener {
            override fun onNextChapter() {
                System.out.println("onNextChapter")
            }

            override fun onPreviousChapter() {
                System.out.println("onPreviousChapter")
            }

            override fun onTouch() {

            }

            override fun onPage(current: Int, total: Int) {
                mBinding.tvNumberOfPage.text = current.toString() + "/" + total.toString()
            }
        })

        mBinding.btnSize.setOnClickListener {
            fontSize = fontSize + 2
            changeSize(fontSize)
        }

        mBinding.btnFont.setOnClickListener {
            changeFont2()
        }
    }

    private fun injectJavascript() {
        val js = "function initialize(){\n" +
                " var d = document.getElementsByTagName('body')[0];\n" +
                "d.style.padding = '0px';\n" +
                " var ourH = window.innerHeight;\n" +
                " var ourW = window.innerWidth;\n" +
                " var fullH = d.offsetHeight;\n" +
                " var pageCount = Math.ceil(fullH/ourH);\n" +
                " var currentPage = 0;\n" +
                " var newW = pageCount*ourW;\n" +
                " d.style.height = ourH+'px';\n" +
                " d.style.width = newW +'px';\n" +
                " d.style.margin = '8px';\n" +
                " d.style.webkitColumnGap = '16px';\n" +
                " d.style.webkitColumnCount = pageCount;\n" +
                " return pageCount;\n" +
                "}"
        mBinding.web.loadUrl("javascript:" + js)
        mBinding.web.loadUrl("javascript:alert(initialize())")
    }


    private fun changeColorTextAndBackground(type: Int) {
        val js = "function changeColor(type) {\n" +
                "var tcl = 'black';\n" +
                "var bgcl = 'white';\n" +
                " if (type == 1) {" +
                "tcl = 'white';\n" +
                "bgcl = 'black';\n" +
                "} else if (type == 2){\n" +
                "tcl = 'black';\n" +
                "bgcl = 'red';\n" +
                "}\n" +
                "document.body.style.backgroundColor = bgcl;\n" +
                "document.body.style.color = tcl;\n" +
                "}"
        mBinding.web.loadUrl("javascript:" + js)
        mBinding.web.loadUrl("javascript:changeColor($type)")
    }


    private fun changeSize(size: Int) {
        val js = "function changeSize(s) {\n" +
                "document.body.style.fontSize = s + 'px'; \n" +
                "}"
        mBinding.web.loadUrl("javascript:" + js)
        mBinding.web.loadUrl("javascript:confirm(changeSize($size))")
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

    private fun changeFont() {

        val addCSSRule = "function addCSSRule(selector, newRule) {\n" +
                "var mySheet = document.styleSheets[0];\n" +
                "ruleIndex = mySheet.cssRules.length;\n" +
                "mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);\n" +
                "}"

        val insertRule1 = "addCSSRule('html, body, div, p, span, a, h1, h2, h3, h4, h5, h6', " +
                "'font-family: Palatino;')"

        mBinding.web.loadUrl("javascript:" + addCSSRule)
        mBinding.web.loadUrl("javascript:" + insertRule1)
    }

    private fun changeFont2() {
        val addCSSRule = "function myFunction() {\n" +
                "var parent = document.getElementsByTagName('head').item(0);\n" +
                "var style = document.createElement('style');\n" +
                "style.type = 'text/css';\n" +
                "style.innerHTML = '@font-face {font-family: Haha; src: url(file:///android_asset/OpenSans-Bold.ttf); }  " +
                "body,html, a, h1,h2,h3,h4,h5,h6,div,p {font-family: Haha}';\n" +
                "parent.appendChild(style); \n" +
                "}"


        mBinding.web.loadUrl("javascript:" + addCSSRule)
        mBinding.web.loadUrl("javascript:myFunction()")
    }
}