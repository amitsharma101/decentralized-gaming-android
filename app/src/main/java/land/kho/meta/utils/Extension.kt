package land.kho.meta.utils

import androidx.databinding.Observable

fun Observable.addPropertyChangedCallback(doThis: () -> Unit) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            doThis.invoke()
        }
    })

}