package com.example.waygo.view.tourism

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.waygo.R
import com.example.waygo.SessionPrefs
import com.example.waygo.databinding.ActivityAddTourismBinding
import com.example.waygo.util.reduceImage
import com.example.waygo.util.rotate
import com.example.waygo.util.uriToFile
import com.example.waygo.view.camX.CamX
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddTourismActivity : AppCompatActivity(){

    private lateinit var binding: ActivityAddTourismBinding
    private lateinit var sessionPrefs: SessionPrefs
    private lateinit var vm: AddTourismVM

    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTourismBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this)[AddTourismVM::class.java]
        vm.showToast.observe(this) {
            if (it) {
                vm.toastMsg.observe(this) { msg ->
                    showToast(true, msg)
                }
            }
        }
        vm.showLoading.observe(this) {
            loading(it)
        }
        vm.failed.observe(this) {
            closeActivity(it)
        }
        sessionPrefs = SessionPrefs(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        val description = binding.tiDesc
        binding.previewImage.setOnClickListener { showBottomNavBar() }
        binding.btnPost.setOnClickListener { upImage(description.text.toString()) }
        supportActionBar?.title = getString(R.string.add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun closeActivity(failed: Boolean) {
        if (!failed) {
            finish()
        }
    }

    private fun loading(show: Boolean) {
        if (show) {
            binding.progBar4.visibility = View.VISIBLE
        } else {
            binding.progBar4.visibility = View.GONE
        }
    }

    private fun showToast(show: Boolean, msg: String?) {
        if (show) {
            msg.let { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }
        }
    }



    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun showBottomNavBar() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_drawer, null)
        val btnClose = view.findViewById<Button>(R.id.btnDismiss)
        val btnCamera = view.findViewById<ImageView>(R.id.btnCam)
        val btnGallery = view.findViewById<ImageView>(R.id.btnGallery)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        btnCamera.setOnClickListener {
            startCameraX()
            dialog.dismiss()
        }
        btnGallery.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun startCameraX() {
        val intent = Intent(this, CamX::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotate(file, isBackCamera)
                getFile = file
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddTourismActivity)
                getFile = myFile
                binding.previewImage.setImageURI(uri)
            }
        }
    }

    private fun upImage(description: String) {
        if (getFile != null) {
            if (description != "") {
                val file = reduceImage(getFile as File)

                val desc =
                    description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                vm.uploadStory(
                    imageMultipart,
                    desc,
                    "Bearer " + sessionPrefs.getToken().api_key
                )
            } else {
                binding.tiDesc.error = "Wajib Diisi!"
            }
        } else {
            showToast(true, "Please add image")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
