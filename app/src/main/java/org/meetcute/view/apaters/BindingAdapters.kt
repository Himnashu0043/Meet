package org.meetcute.view.apaters

import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.meetcute.R
import org.meetcute.appUtils.EditTextCountryModel
import org.meetcute.appUtils.EditTextModel
import org.meetcute.view.customViews.AppEditText
import org.meetcute.view.customViews.AppEditTextCountry
import org.meetcute.view.customViews.AppEditTextDropDown
import org.meetcute.view.customViews.AppEditTextMessage
import org.meetcute.view.customViews.AppEditTextPassword
import org.meetcute.view.customViews.AppButton
import org.meetcute.view.customViews.AppThreeCircleAnimation


@BindingAdapter("app:editTextModel")
fun setEditTextModel(view: AppEditText?, m: EditTextModel?) {
    m?.let {
        view?.setModel(m)
    }
}


@BindingAdapter("app:editTextModelPassword")
fun setEditTextModel(view: AppEditTextPassword?, m: EditTextModel?) {
    m?.let {
        view?.setModel(m)
    }
}

@BindingAdapter("app:editTextModelCountry")
fun setEditTextModel(view: AppEditTextCountry?, m: EditTextCountryModel?) {
    m?.let {
        view?.setModel(m)
    }
}


@BindingAdapter("app:editTextModelDropDown")
fun setEditTextModel(view: AppEditTextDropDown?, m: EditTextModel?) {
    m?.let {
        view?.setModel(m)
    }
}

@BindingAdapter("app:editTextModelMessage")
fun setEditTextModel(view: AppEditTextMessage?, m: EditTextModel?) {
    m?.let {
        view?.setModel(m)
    }
}


@BindingAdapter("app:imageRes")
fun setEditTextModel(view: ImageView?, res: Int) {
    view?.setImageResource(res)
}


@BindingAdapter("app:isSelected")
fun setGenderSelector(view: ImageView, isSelected: Boolean) {
    view.isSelected = isSelected
}


@BindingAdapter("app:loadCircleImage")
fun loadCircleImage(view: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        view.setImageResource(android.R.color.transparent)
    } else {
        val circularProgressDrawable = CircularProgressDrawable(view.context).apply {
            strokeWidth = 5f
            centerRadius = 25f
        }
        circularProgressDrawable.start()
        Glide.with(view).load(imageUrl).circleCrop().placeholder(circularProgressDrawable)
            .into(view)
    }
}

@BindingAdapter("app:load")
fun load(view: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        view.setImageResource(android.R.color.transparent)
    } else {
        val circularProgressDrawable = CircularProgressDrawable(view.context).apply {
            strokeWidth = 5f
            centerRadius = 25f
        }
        circularProgressDrawable.start()
        Glide.with(view).load(imageUrl).placeholder(circularProgressDrawable)
            .into(view)
    }
}


@BindingAdapter("app:setEnabled")
fun setEnabled(view: AppButton, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        view.background =
            ContextCompat.getDrawable(view.context, R.drawable.button_gradient_unselected)
        view.isEnabled = false
    } else {
        view.isEnabled = true
        view.background =
            ContextCompat.getDrawable(view.context, R.drawable.button_gradient)
        view.requestLayout()
    }
}

@BindingAdapter("app:setSeek")
fun setSeek(view: SeekBar, progress: String?) {
    if (progress.isNullOrEmpty())
        view.setProgress((progress ?: "150").toInt(), true)
}

@BindingAdapter("app:setCountryFlag")
fun setCountryFlag(view: ImageView, country: String?) {
    if (country != null)
        AppEditTextCountry.getCountry(country)?.let {
            Glide.with(view).load(it.flagID).transform(RoundedCorners(8)).into(view)
        }
}
@BindingAdapter("app:setCallingImg")
fun setCallingImg(view: AppThreeCircleAnimation, imgs: String?) {
    if (imgs != null)
        view.loadImg(imgs)
}










