package com.example.testfragment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.lifecycle.LifecycleOwner
import com.example.testfragment.contract.CustomAction
import com.example.testfragment.contract.HasCustomAction
import com.example.testfragment.contract.HasCustomTitle
import com.example.testfragment.contract.Navigator
import com.example.testfragment.contract.ResultListener
import com.example.testfragment.databinding.ActivityMainBinding
import com.example.testfragment.fragment.AboutFragment
import com.example.testfragment.fragment.BoxFragment
import com.example.testfragment.fragment.BoxSelectionFragment
import com.example.testfragment.fragment.MenuFragment
import com.example.testfragment.fragment.OptionsFragment

class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    val fragmentListener = object : FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, MenuFragment())
                .commit()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        updateUi()
        return true
    }

    override fun openBoxSelected(options: Options) {
        launchFragment(BoxSelectionFragment.newInstance(options))
    }

    override fun openOptions(options: Options) {
    launchFragment(OptionsFragment.newInstance(options))
    }

    override fun openAbout() {
        launchFragment(AboutFragment())
    }

    override fun exitApp() {

    }

    override fun goToMenu() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun showCongratulationsScreen() {
        launchFragment(BoxFragment())
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun <T : Parcelable> setResult(result: T) {
        supportFragmentManager.setFragmentResult(
            result.javaClass.name,
            bundleOf(BUNDLE_STATE to result)
        )
    }

    override fun <T : Parcelable> listenerResult(
        clazz: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(clazz.name, owner) { requestKey, bundle ->
            listener.invoke(bundle.getParcelable(BUNDLE_STATE)!!)
        }
    }

    fun updateUi() {
        val fragment = currentFragment

        val backStackCount = supportFragmentManager.backStackEntryCount
        if (backStackCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        if (fragment is HasCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.toolbar.title = getString(R.string.fragment_navigation_example)
        }
        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }

    }



    fun createCustomToolbarAction(action: CustomAction) {
        binding.toolbar.menu.clear()

        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
        iconDrawable.setTint(Color.WHITE)

        val menuItem = binding.toolbar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }
    }

    fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    companion object {
        const val BUNDLE_STATE = "BUNDLE_STATE"
    }


}