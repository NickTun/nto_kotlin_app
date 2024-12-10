package ru.myitschool.work.ui

import RetrofitClient
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.ui.login.LoginDestination
import ru.myitschool.work.ui.login.LoginFragment
import ru.myitschool.work.ui.main.MainDestination
import ru.myitschool.work.ui.main.MainFragment
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import ru.myitschool.work.ui.qr.scan.QrScanFragment
import ru.myitschool.work.core.Constants.SHARED_PREFS
import ru.myitschool.work.core.Constants.EMAIL_KEY

// НЕ ИЗМЕНЯЙТЕ НАЗВАНИЕ КЛАССА!
@AndroidEntryPoint
class RootActivity : AppCompatActivity() {
    private lateinit var sharedpreferences: SharedPreferences
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        username = sharedpreferences.getString(EMAIL_KEY, null)

//        with(sharedpreferences.edit()) {
//            clear()
//            apply()
//        }

        setContentView(R.layout.activity_root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        if (navHostFragment != null) {
            val navController = navHostFragment.navController
            navController.graph = navController.createGraph(
                startDestination = if (username != null) MainDestination else LoginDestination
            ) {
                fragment<LoginFragment, LoginDestination>()
                fragment<MainFragment, MainDestination>()
                fragment<QrScanFragment, QrScanDestination>()
            }
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onSupportNavigateUp()
                }
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val popBackResult = if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            false
        }
        return popBackResult || super.onSupportNavigateUp()
    }

    public fun navigate(a: Int, b: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(a, b)
            .commit()
    }
}