package land.kho.meta.data.api

import land.kho.meta.data.model.request.GetAccessTokenRequest
import land.kho.meta.data.model.request.UpdateEmailRequest
import land.kho.meta.data.model.request.UpdateFcmTokenRequest
import land.kho.meta.data.model.request.UpdateReferralCodeRequest
import land.kho.meta.data.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiList {

    @GET
    suspend fun getTokenTransactions(
        @Url url: String,
        @Query("contractaddress") contractAddress: String,
        @Query("address") address: String
    ): Response<TokenResponse>

    @GET("/v1/auth/nonce/{publicAddress}")
    suspend fun getNonce(@Path("publicAddress") publicAddress: String): Response<GetNonceResponse>

    @POST("/v1/auth/accesstoken")
    suspend fun getAccessToken(@Body request: GetAccessTokenRequest): Response<GetAccessTokenResponse>

    @GET("/v1/users/me")
    suspend fun getMe(): Response<GetProfileDataResponse>

    @POST("/v1/auth//send-confirmation-mail/{userId}")
    suspend fun sendEmail(
        @Path("userId") userId: Int,
        @Query("email") email: String
    ): Response<SendMailResponse>

    @GET("/v1/txn/player/pending-tokens")
    suspend fun getUnverifiedTokens(): Response<GetUnverifiedTokensResponse>

    @GET("/v1/pass/battery")
    suspend fun getPasses(): Response<GetPassesResponse>

    @PATCH("/v1/users/me")
    suspend fun updateUserName(
        @Body request: UpdateEmailRequest
    ): Response<GetProfileDataResponse>

    @PATCH("/v1/users/me")
    suspend fun updateFcmToken(
        @Body request: UpdateFcmTokenRequest
    ): Response<GetProfileDataResponse>

    @GET("/v1/ledger/list")
    suspend fun getEarnings(): Response<EarningsResponse>

    @GET("/v1/wallet")
    suspend fun getWalletBalance(): Response<GetWalletResponse>

    @POST("/v1/wallet/withdraw")
    suspend fun transferToWallet(): Response<TransferToWalletResponse>

    @POST("/v1/users/referral")
    suspend fun updateReferralCode(
        @Body request: UpdateReferralCodeRequest
    ): Response<SendReferralResponse>

}