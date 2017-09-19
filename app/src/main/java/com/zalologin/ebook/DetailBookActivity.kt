package com.zalologin.ebook

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
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
    private var desFolder: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_book)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_book)

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

    fun setWebViewListener() {
        mBinding.web.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val webView = view

                val varMySheet = "var mySheet = document.styleSheets[0];"
                val addCSSRule = "function addCSSRule(selector, newRule) {" +
                        "ruleIndex = mySheet.cssRules.length;" +
                        "mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);}"

                val insertRule1 = "addCSSRule('html', 'padding: 0px; height: " +
                        (webView!!.getMeasuredHeight() / getResources().getDisplayMetrics()
                                .density) + "px; -webkit-column-gap: 0px; -webkit-column-width: " +
                        webView.getMeasuredWidth() + "px;')"


                webView.loadUrl("javascript:" + varMySheet)
                webView.loadUrl("javascript:" + addCSSRule)
                webView.loadUrl("javascript:" + insertRule1)
                view.setVisibility(View.VISIBLE)
                super.onPageFinished(view, url)
            }
        })

        mBinding.web.setOnTouchListener(object : OnSwipeTouchListener(this){
            override fun onSwipeLeft() {
                Log.d("ttt", "previous view")
            }

            override fun onSwipeRight() {
                Log.d("ttt", "next view")
            }

            override fun newTouch() {
                super.newTouch()
            }
        })
    }


    fun readCss(): String {


        return "";
    }
}