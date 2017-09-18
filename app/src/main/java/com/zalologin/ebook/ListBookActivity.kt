package com.zalologin.ebook

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zalologin.R
import com.zalologin.databinding.ActivityListBookBinding

/**
 * ListBookActivity
 *
 * Created by HOME on 9/14/2017.
 */
class ListBookActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityListBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_book)
    }
}