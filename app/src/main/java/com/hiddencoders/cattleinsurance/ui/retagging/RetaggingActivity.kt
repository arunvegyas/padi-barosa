package com.hiddencoders.cattleinsurance.ui.retagging

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import com.hiddencoders.cattleinsurance.databinding.ActivityRetaggingBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.claim.ClaimActivity
import com.hiddencoders.cattleinsurance.ui.tagging.EditTaggingActivity
import com.hiddencoders.cattleinsurance.ui.tagging.TaggingViewmodel
import com.hiddencoders.cattleinsurance.ui.utilis.Utilities
import com.hiddencoders.cattleinsurance.ui.utilis.convertDateFormat
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RetaggingActivity : BaseClass() {
    private lateinit var binding: ActivityRetaggingBinding
    private val taggingViewmodel by viewModels<TaggingViewmodel>()
    private lateinit var filePathImageCamera: File
    private lateinit var filePathImageCamera1: File
    private var image1: String = ""
    private var image2: String = ""
    private var CATID: String = ""

    @Inject
    lateinit var apiServices: ApiServices
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetaggingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    private fun setData() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btSave.setOnClickListener {
            if (binding.etReTagNo.text.isEmpty()) {
                binding.etReTagNo.error = "Please enter valid TAG NO"
            }else if (binding.etRCTNo.text.isEmpty()) {
                binding.etRCTNo.error = "Please enter valid Receipt no"
            } else if (binding.etRemarks.text.isEmpty()) {
                binding.etRemarks.error = "Please enter remarks"
            } else if (image1.isEmpty()) {
                showToast(this, "Please upload Image1")
            } else if (image2.isEmpty()) {
                showToast(this, "Please upload Image2")
            }  else {
                var dt = Date()
                val c = Calendar.getInstance()
                c.time = dt
                dt = c.time
                val format2 = SimpleDateFormat("yyyy-MM-dd")
                val formatted1 = format2.format(dt)

                val jsonObject = JsonObject()
                jsonObject.addProperty("CATID", CATID)
                jsonObject.addProperty("UserName", userSession.getUsername())
                jsonObject.addProperty("bcode", userSession.getUserId())
                jsonObject.addProperty("TAGNO", binding.etTagNo.text.toString().trim())
                jsonObject.addProperty("RCTNO", binding.etRCTNo.text.toString().trim())
                jsonObject.addProperty("IMAGE1_IMAGE", image1)
                jsonObject.addProperty("IMAGE2_IMAGE", image2)
                jsonObject.addProperty("TDATE",formatted1)
                uploadDataToServer(jsonObject)
            }
        }

        binding.btAddFarmer.setOnClickListener {
            if (binding.etTagNo.text.toString().isNotEmpty() && binding.etTagNo.text.length >= 6) {
                taggingViewmodel.getTagByTagNo(binding.etTagNo.text.toString()).observeForever {
                    if (it.code == "0") {
                        val data = it.Tags[0]
                        binding.llView.visibility = View.VISIBLE
                        CATID = data.CATID.toString()
                        binding.tvTagNo.text = "Tag No : ${data.TAGNO}"
                        binding.tvRctNo.text = "Receipt No : ${data.RCTNO}"
                        if (data.BREED == "1") {
                            binding.tvBuffaleBreed.text = "Breed : Local"
                        } else {
                            binding.tvBuffaleBreed.text = "Breed : Cross Breed"
                        }
                        if (data.BUFF_COW == "1") {
                            binding.tvBuffaleType.text = "Buffalo"
                        } else {
                            binding.tvBuffaleType.text = "Cow"
                        }
                        if (data.ANIMAL_STATUS==1) {
                            binding.tvCreatedBy.text = "Status : Milking"
                        } else {
                            binding.tvCreatedBy.text = "Status : Pregnent"
                        }
                        binding.tvDate.text =
                            convertDateFormat("yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy", data.TDATE)
                        binding.tvMilkYeild.text = "Milk Yeild : ${data.MILK_YIELD} Ltrs"
                        binding.tvPremium.text = "Premium : ${data.PREMIUM.toString()} Rs"

                        if (data.IMAGE1_IMAGE != null && data.IMAGE2_IMAGE != null) {

                            lifecycleScope.launch {
                                Glide.with(this@RetaggingActivity).load(decode(data.IMAGE1_IMAGE))
                                    .into(binding.ivImage1)
                                Glide.with(this@RetaggingActivity).load(decode(data.IMAGE2_IMAGE))
                                    .into(binding.ivImage2)

                                binding.ivImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                                binding.ivImage1.setPadding(0, 0, 0, 0)
                                binding.ivImage2.scaleType = ImageView.ScaleType.CENTER_CROP
                                binding.ivImage2.setPadding(0, 0, 0, 0)
                            }

                        }
                        if (data.ISAPPROVED) {
                            binding.tvApproved.text = "Approved"
                        } else {
                            binding.tvApproved.text = "Pending"
                        }
                    } else{
                        binding.llView.visibility = View.GONE
                    }
                }
            } else {
                binding.etTagNo.error = "Please enter valid TAG NO"
            }
        }
        binding.ivImage1.setOnClickListener {
            photoCameraIntent(1)
        }
        binding.ivImage2.setOnClickListener {
            photoCameraIntent(2)
        }
    }

    private fun photoCameraIntent(type: Int) {
        try {
            if (type == 1) {
                filePathImageCamera = createImageFile()
            } else {
                filePathImageCamera1 = createImageFile()
            }
        } catch (ex: IOException) {
            // Error occurred while creating the File
        }
        val it = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI: Uri = if (type == 1) FileProvider.getUriForFile(
            this, "com.hiddencoders.cattleinsurance" + ".provider",
            filePathImageCamera
        ) else {
            FileProvider.getUriForFile(
                this, "com.hiddencoders.cattleinsurance" + ".provider",
                filePathImageCamera1
            )
        }
        //        Uri photoURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        it.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        if (type == 1) {
            startActivityForResult(it, EditTaggingActivity.IMAGE_CAMERA_REQUEST)
        } else {
            startActivityForResult(it, EditTaggingActivity.IMAGE_CAMERA_REQUEST2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EditTaggingActivity.IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (filePathImageCamera.exists()) {
                lifecycleScope.launch {
                    val filePath =
                        Compressor.compress(this@RetaggingActivity, filePathImageCamera) {
                            resolution(480, 480)
                            quality(80)
                            format(Bitmap.CompressFormat.JPEG)
                            size(209715)
                        }
                    Glide.with(this@RetaggingActivity).load(filePath).into(binding.ivImage1)
                    binding.ivImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.ivImage1.setPadding(0, 0, 0, 0)
                    image1 = encode(filePath)
                }
            }
        } else if (requestCode == EditTaggingActivity.IMAGE_CAMERA_REQUEST2 && resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val filePath = Compressor.compress(this@RetaggingActivity, filePathImageCamera1) {
                    resolution(480, 480)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(209715)
                }
                Glide.with(this@RetaggingActivity).load(filePath).into(binding.ivImage2)
                binding.ivImage2.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.ivImage2.setPadding(0, 0, 0, 0)
                image2 = encode(filePath)
            }
        }
    }

    private fun uploadDataToServer(jsonObject: JsonObject) {
        disposable = apiServices.uploadReTagFormToServer(jsonObject).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code==0){
                    Utilities.showAlertDialog(this, "ReTagging", "Updated Successfully")
                    Handler().postDelayed({
                        startActivity(Intent(this, RetaggingActivity::class.java))
                    }, 3000)
                }
            }, {
                Log.d("TAG", "uploadDataToServer: ${it.message}")
            })
    }
}