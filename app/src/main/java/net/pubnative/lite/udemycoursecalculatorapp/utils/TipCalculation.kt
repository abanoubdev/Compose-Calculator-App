package net.pubnative.lite.udemycoursecalculatorapp.utils

fun calculateTipAmount(totalBill: String, sliderValue: Float): Double {
    return if (totalBill.isNotEmpty()) {
        ((totalBill.toInt() * sliderValue.toInt()) / 100).toDouble()
    } else {
        0.0
    }
}

fun calculateTotalPerPerson(
    totalBill: String,
    splitBy: Int,
    sliderValue: Float
): Double {
    return if (totalBill.isNotEmpty()) {
        (calculateTipAmount(totalBill, sliderValue) + totalBill.toInt()) / splitBy
    } else {
        0.0
    }
}