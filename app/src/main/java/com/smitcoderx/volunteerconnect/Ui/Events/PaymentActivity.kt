package com.smitcoderx.volunteerconnect.Ui.Events

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.Model.Requests.RequestsData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Utils.Constants
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.ActivityPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener, ExternalWalletListener {

    private val args by navArgs<PaymentActivityArgs>()
    private val viewModel by viewModels<SingleEventViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var prefs: DataStoreUtil
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var requestData: RequestsData
    private lateinit var data: DataFetch


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = DataStoreUtil(this)
        viewModel.isNetworkConnectedLiveData.value = this.hasInternetConnection()
        homeViewModel.isNetworkConnectedLiveData.value = this.hasInternetConnection()
        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())
        data = args.eventData!!
        requestData = args.requestData!!
        Checkout.preload(applicationContext)


        handleCurrentUser()

        handleRequestStatus()

    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val data = RequestsData(
            answers = requestData.answers,
            eventId = requestData.eventId,
            eventName = requestData.eventName,
            recipient = requestData.recipient,
            status = requestData.status,
            transactionId = p1?.paymentId
        )
        viewModel.sendRequest(prefs.getToken().toString(), data)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "External", Toast.LENGTH_SHORT).show()
    }

    private fun handleRequestStatus() {
        viewModel.sendRequestLiveData.observe(this) {
            when (it) {
                is ResponseState.Success -> {
                    Toast.makeText(this, "Request Sended", Toast.LENGTH_SHORT).show()
                    findNavController(R.id.homeFragment).popBackStack()
                }

                is ResponseState.Loading -> {

                }

                is ResponseState.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleCurrentUser() {
        homeViewModel.userLiveData.observe(this) {
            when (it) {
                is ResponseState.Success -> {
                    val co = Checkout()

                    co.setKeyID("rzp_test_9KC8DVX2vBpEqL")

                    try {
                        val options = JSONObject()
                        options.put("name", "VolunteerConnect")
                        options.put("desc", "Event Charges")
                        options.put("currency", "INR")
                        options.put("amount", data.price?.times(100))

                        val prefill = JSONObject()
                        // Have to change this fields
                        prefill.put("name", it.data?.data?.name)
                        prefill.put("email", it.data?.data?.email)
                        prefill.put("contact", it.data?.data?.phoneNumber)
                        options.put("prefill", prefill)
                        co.open(this, options)
                    } catch (e: Exception) {
                        Log.d(Constants.TAG, "Payment Error: ${e.message}")
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                is ResponseState.Error -> {

                }

                is ResponseState.Loading -> {

                }
            }
        }
    }
}