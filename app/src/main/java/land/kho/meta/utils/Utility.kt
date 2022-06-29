package land.kho.meta.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import land.kho.meta.BuildConfig
import land.kho.meta.data.model.response.EarningItem
import land.kho.meta.presentation.application.AppApplication.Companion.appContext
import land.kho.meta.presentation.home.earning.EarningType
import land.kho.wallet.KhoWallet
import org.w3c.dom.Node
import java.math.BigInteger
import java.text.SimpleDateFormat
import javax.xml.parsers.DocumentBuilderFactory

object Utility {

    /** @return Resources */
    fun getResources(): Resources = appContext.resources

    /** @return String Value from StringResource */
    fun getString(@StringRes string: Int): String = getResources().getString(string)

    /** @return Drawable Value from Drawable Resource */
    fun getDrawable(@DrawableRes drawableId: Int) =
        ContextCompat.getDrawable(appContext, drawableId)!!

    /** @return Color Value from Color Resource */
    fun getColor(@ColorRes colorId: Int): Int = ContextCompat.getColor(appContext, colorId)

    /**
     * show toast message
     */

    fun showToast(msg: String) {
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * print log message
     */

    fun log(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    /**
     * converts string(nullable) to string(not null) to UI
     */

    fun renderToString(string: String?): String {
        return string ?: ""
    }

    /**
     * converts date(seconds String) to dd MMM yyyy  to UI
     */

    fun renderToDate(date: String?): String {
        return if (date != null) SimpleDateFormat(
            "dd MMM yyyy",
            Constants.locale
        ).format((date + "000").toLong()) else ""
    }

    /**
     * converts date(long) ms to dd MMM yyyy  to UI
     */

    fun renderToDatePicker(date: Long?): String {
        return if (date != null) SimpleDateFormat(
            "dd MMM yyyy",
            Constants.locale
        ).format(date) else ""
    }

    /**
     * hide keyboard to UI
     */

    fun View.hideKeyboard() {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * show keyboard to UI
     */

    fun View.showKeyboard() {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, SHOW_IMPLICIT)
    }


    /**
     * convert amount to kho balance
     */

    fun toAmount(amount: String, symbol: String): String {
        return "${KhoWallet.fromWei(amount = amount)} $symbol"
    }


    /**
     * convert actual amount  to kho balance
     */

    fun toAmount(amount: BigInteger): String {
        return "$amount KHO"
    }

    /**
     * convert address to ShortAddress (first...last)
     */

    fun toShortAddress(address: String): String {
        return "${address.substring(0..5)}...${address.substring(38..41)}"
    }


    /**
     * convert Username  to Shortname (first8...)
     */

    fun toUserName(name: String): String {
        return if (name.length > 7) {
            "${name.substring(0..7)}..."
        } else
            name
    }

    /**
     * copy to clip board
     */

    fun copyText(text: String) {
        val clipboard = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clipData)
        showToast(msg = "Copied to clipboard")
    }


    /**
     * get version code
     */

    fun getVersionCode(): String {
        return BuildConfig.VERSION_NAME
    }


    /**
     * convert earning data to text +120 $kho from Ludo on 12 March, 2020
     */

    fun toEarningText(earning: EarningItem): String {
        val date = convertStringToDate(earning.updatedAt)
        return when (earning.type) {
            EarningType.GAME_PAYMENT.value -> {
                "-${earning.amount} Pass Consumed from ${earning.metadata} on $date"
            }
            EarningType.GAME_REWARD.value -> {
                "+${earning.amount} KHO from ${earning.metadata} on $date"
            }
            EarningType.TRANSFER_TO_WALLET.value -> {
                "${earning.amount} KHO transferred to wallet on $date"
            }
            EarningType.REFERRAL_REWARD.value -> {
                "+${earning.amount} Pass from Referral Reward on $date"
            }
            else -> {
                "${earning.amount} Invalid on $date"
            }
        }
    }

    /**
     * convert date 2022-04-10T18:40:40.553Z to dd/MM/yy
     */

    fun convertStringToLong(date: String?): Long {
        return if (date != null) {
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                Constants.locale
            ).parse(date)?.time ?: 0L
        } else {
            0L
        }
    }

    /**
     * convert date 2022-04-10T18:40:40.553Z to dd/MM/yy
     */

    fun convertStringToDate(date: String?): String {
        return if (date != null) {
            renderToDatePicker(date = convertStringToLong(date = date))
        } else ""
    }

    fun getCountryCodeList(): ArrayList<String> {
        val countryList: ArrayList<String> = ArrayList()
        val nList = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(appContext.assets.open("country_code_list.xml")).getElementsByTagName("item")

        for (i in 0 until nList.length) {
            if (nList.item(0).nodeType === Node.ELEMENT_NODE) {
                val elm: org.w3c.dom.Element = nList.item(i) as org.w3c.dom.Element
                val user = (elm.getElementsByTagName("name")
                    .item(0).childNodes.item(0).nodeValue.toString() + " " + elm.getElementsByTagName(
                    "code"
                ).item(0).childNodes.item(0).nodeValue.toString())
                countryList.add(user)
            }
        }
        return countryList

    }

}
