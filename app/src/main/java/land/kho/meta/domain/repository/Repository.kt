package land.kho.meta.domain.repository

import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.GetAccessTokenRequest
import land.kho.meta.data.model.request.UpdateEmailRequest
import land.kho.meta.data.model.request.UpdateFcmTokenRequest
import land.kho.meta.data.model.request.UpdateReferralCodeRequest
import land.kho.meta.data.model.response.*

interface Repository {
    suspend fun getTokenTransactions(
        contractAddress: String,
        address: String
    ): NetworkResponse<TokenResponse>

    suspend fun getNftTransactions(
        contractAddress: String,
        address: String
    ): NetworkResponse<TokenResponse>

    suspend fun getNonce(
        publicAddress: String
    ): NetworkResponse<GetNonceResponse>

    suspend fun getAccessToken(
        request: GetAccessTokenRequest
    ): NetworkResponse<GetAccessTokenResponse>

    suspend fun getMe(): NetworkResponse<GetProfileDataResponse>

    suspend fun sendEmail(
        email: String,
        userId: Int
    ): NetworkResponse<SendMailResponse>

    suspend fun getUnverifiedTokens(): NetworkResponse<GetUnverifiedTokensResponse>

    suspend fun getPasses(): NetworkResponse<GetPassesResponse>

    suspend fun updateUserName(
        request: UpdateEmailRequest
    ): NetworkResponse<GetProfileDataResponse>

    suspend fun updateFcmToken(
        request: UpdateFcmTokenRequest
    ): NetworkResponse<GetProfileDataResponse>

    suspend fun getEarnings(): NetworkResponse<EarningsResponse>

    suspend fun getWalletBalance(): NetworkResponse<GetWalletResponse>

    suspend fun transferToWallet(): NetworkResponse<TransferToWalletResponse>

    suspend fun updateReferralCode(
        request: UpdateReferralCodeRequest
    ): NetworkResponse<SendReferralResponse>
}