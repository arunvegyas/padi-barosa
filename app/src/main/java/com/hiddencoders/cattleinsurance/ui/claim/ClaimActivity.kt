package com.hiddencoders.cattleinsurance.ui.claim

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import com.hiddencoders.cattleinsurance.databinding.ActivityClaimBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.retagging.RetaggingActivity
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
class ClaimActivity : BaseClass() {
    private lateinit var binding: ActivityClaimBinding
    private val taggingViewmodel by viewModels<TaggingViewmodel>()
    private lateinit var filePathImageCamera: File
    private lateinit var filePathImageCamera1: File
    private var image1: String = ""
    private var image2: String = ""
    private var CATID: String = ""
    var mSelectedMonth: String = ""
    var mSelectedDay: String = ""
    var mSelectedYear: String = ""
    var mDOB: String = ""
    private var mYear: Int = 0
    private var mMOnth_val: Int = 0

    @Inject
    lateinit var apiServices: ApiServices
    private lateinit var disposable: Disposable

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.etIncDate.setOnClickListener {
            openDateDialog()
        }
        binding.btSave.setOnClickListener {
            if (mDOB.isEmpty()) {
                showToast(this, "Please select Incident date")
            } else if (binding.etRemarks.text.isEmpty()) {
                binding.etRemarks.error = "Please enter remarks"
            } else if (image1.isEmpty()) {
                showToast(this, "Please upload Image1")
            } else if (image2.isEmpty()) {
                showToast(this, "Please upload Image2")
            } else {
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
                jsonObject.addProperty("CLAIM_DATE", formatted1)
                jsonObject.addProperty("INC_DATE", mDOB)
                uploadDataToServer(jsonObject)
            }
        }

        binding.btAddFarmer.setOnClickListener {
            if (binding.etTagNo.text.toString().isNotEmpty()) {
                taggingViewmodel.getClaimByTagNo(binding.etTagNo.text.toString()).observeForever {
                    if (it.code == "0") {
                        val data = it.Claim[0]
                        binding.llView.visibility = View.VISIBLE
                        CATID = data.CATID.toString()
                        binding.tvFarmerName.text = data.FARMERNAME
                        binding.tvTagNo.text = "Tag No : ${data.TAGNO}"
//                        binding.tvRctNo.text = "Receipt No : ${data.RCTNO}"
                        binding.tvRctNo.visibility = View.GONE
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
                        if (data.ANIMAL_STATUS == 1) {
                            binding.tvCreatedBy.text = "Status : Milking"
                        } else {
                            binding.tvCreatedBy.text = "Status : Pregnent"
                        }
//                        binding.tvDate.text =
//                            convertDateFormat("yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy", data.TDATE)
                        binding.tvMilkYeild.text = "Milk Yeild : ${data.MILK_YIELD} Ltrs"
                        binding.tvPremium.text = "Premium : ${data.PREMIUM.toString()} Rs"

                        if (data.IMAGE1_IMAGE != null && data.IMAGE2_IMAGE != null) {

                            lifecycleScope.launch {
                                if (isBase64(data.IMAGE1_IMAGE) && isBase64(data.IMAGE2_IMAGE)) {
                                    Glide.with(this@ClaimActivity).load(decode(data.IMAGE1_IMAGE))
                                        .into(binding.ivImage1)
                                    Glide.with(this@ClaimActivity).load(decode(data.IMAGE2_IMAGE))
                                        .into(binding.ivImage2)

                                    binding.ivImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                                    binding.ivImage1.setPadding(0, 0, 0, 0)
                                    binding.ivImage2.scaleType = ImageView.ScaleType.CENTER_CROP
                                    binding.ivImage2.setPadding(0, 0, 0, 0)
                                }

                            }

                        }
                        if (data.ISAPPROVED) {
                            binding.tvApproved.text = "Approved"
                        } else {
                            binding.tvApproved.text = "Pending"
                        }
                    } else {
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

    private fun openDateDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.datedialog)
        val picker_dailog = dialog.findViewById(R.id.picker_dicker) as DatePicker
        val body = dialog.findViewById(R.id.tvTitle) as TextView
        body.text = "Enter From Date"
        val ok_dialog = dialog.findViewById(R.id.tvOk) as TextView
        val cancel_dialog = dialog.findViewById(R.id.tvCancel) as TextView

        val ca = Calendar.getInstance()

        ca.add(Calendar.DATE, -1)
        picker_dailog.minDate = ca.timeInMillis
        picker_dailog.maxDate = Date().time
        mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        var selectedYear: String = ""
        val mDay = ca[Calendar.DAY_OF_MONTH]
        picker_dailog.init(mYear, mMonth, mDay,
            DatePicker.OnDateChangedListener { view, year, month, day ->

                mSelectedYear = year.toString()

                if (month < 9) {
                    val mMonth = month + 1
                    mSelectedMonth = "0$mMonth"
                }
                if (month >= 9) {
                    val mMonth = month + 1
                    mSelectedMonth = mMonth.toString()
                }

                if (day < 10) {
                    mSelectedDay = "0$day"
                }
                if (day >= 10) {
                    mSelectedDay = day.toString()
                }
                mDOB = "$mSelectedYear-$mSelectedMonth-$mSelectedDay"
                binding.etIncDate.setText(mDOB)

            })

        if (binding.etDate.text!!.isNotEmpty()) {
            val mYearEdit = binding.etDate.text.toString().trim()
            val items1: List<String> = mYearEdit.split("-")
            val date1 = items1[0]
            mMOnth_val = items1[1].toInt()
            val year = items1[2]
            val mMonth_selected = mMOnth_val - 1

            picker_dailog.updateDate(
                Integer.parseInt(date1),
                mMonth_selected,
                Integer.parseInt(year)
            );
        }

        ok_dialog.setOnClickListener {

            dialog.dismiss()

        }
        cancel_dialog.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EditTaggingActivity.IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (filePathImageCamera.exists()) {
                lifecycleScope.launch {
                    val filePath =
                        Compressor.compress(this@ClaimActivity, filePathImageCamera) {
                            resolution(480, 480)
                            quality(80)
                            format(Bitmap.CompressFormat.JPEG)
                            size(209715)
                        }
                    Glide.with(this@ClaimActivity).load(filePath).into(binding.ivImage1)
                    binding.ivImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.ivImage1.setPadding(0, 0, 0, 0)
                    image1 = encode(filePath)
                }
            }
        } else if (requestCode == EditTaggingActivity.IMAGE_CAMERA_REQUEST2 && resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val filePath = Compressor.compress(this@ClaimActivity, filePathImageCamera1) {
                    resolution(480, 480)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(209715)
                }
                Glide.with(this@ClaimActivity).load(filePath).into(binding.ivImage2)
                binding.ivImage2.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.ivImage2.setPadding(0, 0, 0, 0)
                image2 = encode(filePath)
            }
        }
    }

    private fun uploadDataToServer(jsonObject: JsonObject) {
        disposable = apiServices.uploadClaimFormToServer(jsonObject).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    Utilities.showAlertDialog(this, "Claim", "Updated Successfully")
                    Handler().postDelayed({
                        startActivity(Intent(this, RetaggingActivity::class.java))
                    }, 3000)
                }
            }, {
                Log.d("TAG", "uploadDataToServer: ${it.message}")
            })
    }
}