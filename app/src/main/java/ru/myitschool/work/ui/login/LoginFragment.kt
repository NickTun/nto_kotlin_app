package ru.myitschool.work.ui.login

import RetrofitClient
import RetrofitClient.apiInterface
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
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
import ru.myitschool.work.core.Constants.EMAIL_KEY
import ru.myitschool.work.core.Constants.SHARED_PREFS

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
    }

    private fun handleLogin() {
        binding.loading.visibility = View.VISIBLE
        binding.username.visibility = View.GONE
        binding.login.visibility = View.GONE

        sharedpreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)!!

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = apiInterface.auth(binding.username.text.toString())
                with(sharedpreferences.edit()) {
                    putString(EMAIL_KEY, binding.username.text.toString())
                    apply()
                }
                (activity as RootActivity).navigate(R.id.nav_host_fragment, MainFragment())
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.loading.visibility = View.GONE
                    binding.error.visibility = View.VISIBLE
                    binding.error.text = e.toString()
                }

                Log.d("ueban", ""+e)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}