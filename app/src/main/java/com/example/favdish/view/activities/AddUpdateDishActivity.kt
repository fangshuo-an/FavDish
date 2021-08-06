package com.example.favdish.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.ActivityAddUpdateDishBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding
import com.example.favdish.databinding.DialogCustomListBinding
import com.example.favdish.model.entites.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.view.adapters.CustomListItemAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mbinding: ActivityAddUpdateDishBinding
    private var mImagePath: String = ""
    private lateinit var mCustomListDialog: Dialog
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        setupActionBar()

        mbinding.ivAddDishImage.setOnClickListener(this)

        mbinding.edType.setOnClickListener(this)
        mbinding.edCategory.setOnClickListener(this)
        mbinding.edCookingTimeInMinutes.setOnClickListener(this)

        mbinding.btnAddDish.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(mbinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mbinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }
                R.id.ed_type -> {
                    customItemsListDialog(
                        resources.getString(R.string.title_select_dish_type),
                        Constants.dishTypes(),
                        Constants.DISH_TYPE
                    )
                    return
                }
                R.id.ed_category -> {/*
                    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)*/
                    customItemsListDialog(
                        resources.getString(R.string.title_select_dish_category),
                        Constants.dishCategory(),
                        Constants.DISH_CATEGORY
                    )
                    return
                }

                R.id.ed_cooking_time_in_minutes -> {
                    customItemsListDialog(
                        resources.getString(R.string.title_select_dish_cooking_time),
                        Constants.dishCookTime(),
                        Constants.DISH_COOKING_TIME
                    )
                    return
                }
                R.id.btn_add_dish -> {
                    //without space
                    val title = mbinding.edTitle.text.toString().trim { it <= ' ' }
                    val type = mbinding.edType.text.toString().trim { it <= ' ' }
                    val category = mbinding.edCategory.text.toString().trim { it <= ' ' }
                    val ingredients = mbinding.edIngredients.text.toString().trim { it <= ' ' }
                    val cookingTimeInMinutes =
                        mbinding.edCookingTimeInMinutes.text.toString().trim { it <= ' ' }
                    val cookingDirection =
                        mbinding.edDirectionToCook.text.toString().trim { it <= ' ' }

                    when {
                        TextUtils.isEmpty(mImagePath) -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.err_msg_select_dish_image),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.err_msg_enter_dish_title),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.err_msg_select_dish_type),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(category) -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.err_msg_select_dish_category),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.err_msg_enter_dish_ingredients),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingTimeInMinutes) -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.err_msg_select_dish_cooking_time),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.err_msg_enter_dish_cooking_instruction),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            val favDishDetails: FavDish = FavDish(
                                mImagePath,
                                Constants.DISH_IMAGE_SOURCE_LOCAL,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTimeInMinutes,
                                cookingDirection,
                                false
                            )

                            mFavDishViewModel.insert(favDishDetails)
                            Toast.makeText(this, "add dish details.", Toast.LENGTH_SHORT).show()
                            finish()    //close activity
                        }
                    }
                }
            }
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        //unclick event
        binding.tvCamera.setOnClickListener {

            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,  not required in latest api levels
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.apply {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //capture a image
                            startActivityForResult(intent, CAMERA)

                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationaleDialogForPermission()

                }
            }).onSameThread().check()

            dialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this@AddUpdateDishActivity)
                .withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(report: PermissionGrantedResponse?) {

                        val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(intent, GALLERY)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            "denied to select image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        showRationaleDialogForPermission()
                    }

                }).onSameThread().check()
            dialog.dismiss()
        }
        dialog.show()
    }

    //click the list, set edtitext to be item
    fun selectedListItem(item: String, selection: String) {
        when (selection) {
            Constants.DISH_TYPE -> {
                mCustomListDialog.dismiss()
                mbinding.edType.setText(item)
            }
            Constants.DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                mbinding.edCategory.setText(item)
            }
            else -> {
                mCustomListDialog.dismiss()
                mbinding.edCookingTimeInMinutes.setText(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras?.get("data") as Bitmap
//                    mbinding.ivDishImage.setImageBitmap(thumbnail)

                    Glide.with(this).load(thumbnail)
                        .centerCrop()
                        .into(mbinding.ivDishImage)

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath", mImagePath)

                    mbinding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_vector_edit
                        )
                    )
                }
            }
            if (requestCode == GALLERY) {
                data?.let {
                    //data as result         data return uri
                    val selectedPhotoUri = data.data

//                    mbinding.ivDishImage.setImageURI(selectedPhotoUri)

                    Glide.with(this).load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG", "Error loading image")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)
                                    Log.i("ImagePath", mImagePath)
                                }
                                return false
                            }
                        })
                        .into(mbinding.ivDishImage)

                    mbinding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_vector_edit
                        )
                    )
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("cancelled", "User cancelled image selection")
        }
    }

    private fun showRationaleDialogForPermission() {
        AlertDialog.Builder(this).setMessage("denied")
            .setPositiveButton("GO TO SETTING") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(
            IMAGE_DIRECTORY,
            Context.MODE_PRIVATE
        )    //Retrieve, creating if needed, a new directory in which the application can place its own custom data files.
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()   //flush the stream
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath //return the directory in the file
    }

    private fun customItemsListDialog(title: String, itemList: List<String>, selection: String) {
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title

        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListItemAdapter(this, itemList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2

        private const val IMAGE_DIRECTORY = "FavDishImage"
    }
}