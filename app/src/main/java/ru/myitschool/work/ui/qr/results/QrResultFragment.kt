package ru.myitschool.work.ui.qr.results

import RetrofitClient.getInstance
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.myitschool.work.ApiService
import ru.myitschool.work.Data
import ru.myitschool.work.R
import ru.myitschool.work.core.env.EMAIL_KEY
import ru.myitschool.work.core.env.SHARED_PREFS
import ru.myitschool.work.databinding.FragmentMainBinding
import ru.myitschool.work.databinding.FragmentQrResultBinding
import ru.myitschool.work.databinding.FragmentQrScanBinding
import ru.myitschool.work.ui.RootActivity
import ru.myitschool.work.ui.login.LoginDestination
import ru.myitschool.work.ui.main.MainDestination
import ru.myitschool.work.ui.qr.scan.QrScanDestination

class QrResultFragment: Fragment(R.layout.fragment_qr_result) {
    private var _binding: FragmentQrResultBinding? = null
    private lateinit var sharedpreferences: SharedPreferences
    private val binding: FragmentQrResultBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQrResultBinding.bind(view)

        sharedpreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)!!

        val username = sharedpreferences.getString(EMAIL_KEY, null)
        val data = (activity as RootActivity).qrData

        if(data == "" || data == "Отмена") {
            binding.result.text = resources.getString(R.string.canceled)
        } else {
            MainScope().launch {
                try {
                    val apiInterface = getInstance().create(ApiService::class.java)
                    val req: Data = Data(data)
                    val result = apiInterface.open(req, ""+username)
                    binding.result.text = resources.getString(R.string.success)
                } catch (e: Exception) {
                    binding.result.text = resources.getString(R.string.smthwr)
                }
            }
        }

        (activity as RootActivity).qrData = "null"

        binding.close.setOnClickListener(View.OnClickListener {
           goBack()
        })
    }

    private fun goBack() {
        (activity as RootActivity).navController?.popBackStack()
    }
}