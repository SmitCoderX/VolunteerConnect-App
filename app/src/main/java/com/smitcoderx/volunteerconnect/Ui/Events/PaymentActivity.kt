package com.smitcoderx.volunteerconnect.Ui.Events

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener,
    ExternalWalletListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val data = intent.extras?.getParcelable("data", DataFetch::class.java)
        Checkout.preload(applicationContext)
        val co = Checkout()

        co.setKeyID("rzp_test_9KC8DVX2vBpEqL")

        try {
            val options = JSONObject()
            options.put("name", "VolunteerConnect")
            options.put("desc", "Event Charges")
            options.put("currency", "INR")
            options.put("amount", data?.price?.times(100))

            val prefill = JSONObject()
            // Have to change this fields
            prefill.put("email", data?.email)
            prefill.put("contact", data?.phone)
            options.put("prefill", prefill)
            co.open(this, options)
        } catch (e: Exception) {
            Log.d(Constants.TAG, "Payment Error: ${e.message}")
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }


    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "External", Toast.LENGTH_SHORT).show()
    }
}