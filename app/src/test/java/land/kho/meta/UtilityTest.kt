package land.kho.meta

import land.kho.meta.utils.Utility
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class UtilityTest {

    @Test
    fun toAmount() {
        Assert.assertEquals(
            Utility.toAmount(amount = "22000000000000000000", symbol = "KHO"),
            "22 KHO"
        )
    }

    @Test
    fun toAmountBigInteger() {
        Assert.assertEquals(
            Utility.toAmount(amount = BigInteger("22")),
            "22 KHO"
        )
    }

    @Test
    fun toShortAddress() {
        Assert.assertEquals(
            Utility.toShortAddress(address = "0xc8F41DE83328e05229041ACD24FB5FC015eb7296"),
            "0xc8F4...7296"
        )
    }

    @Test
    fun toUserName() {
        Assert.assertEquals(
            Utility.toUserName(name = "aryanlolislol"),
            "aryanlol..."
        )
    }
    @Test
    fun testRenderToString() {
        Assert.assertEquals(
            Utility.renderToString(string = "op"),
            "op"
        )
    }

    @Test
    fun testRenderToStringNull() {
        Assert.assertEquals(
            Utility.renderToString(string = null),
            ""
        )
    }

    @Test
    fun testRenderToDate() {
        Assert.assertEquals(
            Utility.renderToDate(date = "1644796762"),
            "14 Feb 2022"
        )
    }

    @Test
    fun testRenderToDateNull() {
        Assert.assertEquals(
            Utility.renderToDate(date = null),
            ""
        )
    }

    @Test
    fun testRenderToDatePicker() {
        Assert.assertEquals(
            Utility.renderToDatePicker(date = 1649629455740L),
            "11 Apr 2022"
        )
    }

    @Test
    fun testRenderToDatePickerNull() {
        Assert.assertEquals(
            Utility.renderToDatePicker(date = null),
            ""
        )
    }

    @Test
    fun convertStringToLong() {
        Assert.assertEquals(
            Utility.convertStringToLong(date = "2022-04-10T22:53:36.420Z"),
            1649611416420L
        )
    }

    @Test
    fun convertStringToLongNull() {
        Assert.assertEquals(
            Utility.convertStringToLong(date = null),
            0L
        )
    }

}