package land.kho.meta.presentation.home.game

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.Window
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import land.kho.meta.R
import land.kho.meta.databinding.LayoutLudoRulesDialogBinding

class LudoRulesDialog(
    context: Context,
    positiveBtnListener: DialogInterface.OnClickListener,
    closeBtnListener: DialogInterface.OnClickListener? = null
) : Dialog(context, R.style.GenericDialog) {
    private val bind: LayoutLudoRulesDialogBinding =
        LayoutLudoRulesDialogBinding.inflate(LayoutInflater.from(getContext()))

    private var positiveBtnListener: DialogInterface.OnClickListener? = null
    private var closeBtnListener: DialogInterface.OnClickListener? = null

    init {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE)

        super.setCancelable(false)
        super.setContentView(bind.root)

        this.positiveBtnListener = positiveBtnListener
        this.closeBtnListener = closeBtnListener

        bind.btnPositive.setOnClickListener {
            positiveBtnListener.let {
                it.onClick(this, BUTTON_POSITIVE)
            }
            super.dismiss()
        }
        bind.ivClose.setOnClickListener {
            closeBtnListener.let {
                it?.onClick(this, BUTTON_NEGATIVE)
            }
            super.dismiss()
        }

        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        bind.cardBack.setBackgroundColor(
            ContextCompat.getColor(
                getContext(),
                android.R.color.transparent
            )
        )
    }


}