package ru.myitschool.work.ui.main

import RetrofitClient.getInstance
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.squareup.picasso.Picasso
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
import ru.myitschool.work.core.env.EMAIL_KEY
import ru.myitschool.work.core.env.SHARED_PREFS
import ru.myitschool.work.databinding.FragmentMainBinding
import ru.myitschool.work.ui.RootActivity
import ru.myitschool.work.ui.login.LoginDestination
import ru.myitschool.work.ui.login.LoginFragment
import ru.myitschool.work.ui.qr.results.QrResultDestination
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import ru.myitschool.work.ui.qr.scan.QrScanFragment
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

        refresh()
        binding.logout.setOnClickListener(View.OnClickListener {
            with(sharedpreferences.edit()) {
                clear()
                apply()
            }
            (activity as RootActivity).navController?.popBackStack()
            (activity as RootActivity).navController?.navigate(LoginDestination)
        })

        binding.scan.setOnClickListener(View.OnClickListener {
            (activity as RootActivity).navController?.navigate(QrScanDestination)
        })

        binding.refresh.setOnClickListener(View.OnClickListener {
            refresh()
        })

        setFragmentResultListener(QrScanDestination.REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString("key_qr")
            if(result != null) {
                (activity as RootActivity).qrData = result.toString()
                (activity as RootActivity).navController?.navigate(QrResultDestination)
            }
        }
    }

    fun refresh() {
        val username = sharedpreferences.getString(EMAIL_KEY, null)
        val gson = Gson()

        MainScope().launch {
            val task = async {
                try {
                    val apiInterface = getInstance().create(ApiService::class.java)
                    val response = apiInterface.info(""+username)
                    return@async response
                } catch (e: Exception) {
                    binding.innerFields.visibility = View.GONE
                    binding.error.visibility = View.VISIBLE
                    binding.error.text = e.toString()
                    return@async null
                }
            }
            val info = gson.fromJson(task.await()?.string(), User::class.java)
            if(info != null) {
                withContext(Dispatchers.Main) {
                    binding.error.visibility = View.GONE
                    binding.innerFields.visibility = View.VISIBLE

                    binding.fullname.text = info.name
                    binding.position.text = info.position
                    val time = LocalDateTime.parse(info.lastVisit).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    binding.lastEntry.text = time.toString()
                    val imageView: ImageView = binding.photo
                    Picasso.get().load(info.photo).into(imageView)
                }
            }
        }
    }
}