package com.example.useraddreessbdbootcamp.views.addresslist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.useraddreessbdbootcamp.R
import com.example.useraddreessbdbootcamp.viewmodels.address.AddressViewModel
import com.example.useraddreessbdbootcamp.viewmodels.user.UserViewModel

class AddressActivity : AppCompatActivity() {

    private lateinit var viewModelAddress: AddressViewModel
    private var addressId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)


    }
}