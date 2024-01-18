package com.example.testfragment.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.testfragment.Options

typealias ResultListener<T> = (T) -> Unit

fun Fragment.navigator(): Navigator {

    return requireActivity() as Navigator

}

interface Navigator {

    fun openBoxSelected(options: Options)

    fun openOptions(options: Options)

    fun openAbout()

    fun exitApp()

    fun goBack()

    fun goToMenu()

    fun showCongratulationsScreen()

    fun <T: Parcelable>setResult(result: T)

    fun <T: Parcelable>listenerResult(clazz: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>)
}