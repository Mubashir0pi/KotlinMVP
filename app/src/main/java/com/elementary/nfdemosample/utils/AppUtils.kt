package com.elementary.nfdemosample.utils

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.location.Geocoder
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.balysv.materialripple.MaterialRippleLayout
import com.elementary.nfdemosample.R
import com.tapadoo.alerter.Alerter
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

//Author Muhammad Mubashir 10/30/2018

class AppUtils(private val mContext: Context) {




    /****************************************
     * Get "String" to check is it null or not
     */
    fun getErrorDefinition(errorCode: Int): String {
        when (errorCode) {
            400 -> return "Missing required parameters or invalid parameters/values ($errorCode)"
            401 -> return "Incorrect email or password.($errorCode)"
            403 -> return "Account exists and user provided correct password, but account does not have a valid status.($errorCode)"
            500 -> return "Server Failure. ($errorCode)"
            else -> return "An error has occurred. ($errorCode)"
        }
    }

    fun checkDates(context: Context, Todate: String, FromDate: String): Boolean {
        //SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        var b = false
        try {
            if (dateFormatter.parse(Todate).before(dateFormatter.parse(FromDate))) {
                b = true
                //AppUtils.ShowToast(context, "Valid Date");//If start date is before end date
            } else b = dateFormatter.parse(Todate) == dateFormatter.parse(FromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return b
    }

    private fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = mContext.contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(index)
        }
    }

    companion object {

        private val latitude: Double = 0.toDouble()
        private val longitude: Double = 0.toDouble()
        private val pref: SharedPreferences? = null
        private val editor: SharedPreferences.Editor? = null
        private val locale: Locale? = null
        private val languageCode: String? = null


        /********************
         * For Bitmap Streaming
         */
        fun CopyStream(`is`: InputStream, os: OutputStream) {
            val buffer_size = 1024
            try {
                val bytes = ByteArray(buffer_size)
                while (true) {
                    val count = `is`.read(bytes, 0, buffer_size)
                    if (count == -1)
                        break
                    os.write(bytes, 0, count)
                }
            } catch (ex: Exception) {
            }

        }

        /********************
         * For Bitmap Rotation
         */
        fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }

        /**********************
         * For email validation
         */
        fun isEmailValid(email: String): Boolean {
            val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
            val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        /****************************************
         * Get "String" to check is it null or not
         */
        fun isSet(string: String?): Boolean {
            return string != null && string.trim { it <= ' ' }.length > 0
        }

        /**********************************
         * Get city and Country name method
         */
        fun getcity(context: Context, latitude: Double, longitude: Double): String {
            val results = StringBuilder()
            try {
                val geocoderr = Geocoder(context, Locale.getDefault())
                val addresses_city = geocoderr.getFromLocation(latitude, longitude, 1)
                if (addresses_city.size > 0) {
                    val address_city = addresses_city[0]
                    results.append(address_city.locality).append(" ,")
                    results.append(address_city.countryName)
                }
            } catch (e: IOException) {
                Log.e("tag", e.message)
            }

            return results.toString()
        }

        /***********************************
         * Get city and Country name method
         */
        fun getAddress(context: Context, latitude: Double, longitude: Double): String {
            val results = StringBuilder()
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses.size > 0) {
                    val address = addresses[0]
                    results.append(address.thoroughfare).append(" ,")
                    results.append(address.locality).append(" ,")
                    results.append(address.countryName)
                }
            } catch (e: IOException) {
                Log.e("tag", e.message)
            }

