package land.kho.meta.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import land.kho.meta.R
import land.kho.meta.utils.Utility.hideKeyboard
import land.kho.meta.utils.Utility.showKeyboard

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(isVisible: Boolean?) {
    if (isVisible != null) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("visible")
fun View.visible(isVisible: Boolean?) {
    if (isVisible != null) {
        visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}

@BindingAdapter("isKeyboardVisible")
fun View.isKeyboardVisible(isVisible: Boolean?) {
    if (isVisible != null) {
        if (isVisible) showKeyboard() else hideKeyboard()
    }
}

@BindingAdapter("setBitmap")
fun ShapeableImageView.setBitmap(bitmap: Bitmap?) {
    bitmap?.let {
        setImageBitmap(bitmap)
    }
}

@BindingAdapter("setTrxImage")
fun ShapeableImageView.setTrxImage(type: String?) {
    type?.let {
        setImageResource(if (type == Constants.SEND_TO) R.drawable.ic_send else R.drawable.ic_receive)

    }
}

@BindingAdapter("setAddress")
fun MaterialTextView.setAddress(address: String?) {
    address?.let {
        text = Utility.toShortAddress(address = address)
    }
}

@BindingAdapter("setVerifyImage")
fun ShapeableImageView.setVerifyImage(isVerified: Boolean?) {
    isVerified?.let {
        background =
            if (isVerified) Utility.getDrawable(R.drawable.ic_verified) else Utility.getDrawable(R.drawable.ic_unverified)
    }
}

@BindingAdapter("setVerifyImageLight")
fun ShapeableImageView.setVerifyImageLight(isVerified: Boolean?) {
    isVerified?.let {
        background =
            if (isVerified) Utility.getDrawable(R.drawable.ic_verified_white) else Utility.getDrawable(
                R.drawable.ic_unverified
            )
    }
}

@BindingAdapter("setEmailText")
fun MaterialTextView.setEmailText(email: String?) {
    email?.let {
        text = resources.getString(
            R.string.verification_mail_has_been_already_send_to_x_check_your_inbox_to_verify,
            email
        )
    }
}


@BindingAdapter("setUnverifiedTokenText")
fun MaterialTextView.setUnverifiedTokenText(tokens: String?) {
    tokens?.let {
        text = resources.getString(R.string.verify_you_account_to_claim_x_tokens, tokens)
    }
}

@BindingAdapter("setRenewButton")
fun MaterialButton.setRenewButton(left: Int?) {
    left?.let {
        visibility = if (left == 0) View.VISIBLE else View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setPassName")
fun MaterialTextView.setPassName(name: Int?) {
    name?.let {
        text = "KHO PASS : #ENTRY$name"
    }
}

@BindingAdapter("setUserName")
fun MaterialTextView.setUserName(name: String?) {
    name?.let {
        text = Utility.toUserName(name = name)
    }
}

@BindingAdapter("setReferredBY")
fun MaterialTextView.setReferredBY(referral: String?) {
    referral?.let {
        text = resources.getString(R.string.congratulation_you_were_referred_by, referral)
    }
}










