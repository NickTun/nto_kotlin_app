package ru.myitschool.work.ui.login

import RetrofitClient
import RetrofitClient.getInstance
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentLoginBinding
import ru.myitschool.work.ui.RootActivity
import ru.myitschool.work.ui.main.MainFragment
import ru.myitschool.work.ApiService
import ru.myitschool.work.core.env.EMAIL_KEY
import ru.myitschool.work.core.env.SHARED_PREFS
import ru.myitschool.work.ui.main.MainDestination
import ru.myitschool.work.ui.qr.scan.QrScanDestination

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var sharedpreferences: SharedPreferences

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.login.setOnClickListener(View.OnClickListener {
            handleLogin()
        })

        binding.username.addTextChangedListener {
            val name = binding.username.text.toString()
            if(name != "" && name.length >= 3 && !isNumericToX(name[0].toString()) && name.matches(Regex("^[A-Za-z0-9]+\$"))) {
                binding.login.isEnabled = true
                binding.login.isClickable = true
            } else {
                binding.login.isEnabled = false
                binding.login.isClickable = false
            }
        }
    }

    fun isNumericToX(toCheck: String): Boolean {
        return toCheck.toDoubleOrNull() != null
    }

    private fun handleLogin() {
        binding.loading.visibility = View.VISIBLE
        binding.fields.visibility = View.GONE

        sharedpreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)!!

        MainScope().launch {
            try {
                val apiInterface = getInstance().create(ApiService::class.java)
                val result = apiInterface.auth(binding.username.text.toString())
                with(sharedpreferences.edit()) {
                    putString(EMAIL_KEY, binding.username.text.toString())
                    apply()
                }
                (activity as RootActivity).navController?.popBackStack()
                (activity as RootActivity).navController?.navigate(MainDestination)
            } catch (e: Exception) {
                binding.loading.visibility = View.GONE
                binding.fields.visibility = View.VISIBLE
                binding.error.visibility = View.VISIBLE
                binding.error.text = e.toString()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}