package land.kho.meta.presentation.wallet.bsReceive

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import javax.inject.Inject

@HiltViewModel
class BsReceiveVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM() {

    var qrCode: Bitmap? = null

    fun getQrCode(args: String) {
        val bitMatrix = QRCodeWriter().encode(args, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        qrCode = bitmap
    }

}
