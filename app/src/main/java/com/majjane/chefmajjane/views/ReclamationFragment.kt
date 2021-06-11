package com.majjane.chefmajjane.views

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.facebook.FacebookSdk.getApplicationContext
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.UploadRequestBody
import com.majjane.chefmajjane.databinding.FragmentReclamationBinding
import com.majjane.chefmajjane.network.ReclamationApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.ReclamationRepository
import com.majjane.chefmajjane.responses.ReclamationResponse
import com.majjane.chefmajjane.utils.*
import com.majjane.chefmajjane.viewmodel.ReclamationViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ReclamationFragment :
    BaseFragment<ReclamationViewModel, FragmentReclamationBinding, ReclamationRepository>(),
    UploadRequestBody.UploadCallBack {
    private lateinit var navController: NavController
    private var bitmap: Bitmap? = null
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            setToolbar(getString(R.string.reclamation))
            toolbarIcon?.setImageResource(R.drawable.back_arrow_ic)
            setToolbarHeight(170)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val list = listOf(getString(R.string.broken_item), getString(R.string.wrong_item))
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
        binding.spinner.adapter = adapter

        binding.ibSelectImage.setOnClickListener {
            if (isPermissionGranted()) {
                pickImageFromGallery()
            } else {
                takePermission()
            }
        }

        binding.sendBtn.setOnClickListener {
            uploadImage()
        }
        viewModel.reclamationLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    Log.d(TAG, "onViewCreated: ${it.data} ")
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
                is Resource.Loading -> {
                    Log.d(TAG, "onViewCreated: Loading...")
                }
            }
        })
    }

    private fun uploadImage() {
        if (imageUri == null) {
            requireView().snackbar(getString(R.string.select_image))
            return
        }
        if (binding.numCommande.getText().isEmpty()) {
            binding.numCommande.setError(getString(R.string.field_required))
            return
        }
        if (binding.commentaire.getText().isEmpty()) {
            binding.commentaire.setError(getString(R.string.field_required))
            return
        }
        val parcelFileDescriptor =
            getApplicationContext().contentResolver.openFileDescriptor(imageUri!!, "r", null)
                ?: return
        val file =
            File(context?.cacheDir, getApplicationContext().contentResolver.getFileName(imageUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)

        binding.progressBar5.visible(true)
        binding.sendBtn.enable(false)
        RemoteDataSource().buildApi(ReclamationApi::class.java).uploadReclamation(
            MultipartBody.Part.createFormData("image", file.name, body),
            RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                binding.commentaire.getText()
            ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "PBJRZBIYB"),
            RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                binding.spinner.selectedItem.toString()
            ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "3"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "0"),
        ).enqueue(object : retrofit2.Callback<ReclamationResponse> {
            override fun onResponse(
                call: Call<ReclamationResponse>,
                response: Response<ReclamationResponse>
            ) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 20000")
                    response.body()?.let {
                        if (it.succes == 1) {
                            binding.progressBar5.visible(false)
                            binding.sendBtn.enable(true)
                            Log.d(TAG, "onResponse: ${it.succes}")
                            requireView().snackbar(getString(R.string.claim_success))
                            Thread.sleep(1000)
                            navController.navigate(R.id.action_reclamationFragment_to_homeFragment)

                        } else if (it.comment != null || it.numCom != null || it.id != null || it.msj != null) {
                            binding.apply {
                                commentaire.setError(it.comment.toString())
                                numCommande.setError(it.numCom.toString())
                            }
                        }
                    }

                }
            }

            override fun onFailure(call: Call<ReclamationResponse>, t: Throwable) {
                binding.progressBar5.visible(false)
                if (t is HttpException) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.unknown_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private val TAG = "ReclamationFragment"

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readExternalStorage = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            readExternalStorage == PackageManager.PERMISSION_GRANTED
        }
    }

    private var imageUri: Uri? = null
    private fun takePermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.ca tegory,DEFAULT")
                intent.data = Uri.parse(String.format("package%s", requireContext().packageName))
                startActivityForResult(intent, 100)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = "Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION"
                startActivityForResult(intent, 100)
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        pickImageFromGallery()
                    } else {
                        takePermission()
                    }
                }
            } else if (requestCode == 102) {
                if (data != null) {
                    val uri = data.data
                    if (uri != null) {
                        binding.ivSelectImage.setImageURI(uri)
                        binding.ibSelectImage.alpha = 0F
                        bitmap = MediaStore.Images.Media.getBitmap(
                            getApplicationContext().contentResolver,
                            uri
                        )
                        imageUri = uri
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (requestCode == 101) {
                val readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (readExternalStorage) {
                    pickImageFromGallery()
                } else {
                    takePermission()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 102)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReclamationBinding =
        FragmentReclamationBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<ReclamationViewModel> =
        ReclamationViewModel::class.java

    override fun getFragmentRepository(): ReclamationRepository =
        ReclamationRepository(RemoteDataSource().buildApi(ReclamationApi::class.java))

    override fun onProgressUpdate(percentage: Int) {

    }


}