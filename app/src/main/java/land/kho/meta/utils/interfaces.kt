package land.kho.meta.utils

interface ItemClickListener<in T> {

    fun onItemClick(value: T)
}

interface AdapterViewModel {
    val viewType: Int
}
