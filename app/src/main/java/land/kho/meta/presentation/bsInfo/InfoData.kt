package land.kho.meta.presentation.bsInfo

data class InfoData(
    var text: String,
    var isButtonVisible: Boolean = false,
    var buttonText: String = "",
    var buttonLink: String = "",
    var isButtonSecondVisible: Boolean = false,
    var buttonSecondText: String = "",
    var buttonSecondLink: String = ""
)