            return results.toString()
        }

        /***************
         * Alert Dialog
         */
        fun AlertEdit(context: Context, string: String) {
            val alertDialog = AlertDialog.Builder(context)
                    .create()
            alertDialog.setTitle(R.string.app_name)
            alertDialog.window!!.attributes.windowAnimations = R.style.CustomAlertDialogStyle
            alertDialog.setMessage(string)
            alertDialog.setButton("OK") { dialog, which -> alertDialog.dismiss() }
            alertDialog.show()
        }

        /*****************************************************
         * Apply Blink Effect On Every TextView And Button etc
         */
        fun buttonEffect(button: View?) {
            val color = Color.parseColor("#00000000")
            try {
                button?.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            v.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                            v.invalidate()
                        }
                        MotionEvent.ACTION_UP -> {
                            v.background.clearColorFilter()
                            v.invalidate()
                        }
                    }
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /******************************
         * Apply Effect On Every View(Button, TextView, LinearLayout etc)
         * Please Add In Gradle This Code First (compile 'com.balysv:material-ripple:1.0.2')
         */
        fun setRippleEffect(view: View) {
            MaterialRippleLayout.on(view)
                    .rippleColor(Color.parseColor("#0183b5"))
                    .rippleAlpha(0.1f)
                    .rippleHover(true)
                    .create()
        }

        /******************************
         * Apply Font On Whole Activity
         */
        fun applyFont(context: Context, root: View, fontPath: String) {
            try {
                if (root is ViewGroup) {
                    val childCount = root.childCount
                    for (i in 0 until childCount)
                        applyFont(context, root.getChildAt(i), fontPath)
                } else if (root is TextView)
                    root.typeface = Typeface.createFromAsset(context.assets, fontPath)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /****************************************
         * Set First Word Capitalize Of String
         */
        fun firstWordCapitalize(line: String?): String {
            return if (line != null && line.length > 0) {
                Character.toUpperCase(line[0]) + line.substring(1)
            } else {
                ""
            }
        }

        /*******************************************
         * Decode File For "Out Of Memory" Exception
         */
        fun decodeFile(f: File, WIDTH: Int, HIGHT: Int): Bitmap? {
            try {
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeStream(FileInputStream(f), null, o)
                // The new size we want to scale to
                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (o.outWidth / scale / 2 >= WIDTH && o.outHeight / scale / 2 >= HIGHT)
                    scale *= 2
                // Decode with inSampleSize
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                return BitmapFactory.decodeStream(FileInputStream(f), null, o2)
            } catch (e: FileNotFoundException) {
            }

            return null
        }

        /*************************
         * To set Image Rotation
         */
        fun getCameraPhotoOrientation(context: Context, imageUri: Uri,
                                      imagePath: String): Int {
            var rotate = 0
            try {
                context.contentResolver.notifyChange(imageUri, null)
                val imageFile = File(imagePath)
                val exif = ExifInterface(imageFile.absolutePath)
                val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return rotate
        }

        /******************************************
         * To set first letter capitalize of particular string.
         */
        fun capitalize(line: String): String {
            return Character.toUpperCase(line[0]) + line.substring(1)
        }


        /*********************************************************
         * Multiple texts with different color into single TextView
         */
        fun getColoredSpanned(text: String, color: String): String {
            return "<font color=$color>$text</font>"
        }

        fun parseTodaysDate(time: String): String? {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "MM-dd-yyyy"

            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
                //URLogs.i("mini", "Converted Date Today:" + str);
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        fun parseTodayCalenderDateOrTime(time: String): String? {
            val inputPattern = "EEE MMM d HH:mm:ss zzz yyyy"
            //String outputPattern = "dd-MM-yyyy";
            val outputPattern = "dd-MM-yyyy"

            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)

                Log.i("mini", "Converted Date Today:" + str!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        fun parseTodaysDateII(time: String): String? {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val outputPattern = "MM-dd-yyyy HH:mm "

            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        fun parseTime(time: String): String? {

            val inputPattern = "hh:mm a"
            val outputPattern = "HH:mm:ss"

            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        fun parseTimeReverse(time: String): String? {

            val outputPattern = "hh:mm a"
            val inputPattern = "HH:mm:ss"
            val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
            val outputFormat = SimpleDateFormat(outputPattern, Locale.US)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        fun parseTimeReverseForOpeningHour(time: String): String? {

            val outputPattern = "h:mm a"
            val inputPattern = "HH:mm:ss"

            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        fun parseTimeFromTimePicker(time: String): String? {
            val inputPattern = "EEE MMM d HH:mm:ss zzz yyyy"
            //String outputPattern = "HH:mm a";
            val outputPattern = "HH:mm:ss"

            val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
            val outputFormat = SimpleDateFormat(outputPattern, Locale.US)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        fun formatTimeFromServer(dateStr: String): String {
            var dateStr = dateStr
            val inputPattern = "yyyy-MM-dd hh:mm:ss"
            val outputPattern = "hh:mm aa"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            try {
                date = inputFormat.parse(dateStr)
                dateStr = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return dateStr
        }

        /**
         * @param s H:m timestamp, i.e. [Hour in day (0-23)]:[Minute in hour (0-59)]
         * @return total minutes after 00:00
         */
        fun parseHrstoMins(s: String): Int {
            val str = s.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val stringHourMins = str[0]
            val hourMin = stringHourMins.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val hour = Integer.parseInt(hourMin[0])
            val mins = Integer.parseInt(hourMin[1])
            val hoursInMins = hour * 60
            return hoursInMins + mins
        }


        /*******************************
         * Method for network is in working state or not.
         */
        fun isOnline(cntext: Context): Boolean {
            val cm = cntext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return (netInfo != null && netInfo.isConnected
                    && cm.activeNetworkInfo.isConnected)
        }

        /*******************************
         * Hide keyboard from edit text
         */
        fun hideKeyboard(context: Activity) {
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = context.currentFocus
            if (view != null) {
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        /*******************************
         * Show keyboard with edit text
         */
        fun showKeyboardWithFocus(v: View, a: Activity) {
            try {
                v.requestFocus()
                val imm = a.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
                a.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /*************************************
         * Show alerter with your custom messages
         */
        fun showDropDownNotification(mContext: Activity, title: String, message: String) {
            Alerter.create(mContext)
                    .setTitle(title)
                    .setText(message)
                    .setBackgroundColorRes(R.color.red)// or setBackgroundColorInt(Color.CYAN) // setBackgroundColorRes(R.color.colorAccent)
                    .setIcon(R.drawable.ic_alert)
                    .setIconColorFilter(0)
                    .setDuration(2000)
                    .show()
        }
        fun showDropDownSuccessNotification(mContext: Activity, title: String, message: String) {
            Alerter.create(mContext)
                    .setTitle(title)
                    .setText(message)
                    .setBackgroundColorRes(R.color.green)// or setBackgroundColorInt(Color.CYAN) // setBackgroundColorRes(R.color.colorAccent)
                    .setIcon(R.drawable.ic_success)
                    .setIconColorFilter(0)
                    .setDuration(2000)
                    .show()
        }
        fun showDropDownSuccessNotificationAndMoveToNextActivity(mContext: Activity, title: String, message: String, cls: Class<*>) {
            Alerter.create(mContext)
                    .setTitle(title)
                    .setText(message)
                    .setBackgroundColorRes(R.color.green)// or setBackgroundColorInt(Color.CYAN) // setBackgroundColorRes(R.color.colorAccent)
                    .setIcon(R.drawable.ic_success)
                    .setIconColorFilter(0)
                    .setDuration(2000)
                    .setOnShowListener {

                    }
                    .setOnHideListener {
                        val myIntent = Intent(mContext, cls)

                        mContext.startActivity(myIntent)
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        mContext.finish()
                    }
                    .show()
        }
        /*************************************
         * Show progress bar with callback true or false
         */
        private fun showProgress(btn: Button, progressBar: ProgressBar, progressVisible: Boolean) {
            btn.isEnabled = !progressVisible
            progressBar.visibility = if (progressVisible) View.VISIBLE else View.GONE
        }





        fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        /*************************************
         * Get bitmap from Uri
         */
        @Throws(IOException::class)
        fun getBitmapFromUri(uri: Uri, context: Context): Bitmap {
            val parcelFileDescriptor = context
                    .contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor!!
                    .fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        }


        /*****************************************************
         * Show error into edit text with your custom messages
         */
        fun ShowError(et: EditText, error: String) {
            if (et.length() == 0) {
                et.error = error
            }
        }

        /*************************************
         * Get bitmap from Uri
         */
        fun getBitmapFromURL(src: String): Bitmap? {
            try {
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                return BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                // URLogs exception
                return null
            }

        }

        fun resolveTransparentStatusBarFlag(ctx: Context): Int {
            val libs = ctx.packageManager.systemSharedLibraryNames
            var reflect: String? = null
            if (libs == null)
                return 0
            val SAMSUNG = "touchwiz"
            val SONY = "com.sonyericsson.navigationbar"
            for (lib in libs) {
                if (lib == SAMSUNG) {
                    reflect = "SYSTEM_UI_FLAG_TRANSPARENT_BACKGROUND"
                } else if (lib.startsWith(SONY)) {
                    reflect = "SYSTEM_UI_FLAG_TRANSPARENT"
                }
            }
            if (reflect == null)
                return 0
            try {
                val field = View::class.java!!.getField(reflect)
                if (field.getType() == Integer.TYPE) {
                    return field.getInt(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return 0
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        fun setTranslucentStatus(win: Window, on: Boolean) {
            val winParams = win.attributes
            val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }

        /*************************************
         * Get bitmap path with 100% quality*/
        @Throws(IOException::class)

        fun getImagePath(bitmap: Bitmap, quality: Int): String {
            val folder = File(Environment.getExternalStorageDirectory().absolutePath + Constants.DEFAULT_IMAGE_DIRECTORY)
            if (!folder.exists()) {
                folder.mkdirs()
            }

            val imagePath = Environment.getExternalStorageDirectory().absolutePath + Constants.DEFAULT_IMAGE_DIRECTORY + Constants.DEFAULT_IMAGE_NAME
            val f = File(imagePath)
            f.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
            val bitmapdata = bos.toByteArray()

            //write the bytes in file

            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return imagePath
        }

        fun getImagePath(selectedImage: Uri, ctx: Context): String? {
            var filePath: String? = null
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME)
            val cursor = ctx.contentResolver.query(selectedImage, filePathColumn, null, null, null)
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
            return filePath
        }

        /********************************************************
         * Decodes image and scales it to reduce memory consumption
         */
        //
        fun decodeFile(f: File): Bitmap? {
            try {
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeStream(FileInputStream(f), null, o)

                //Find the correct scale value. It should be the power of 2.
                val REQUIRED_SIZE = 200
                var width_tmp = o.outWidth
                var height_tmp = o.outHeight
                var scale = 1
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                        break
                    width_tmp /= 2
                    height_tmp /= 2
                    scale *= 2
                }

                //decode with inSampleSize
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                return BitmapFactory.decodeStream(FileInputStream(f), null, o2)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        /***********************************************************
         * Below code Working for scale image as aspect ratio:
         * Bitmap bitmapImage = BitmapFactory.decodeFile("Your path");
         * int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
         * Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
         * your_imageview.setImageBitmap(scaled);
         * Compress your image without losing quality like Whatsapp
         */
        fun compressImage(filePath: String): String {
            //String filePath = getRealPathFromURI(imageUri);
            var scaledBitmap: Bitmap? = null
            val options = BitmapFactory.Options()
            //by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
            //you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true
            var bmp = BitmapFactory.decodeFile(filePath, options)

            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            //max Height and width values of the compressed image is taken as 816x612
            val maxHeight = 816.0f
            val maxWidth = 612.0f
            var imgRatio = (actualWidth / actualHeight).toFloat()
            val maxRatio = maxWidth / maxHeight

            //width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }

            //setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
            //inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false
            //this options allow android to claim the bitmap memory if it runs low on memory
            options.inTempStorage = ByteArray(16 * 1024)
            try {
                //load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val canvas = Canvas(scaledBitmap!!)
            canvas.matrix = scaleMatrix
            canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

            //check the rotation of the image and display it properly
            try {
                val exif = ExifInterface(filePath) //imgFile.getAbsolutePath());
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                Log.d("EXIF", "Exif: $orientation")
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90f)
                } else if (orientation == 3) {
                    matrix.postRotate(180f)
                } else if (orientation == 8) {
                    matrix.postRotate(270f)
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true) // rotating bitmap
            } catch (e: Exception) {
                e.stackTrace
            }


            var out: FileOutputStream? = null
            val filename = filename
            try {
                out = FileOutputStream(filename)
                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return filename
        }

        val filename: String
            get() {
                val file = File(Environment.getExternalStorageDirectory().path, Constants.DEFAULT_IMAGE_DIRECTORY)
                if (!file.exists()) {
                    file.mkdirs()
                }
                return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"

            }

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize
        }

        fun formatDateFromServer(dateStr: String): String {
            var dateStr = dateStr
            val inputPattern = "yyyy-MM-dd hh:mm:ss"
            val outputPattern = "dd MMM yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            try {
                date = inputFormat.parse(dateStr)
                dateStr = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return dateStr
        }

        fun showPermissionAlertDialog(mContext: Activity) {
            val dialog = Dialog(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_permission_settings)
            val btSettings = dialog.findViewById<Button>(R.id.btSettings)

            btSettings.setOnClickListener {
                dialog.dismiss()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", mContext.packageName, null)
                intent.data = uri
                mContext.startActivity(intent)
            }

            dialog.show()

        }
    }

}
