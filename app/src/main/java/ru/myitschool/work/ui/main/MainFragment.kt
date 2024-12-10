package ru.myitschool.work.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import ru.myitschool.work.ApiService
import ru.myitschool.work.R
import ru.myitschool.work.User
import ru.myitschool.work.core.Constants.EMAIL_KEY
import ru.myitschool.work.core.Constants.SHARED_PREFS
import ru.myitschool.work.databinding.FragmentMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private lateinit var sharedpreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        sharedpreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)!!
        val apiInterface = RetrofitClient.getInstance().create(ApiService::class.java)

        val username = sharedpreferences.getString(EMAIL_KEY, null)

        val gson = Gson()

        lifecycleScope.launch(Dispatchers.IO) {
            val task = async {
                val response = apiInterface.info(""+username)
                return@async response
            }
            val info = gson.fromJson(task.await().string(), User::class.java)
            withContext(Dispatchers.Main) {
                Log.d("penis", info.name)

                binding.fullname.text = info.name
                binding.position.text = info.position
                val time = LocalDateTime.parse(info.lastVisit).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                binding.lastEntry.text = time.toString()
            }
        }
    }
}