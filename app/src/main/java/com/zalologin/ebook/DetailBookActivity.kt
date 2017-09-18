package com.zalologin.ebook

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.zalologin.R
import com.zalologin.databinding.ActivityDetailBookBinding
import com.zalologin.databinding.ActivityListBookBinding
import nl.siegmann.epublib.domain.TOCReference
import android.webkit.WebView
import android.webkit.WebViewClient


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

        mBinding.web.loadUrl("file://" + desFolder + tocReference.resource.href)
    }


    fun readCss() :String{


        return "";
    }

}