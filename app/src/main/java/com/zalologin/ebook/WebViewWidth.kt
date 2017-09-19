package com.zalologin.ebook

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import java.util.jar.Attributes

/**
 * //Todo
 *
 * Created by HOME on 9/19/2017.
 */
class WebViewWidth : WebView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)
    constructor(context: Context, attributes: AttributeSet, def: Int) : super(context, attributes, def)

    fun getContentWidth(): Int = this.computeHorizontalScrollRange()

    fun getTotalHeight(): Int = this.computeVerticalScrollRange()
}