package land.kho.meta.domain.repository

import land.kho.meta.data.api.ApiList
import land.kho.meta.data.api.ApiRequest
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.GetAccessTokenRequest
import land.kho.meta.data.model.request.UpdateEmailRequest
import land.kho.meta.data.model.request.UpdateFcmTokenRequest
import land.kho.meta.data.model.request.UpdateReferralCodeRequest
import land.kho.meta.data.model.response.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private var apiList: ApiList
) : Repository, ApiRequest() {

    override suspend fun getTokenTransactions(
        contractAddress: String,
        address: String
    ): NetworkResponse<TokenResponse> {
        return apiRequest {
            apiList.getTokenTransactions(
                url = "https://api.polygonscan.com/api?module=account&action=tokentx",
                contractAddress = contractAddress,
                address = address
            )
        }
    }

    override suspend fun getNftTransactions(
        contractAddress: String,
        address: String
    ): NetworkResponse<TokenResponse> {
        return apiRequest {
            apiList.getTokenTransactions(
                url = "https://api.polygonscan.com/api?module=account&action=tokennfttx",
                contractAddress = contractAddress,
                address = address
            )
        }
    }

    override suspend fun getNonce(
        publicAddress: String
    ): NetworkResponse<GetNonceResponse> {
        return apiRequest {
            apiList.getNonce(
                publicAddress = publicAddress
            )
        }
    }

    override suspend fun getAccessToken(
        request: GetAccessTokenRequest
    ): NetworkResponse<GetAccessTokenResponse> {
        return apiRequest {
            apiList.getAccessToken(
                request = request
            )
        }
    }

    override suspend fun getMe(
    ): NetworkResponse<GetProfileDataResponse> {
        return apiRequest { apiList.getMe() }
    }

    override suspend fun sendEmail(
        email: String,
        userId: Int
    ): NetworkResponse<SendMailResponse> {
        return apiRequest {
            apiList.sendEmail(
                userId = userId,
                email = email
            )
        }
    }

    override suspend fun getUnverifiedTokens(): NetworkResponse<GetUnverifiedTokensResponse> {
        return apiRequest { apiList.getUnverifiedTokens() }
    }

    override suspend fun getPasses(): NetworkResponse<GetPassesResponse> {
        return apiRequest { apiList.getPasses() }
    }

    override suspend fun updateUserName(request: UpdateEmailRequest): NetworkResponse<GetProfileDataResponse> {
        return apiRequest {
            apiList.updateUserName(
                request = request
            )
        }
    }

    override suspend fun updateFcmToken(request: UpdateFcmTokenRequest): NetworkResponse<GetProfileDataResponse> {
        return apiRequest { apiList.updateFcmToken(request) }
    }

    override suspend fun getEarnings(): NetworkResponse<EarningsResponse> {
        return apiRequest { apiList.getEarnings() }
    }

    override suspend fun getWalletBalance(): NetworkResponse<GetWalletResponse> {
        return apiRequest { apiList.getWalletBalance() }
    }

    override suspend fun transferToWallet(): NetworkResponse<TransferToWalletResponse> {
        return apiRequest { apiList.transferToWallet() }
    }

    override suspend fun updateReferralCode(request: UpdateReferralCodeRequest): NetworkResponse<SendReferralResponse> {
        return apiRequest {
            apiList.updateReferralCode(
                request = request
            )
        }
    }


}