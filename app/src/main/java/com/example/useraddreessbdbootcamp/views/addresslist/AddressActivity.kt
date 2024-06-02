package com.example.useraddreessbdbootcamp.views.addresslist

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.useraddreessbdbootcamp.R
import com.example.useraddreessbdbootcamp.database.AppDataBase
import com.example.useraddreessbdbootcamp.entities.Address
import com.example.useraddreessbdbootcamp.repository.MainRepository
import com.example.useraddreessbdbootcamp.viewmodels.address.AddressViewModel
import com.example.useraddreessbdbootcamp.viewmodels.address.AddressViewModelFactory

class AddressActivity : AppCompatActivity() {

    private lateinit var viewModelAddress: AddressViewModel
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        userId = intent.getLongExtra("USER_ID", -1L)

        val database = AppDataBase.getDatabase(application)
        val repository = MainRepository(database.userDao(), database.addressDao())
        val factory = AddressViewModelFactory(application, repository)
        viewModelAddress = ViewModelProvider(this, factory).get(AddressViewModel::class.java)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAddress)
        val adapter = AddressListAdapter { address ->

        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModelAddress.usersLV.observe(this) { addressList ->
            addressList?.let {
                adapter.submitList(it)
            }
        }

        val addAddressButton: Button = findViewById(R.id.btn_add_address)

        addAddressButton.setOnClickListener {
            showAlertDialogInsertAddress()
        }


    }

    private fun showAlertDialogInsertAddress() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar una dirección")

        // Layout para los inputs
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        // EditText para la calle
        val inputStreet = EditText(this)
        inputStreet.hint = "Calle"
        layout.addView(inputStreet)

        // EditText para la ciudad
        val inputCity = EditText(this)
        inputCity.hint = "Ciudad"
        layout.addView(inputCity)

        // EditText para el número
        val inputNumber = EditText(this)
        inputNumber.hint = "Número"
        inputNumber.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(inputNumber)

        builder.setView(layout)

        builder.setPositiveButton("OK"){ dialog, _ ->
            val street = inputStreet.text.toString()
            val city = inputCity.text.toString()
            val number = inputNumber.text.toString().toIntOrNull() ?: 0

            if(street.isNotEmpty() && city.isNotEmpty() && number > 0) {
                // Si los campos no están vacíos, insertamos la dirección
                val address = Address(userOwnerId = userId, street = street, city = city, number = number)
                viewModelAddress.insertAddress(address, userId) { addressId ->
                    if(addressId != -1L) {
                        Toast.makeText(this, "Dirección Agregada con ID $addressId", Toast.LENGTH_SHORT).show()
                        viewModelAddress.getAddressForUser(userId)
                    } else {
                        Toast.makeText(this, "Error al insertar dirección", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Si algún campo está vacío, mostramos un mensaje de error
                Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar"){ dialog , _-> dialog.cancel() }
        builder.show()
    }
}