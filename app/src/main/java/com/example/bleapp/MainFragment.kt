package com.example.bleapp

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bleapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var mContext: Context

    // permission
    private val ENABLE_BLUETOOTH_REQ_CODE = 1

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context?.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        mContext = requireContext()
        setupListener()
        return binding.root
    }

    private fun setupListener() {
        binding.button.setOnClickListener {
            checkGrantedPermission()
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQ_CODE)
                startForResult.launch(enableBtIntent)
            }
        }
    }

    private fun checkGrantedPermission() {
        when {
            ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PERMISSION_GRANTED -> {
                Log.i("Bluetooth", "permission already granted")
            }
            else -> {
                Log.i("Bluetooth", "please grant permission")
                activity?.let { it1 ->
                    ActivityCompat.requestPermissions(
                        it1,
                        arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION),
                        ENABLE_BLUETOOTH_REQ_CODE
                    )
                }
            }
        }
    }

    val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.i("Bluetooth", "good")
        }
    }
}
