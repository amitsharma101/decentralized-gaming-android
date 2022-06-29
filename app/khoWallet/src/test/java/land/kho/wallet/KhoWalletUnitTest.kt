package land.kho.wallet

import org.junit.Assert.assertEquals
import org.junit.Test
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class KhoWalletUnitTest {

    @Test
    fun khoWallet() {
        val wallet = KhoWallet.createWallet()

        val balance = KhoWallet.getBalance(
            accountAddress = wallet.accountAddress,
            mnemonic = wallet.mnemonic
        )
        assertEquals(balance.toString(), "0")

        val transfer = KhoWallet.transfer(
            amount = "1",
            toAddress = "0xc8F41DE83328e05229041ACD24FB5FC015eb7296",
            mnemonic = wallet.mnemonic
        )
        assertEquals(
            transfer,
            "Error processing transaction request: insufficient funds for gas * price + value"
        )
    }

    @Test
    fun checkAddress() {
        assertEquals(
            KhoWallet.checkAddress(accountAddress = "0xc8F41DE83328e05229041ACD24FB5FC015eb7296"),
            true
        )
        assertEquals(
            KhoWallet.checkAddress(accountAddress = "8880xc8F41DE83328e05229041ACD24FB5FC015eb7296"),
            false
        )
        assertEquals(
            KhoWallet.checkAddress(accountAddress = "ABCxc8F41DE83328e05229041ACD24FB5FC015eb7296"),
            false
        )
        assertEquals(
            KhoWallet.checkAddress(accountAddress = ""),
            false
        )
        assertEquals(
            KhoWallet.checkAddress(accountAddress = "0xc8F41C015eb7296"),
            false
        )
    }

    @Test
    fun checkMnemonic() {
        assertEquals(
            KhoWallet.validateMnemonic(mnemonic = "0xc8F41DE83328e05229041ACD24FB5FC015eb7296"),
            false
        )

        assertEquals(
            KhoWallet.validateMnemonic(mnemonic = "lion surround crew idea arrive auto crush section vanish noble wonder arm"),
            true
        )

        assertEquals(
            KhoWallet.validateMnemonic(mnemonic = "when peasant ejjs  doctor amused police obtain trap divide weasel can"),
            false
        )

        assertEquals(
            KhoWallet.validateMnemonic(mnemonic = "when peasant toss domain doctor amused police obtain trap divide weasel can"),
            true
        )

        assertEquals(
            KhoWallet.validateMnemonic(mnemonic = "when peasant toss domain doctor amused police obtain trap divide weasel can"),
            true
        )

    }

    @Test
    fun checkConvert() {
        assertEquals(
            Convert.fromWei(
              "69000000000000000000", Convert.Unit.ETHER
            ).toBigInteger(),
            BigInteger("69")
        )
        assertEquals(
            Convert.toWei(
                "70", Convert.Unit.ETHER
            ).toBigInteger(),
            BigInteger("70000000000000000000")
        )
    }
}