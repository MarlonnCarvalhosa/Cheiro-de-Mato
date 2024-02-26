package com.marlonncarvalhosa.cheirodemato.util

import android.app.Activity
import android.content.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar
import com.marlonncarvalhosa.cheirodemato.R
import java.text.SimpleDateFormat
import java.util.*


//fun ImageView.loadImage(url: String?) {
//    Glide.with(this.context)
//        .load(url)
//        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//        .placeholder(R.drawable.ic_baseline_image_not_supported_24)
//        .centerCrop()
//        .into(this)
//    }

inline fun <reified T : Activity> Context.openActivity(
    options: Bundle? = null,
    finishWhenOpen: Boolean = false,
    finishStack: Boolean = false,
    @AnimRes enterAnim: Int = R.anim.anim_frag_fade_in,
    @AnimRes exitAnim: Int = R.anim.anim_frag_fade_out,
    noinline f: Intent.() -> Unit = {}
) {

    val intent = Intent(this, T::class.java)
    intent.f()
    if (finishStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.putExtra("options", options)
    startActivity(intent)

    if (finishWhenOpen) (this as Activity).finish()

    (this as Activity).overridePendingTransition(enterAnim, exitAnim)
}

fun View.viewVisible() {
    this.visibility = View.VISIBLE
}

fun View.viewInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.viewGone() {
    this.visibility = View.GONE
}

// Snackbar Extensions
fun View.showSnackbarRed(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(Color.RED)
    snackBar.show()
}

fun View.showSnackbar(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackBar.show()
}

fun View.snackBarWithAction(
    message: String, actionLabel: String,
    block: () -> Unit
) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionLabel) {
            block()
        }
}

fun alertNotification(ctx: Context, title: String, message: String) {
    val alertDialogBuilder = AlertDialog.Builder(ctx)
    alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    })
    alertDialogBuilder.setTitle(title)
    alertDialogBuilder.setMessage(Html.fromHtml(message))
        .show()
}

fun alert(ctx: Context, title: String, message: String) {
    val alertDialogBuilder = android.app.AlertDialog.Builder(ctx)
    val alertDialog = alertDialogBuilder.create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(Html.fromHtml(message))
    alertDialog.show()
}

//fun failureAlert(ctx: Context, title: String, message: String) {
//    val alertDialogBuilder = android.app.AlertDialog.Builder(ctx)
//    alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
//        ctx.openActivity<MainActivity>(finishWhenOpen = true, finishStack = true) {  }
//    })
//    val alertDialog = alertDialogBuilder.create()
//    alertDialog.setCancelable(false)
//    alertDialog.setTitle(title)
//    alertDialog.setMessage(Html.fromHtml(message))
//    alertDialog.show()
//}

//fun loadingDialog(ctx: Context): Dialog {
//    val loading = Dialog(ctx)
//    loading.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    loading.setContentView(R.layout.dialog_loading)
//    loading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    loading.setCanceledOnTouchOutside(false)
//    loading.setCancelable(false)
//    return loading
//}

fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun setupUnderLine(text: CharSequence?): CharSequence {
    val mSpannableString = SpannableString(text)
    mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
    return mSpannableString
}

fun hideKeyBoard(it: View) {
    try {
        val imm = it.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun setDateFormat(oldFormat: String, format: String, date: String, context: Context): String {
    val formatedDate = SimpleDateFormat(
        format,
        Locale("pt", "BR")
    )
    val oldFormatedDate = SimpleDateFormat(
        oldFormat,
        Locale("pt", "BR")
    )
    val initialDate = oldFormatedDate.parse(date)

    return formatedDate.format(initialDate!!)
}

fun ImageView.setSvgColor(@ColorRes color: Int) =
    setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP)

@RequiresApi(Build.VERSION_CODES.M)
fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(ClipboardManager::class.java)
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

