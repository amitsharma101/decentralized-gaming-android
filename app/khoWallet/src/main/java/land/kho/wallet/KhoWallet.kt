package land.kho.wallet

import android.os.StrictMode
import android.util.Log
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.*
import org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.security.Security
import java.util.*


object KhoWallet {

    private const val serverUrl = "https://polygon-mainnet.infura.io/v3/9734a98d1b124b7cbdf4fd3e3fb5208b"
    private var web3j: Web3j? = null
    private var khoToken: KhoToken? = null

    // Address of test KhoToken on Main net - 0x79e600a610c67E0920fa45aF29c6212E6538FE44
    private const val contractAddressTest = "0x79e600a610c67E0920fa45aF29c6212E6538FE44"
    //    Address of main KhoToken on Main net - 0x8f5a1E64c4EA6FcfEC6f2Cc5b5E6dF124c6d5Ea7
    private const val contractAddress = "0x8f5a1E64c4EA6FcfEC6f2Cc5b5E6dF124c6d5Ea7"
    const val nftContractAddress = "0xdF066C6DB025468da6ce206E475c1b69bc490f52"

    /**
     * return suitable contractAddress
     */

    fun getContractAddress(): String {
        return if (BuildConfig.DEBUG) contractAddressTest else contractAddress
    }

    /**
     * create kho wallet
     */

    fun createWallet(): KhoWalletAccount {
        return restoreWallet(mnemonic = generateMnemonic())
    }

    /**
     * restore/import  wallet
     */

    fun restoreWallet(mnemonic: String): KhoWalletAccount {
        initKho(mnemonic = mnemonic)
        val credentials = createCredential(mnemonic = mnemonic)
        val accountAddress = credentials.address
        return KhoWalletAccount(
            accountAddress = accountAddress,
            mnemonic = mnemonic,
            privateKeyParse = credentials.ecKeyPair.privateKey.toString(16),
            publicKeyParse = credentials.ecKeyPair.publicKey.toString(16)
        )
    }

    /**
     * Retrieving wallet balance
     */

    fun getBalance(
        accountAddress: String,
        mnemonic: String
    ): BigInteger {
        initKho(mnemonic = mnemonic)
        return try {
            fromWei(
                amount = khoToken!!.balanceOf(account = accountAddress).send().toString()
            )
        } catch (e: Exception) {
            BigInteger("0")
        }
    }

    fun fromWei(amount: String): BigInteger {
        return Convert.fromWei(
            amount,
            Convert.Unit.ETHER
        ).toBigInteger()
    }

    fun toWei(amount: String): BigInteger {
        return Convert.toWei(
            amount,
            Convert.Unit.ETHER
        ).toBigInteger()
    }

    /**
     * Sending Funds
     */

    fun transfer(
        amount: String,
        toAddress: String,
        mnemonic: String
    ): String {
        initKho(mnemonic = mnemonic)
        return try {
            khoToken!!.transfer(
                recipient = toAddress,
                amount = toWei(amount = amount),
            ).send().transactionHash
        } catch (e: Exception) {
            "${e.message}"
        }
    }

    /**
     * checks if account Address is valid
     */

    fun checkAddress(
        accountAddress: String
    ): Boolean {
        return WalletUtils.isValidAddress(accountAddress)
    }

    /**
     * checks if mnemonicis valid
     */

    fun validateMnemonic(
        mnemonic: String
    ): Boolean {
        return MnemonicUtils.validateMnemonic(mnemonic)
    }

    private fun createCredential(mnemonic: String): Credentials {
        val masterKeypair =
            Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, null))

        val path = intArrayOf(44 or HARDENED_BIT, 60 or HARDENED_BIT, 0 or HARDENED_BIT, 0, 0)
        val keyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path)

        return Credentials.create(keyPair)
    }

    private fun generateMnemonic(): String {
        val initialEntropy = ByteArray(16)
        Random().nextBytes(initialEntropy)
        return MnemonicUtils.generateMnemonic(initialEntropy)
    }

    /**
     * call every time to avoid async issues
     */

    private fun initKho(mnemonic: String) {
        //comment out StrictMode during running test case
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        setupBouncyCastle()
        web3j = Web3j.build(HttpService(serverUrl))
        khoToken = KhoToken.load(
            contractAddress = getContractAddress(),
            web3j = web3j,
            credentials = createCredential(mnemonic = mnemonic),
            contractGasProvider = DefaultGasProvider()
        )
    }

    /**
     * et up the security provider
     */

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up a provider  when it's used for the first time.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            return
        }
        //There is a possibility  the bouncy castle registered by android may not have all ciphers
        //so we  substitute with the one bundled in the app.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

    fun signMessage(message: String, mnemonic: String): String{
        val signature = Sign.signPrefixedMessage(message.toByteArray(), createCredential(mnemonic=mnemonic).ecKeyPair)
        val r = Numeric.toHexString(signature.r)
        val s = Numeric.toHexString(signature.s).substring(2)
        val v = Numeric.toHexString(signature.v).substring(2)
        return r+s+v
    }
}
