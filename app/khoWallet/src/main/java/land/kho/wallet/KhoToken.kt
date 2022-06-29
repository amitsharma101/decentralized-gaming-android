package land.kho.wallet

import io.reactivex.Flowable
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.BaseEventResponse
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger

/**
 *
 * Auto generated code.
 *
 * **Do not modify!**
 *
 * Please use the [web3j command line tools](https://docs.web3j.io/command_line.html),
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * [codegen module](https://github.com/web3j/web3j/tree/master/codegen) to update.
 *
 *
 * Generated with web3j version 1.4.1.
 */ //test
class KhoToken : Contract {

    private constructor(
        contractAddress: String?,
        web3j: Web3j?,
        credentials: Credentials?,
        contractGasProvider: ContractGasProvider?
    ) : super(
        BINARY, contractAddress, web3j, credentials, contractGasProvider
    )

    private constructor(
        contractAddress: String?,
        web3j: Web3j?,
        transactionManager: TransactionManager?,
        contractGasProvider: ContractGasProvider?
    ) : super(
        BINARY, contractAddress, web3j, transactionManager, contractGasProvider
    )

    fun getApprovalEvents(transactionReceipt: TransactionReceipt?): List<ApprovalEventResponse> {
        val valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt)
        val responses = ArrayList<ApprovalEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = ApprovalEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.owner = eventValues.indexedValues[0].value as String
            typedResponse.spender = eventValues.indexedValues[1].value as String
            typedResponse.value = eventValues.nonIndexedValues[0].value as BigInteger
            responses.add(typedResponse)
        }
        return responses
    }

    fun approvalEventFlowable(filter: EthFilter?): Flowable<ApprovalEventResponse> {
        return web3j.ethLogFlowable(filter).map { log ->
            val eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log)
            val typedResponse = ApprovalEventResponse()
            typedResponse.log = log
            typedResponse.owner = eventValues.indexedValues[0].value as String
            typedResponse.spender = eventValues.indexedValues[1].value as String
            typedResponse.value = eventValues.nonIndexedValues[0].value as BigInteger
            typedResponse
        }
    }

    fun approvalEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<ApprovalEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT))
        return approvalEventFlowable(filter)
    }

    fun getOwnershipTransferredEvents(transactionReceipt: TransactionReceipt?): List<OwnershipTransferredEventResponse> {
        val valueList =
            extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt)
        val responses = ArrayList<OwnershipTransferredEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = OwnershipTransferredEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.previousOwner = eventValues.indexedValues[0].value as String
            typedResponse.newOwner = eventValues.indexedValues[1].value as String
            responses.add(typedResponse)
        }
        return responses
    }

    fun ownershipTransferredEventFlowable(filter: EthFilter?): Flowable<OwnershipTransferredEventResponse> {
        return web3j.ethLogFlowable(filter)
            .map { log ->
                val eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log)
                val typedResponse = OwnershipTransferredEventResponse()
                typedResponse.log = log
                typedResponse.previousOwner = eventValues.indexedValues[0].value as String
                typedResponse.newOwner = eventValues.indexedValues[1].value as String
                typedResponse
            }
    }

    fun ownershipTransferredEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<OwnershipTransferredEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT))
        return ownershipTransferredEventFlowable(filter)
    }

    fun getTransferEvents(transactionReceipt: TransactionReceipt?): List<TransferEventResponse> {
        val valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt)
        val responses = ArrayList<TransferEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = TransferEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.from = eventValues.indexedValues[0].value as String
            typedResponse.to = eventValues.indexedValues[1].value as String
            typedResponse.value = eventValues.nonIndexedValues[0].value as BigInteger
            responses.add(typedResponse)
        }
        return responses
    }

    fun transferEventFlowable(filter: EthFilter?): Flowable<TransferEventResponse> {
        return web3j.ethLogFlowable(filter).map { log ->
            val eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log)
            val typedResponse = TransferEventResponse()
            typedResponse.log = log
            typedResponse.from = eventValues.indexedValues[0].value as String
            typedResponse.to = eventValues.indexedValues[1].value as String
            typedResponse.value = eventValues.nonIndexedValues[0].value as BigInteger
            typedResponse
        }
    }

    fun transferEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<TransferEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT))
        return transferEventFlowable(filter)
    }

    fun allowance(owner: String?, spender: String?): RemoteFunctionCall<BigInteger> {
        val function = Function(
            FUNC_ALLOWANCE,
            listOf<Type<*>>(
                Address(160, owner),
                Address(160, spender)
            ),
            listOf<TypeReference<*>>(object : TypeReference<Uint256?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun approve(spender: String?, amount: BigInteger?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_APPROVE,
            listOf<Type<*>>(
                Address(160, spender),
                Uint256(amount)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun balanceOf(account: String?): RemoteFunctionCall<BigInteger> {
        val function = Function(
            FUNC_BALANCEOF,
            listOf<Type<*>>(Address(160, account)),
            listOf<TypeReference<*>>(object : TypeReference<Uint256?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun decimals(): RemoteFunctionCall<BigInteger> {
        val function = Function(
            FUNC_DECIMALS,
            listOf(),
            listOf<TypeReference<*>>(object : TypeReference<Uint8?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun decreaseAllowance(
        spender: String?,
        subtractedValue: BigInteger?
    ): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_DECREASEALLOWANCE,
            listOf<Type<*>>(
                Address(160, spender),
                Uint256(subtractedValue)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun increaseAllowance(
        spender: String?,
        addedValue: BigInteger?
    ): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_INCREASEALLOWANCE,
            listOf<Type<*>>(
                Address(160, spender),
                Uint256(addedValue)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun name(): RemoteFunctionCall<String> {
        val function = Function(
            FUNC_NAME,
            listOf(),
            listOf<TypeReference<*>>(object : TypeReference<Utf8String?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun owner(): RemoteFunctionCall<String> {
        val function = Function(
            FUNC_OWNER,
            listOf(),
            listOf<TypeReference<*>>(object : TypeReference<Address?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun renounceOwnership(): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_RENOUNCEOWNERSHIP,
            listOf(), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun symbol(): RemoteFunctionCall<String> {
        val function = Function(
            FUNC_SYMBOL,
            listOf(),
            listOf<TypeReference<*>>(object : TypeReference<Utf8String?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun totalSupply(): RemoteFunctionCall<BigInteger> {
        val function = Function(
            FUNC_TOTALSUPPLY,
            listOf(),
            listOf<TypeReference<*>>(object : TypeReference<Uint256?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun transfer(recipient: String?, amount: BigInteger?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_TRANSFER,
            listOf<Type<*>>(
                Address(160, recipient),
                Uint256(amount)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun transferFrom(
        sender: String?,
        recipient: String?,
        amount: BigInteger?
    ): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_TRANSFERFROM,
            listOf<Type<*>>(
                Address(160, sender),
                Address(160, recipient),
                Uint256(amount)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun transferOwnership(newOwner: String?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_TRANSFEROWNERSHIP,
            listOf<Type<*>>(Address(160, newOwner)), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun initialize(trustedForwarder: String?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_INITIALIZE,
            listOf<Type<*>>(Address(160, trustedForwarder)), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun isTrustedForwarder(forwarder: String?): RemoteFunctionCall<Boolean> {
        val function = Function(
            FUNC_ISTRUSTEDFORWARDER,
            listOf<Type<*>>(Address(160, forwarder)),
            listOf<TypeReference<*>>(object : TypeReference<Bool?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, Boolean::class.java)
    }

    fun setTrustedForwarder(trustedForwarder: String?): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_SETTRUSTEDFORWARDER,
            listOf<Type<*>>(Address(160, trustedForwarder)), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    class ApprovalEventResponse : BaseEventResponse() {
        var owner: String? = null
        var spender: String? = null
        var value: BigInteger? = null
    }

    class OwnershipTransferredEventResponse : BaseEventResponse() {
        var previousOwner: String? = null
        var newOwner: String? = null
    }

    class TransferEventResponse : BaseEventResponse() {
        var from: String? = null
        var to: String? = null
        var value: BigInteger? = null
    }

    companion object {
        const val BINARY =
            "608060405260405162000f4038038062000f408339810160408190526200002691620004d4565b82816200005560017f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbd62000603565b60008051602062000ef9833981519152146200008157634e487b7160e01b600052600160045260246000fd5b6200008f82826000620000ff565b50620000bf905060017fb53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d610462000603565b60008051602062000ed983398151915214620000eb57634e487b7160e01b600052600160045260246000fd5b620000f68262000170565b5050506200066c565b6200010a83620001cb565b6040516001600160a01b038416907fbc7cd75a20ee27fd9adebab32041f755214dbc6bffa90cc0225b39da2e5c2d3b90600090a26000825111806200014c5750805b156200016b576200016983836200029360201b6200026c1760201c565b505b505050565b7f7e644d79422f17c01e4894b5f4f588d331ebfa28653d42ae832dc59e38c9798f6200019b620002c2565b604080516001600160a01b03928316815291841660208301520160405180910390a1620001c881620002fb565b50565b620001e1816200038b60201b620002981760201c565b620002495760405162461bcd60e51b815260206004820152602d60248201527f455243313936373a206e657720696d706c656d656e746174696f6e206973206e60448201526c1bdd08184818dbdb9d1c9858dd609a1b60648201526084015b60405180910390fd5b806200027260008051602062000ef983398151915260001b6200039560201b620002141760201c565b80546001600160a01b0319166001600160a01b039290921691909117905550565b6060620002bb838360405180606001604052806027815260200162000f196027913962000398565b9392505050565b6000620002ec60008051602062000ed983398151915260001b6200039560201b620002141760201c565b546001600160a01b0316905090565b6001600160a01b038116620003625760405162461bcd60e51b815260206004820152602660248201527f455243313936373a206e65772061646d696e20697320746865207a65726f206160448201526564647265737360d01b606482015260840162000240565b806200027260008051602062000ed983398151915260001b6200039560201b620002141760201c565b803b15155b919050565b90565b6060620003a5846200038b565b620004025760405162461bcd60e51b815260206004820152602660248201527f416464726573733a2064656c65676174652063616c6c20746f206e6f6e2d636f6044820152651b9d1c9858dd60d21b606482015260840162000240565b600080856001600160a01b0316856040516200041f9190620005b0565b600060405180830381855af49150503d80600081146200045c576040519150601f19603f3d011682016040523d82523d6000602084013e62000461565b606091505b509092509050620004748282866200047e565b9695505050505050565b606083156200048f575081620002bb565b825115620004a05782518084602001fd5b8160405162461bcd60e51b8152600401620002409190620005ce565b80516001600160a01b03811681146200039057600080fd5b600080600060608486031215620004e9578283fd5b620004f484620004bc565b92506200050460208501620004bc565b60408501519092506001600160401b038082111562000521578283fd5b818601915086601f83011262000535578283fd5b8151818111156200054a576200054a62000656565b604051601f8201601f19908116603f0116810190838211818310171562000575576200057562000656565b816040528281528960208487010111156200058e578586fd5b620005a183602083016020880162000627565b80955050505050509250925092565b60008251620005c481846020870162000627565b9190910192915050565b6000602082528251806020840152620005ef81604085016020870162000627565b601f01601f19169190910160400192915050565b6000828210156200062257634e487b7160e01b81526011600452602481fd5b500390565b60005b83811015620006445781810151838201526020016200062a565b83811115620001695750506000910152565b634e487b7160e01b600052604160045260246000fd5b61085d806200067c6000396000f3fe60806040526004361061004e5760003560e01c80633659cfe6146100655780634f1ef286146100855780635c60da1b146100985780638f283970146100c9578063f851a440146100e95761005d565b3661005d5761005b6100fe565b005b61005b6100fe565b34801561007157600080fd5b5061005b6100803660046106ed565b610118565b61005b610093366004610707565b610164565b3480156100a457600080fd5b506100ad6101da565b6040516001600160a01b03909116815260200160405180910390f35b3480156100d557600080fd5b5061005b6100e43660046106ed565b610217565b3480156100f557600080fd5b506100ad610241565b6101066102a2565b610116610111610346565b610355565b565b610120610379565b6001600160a01b0316336001600160a01b0316141561015957610154816040518060200160405280600081525060006103ac565b610161565b6101616100fe565b50565b61016c610379565b6001600160a01b0316336001600160a01b031614156101cd576101c88383838080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250600192506103ac915050565b6101d5565b6101d56100fe565b505050565b60006101e4610379565b6001600160a01b0316336001600160a01b0316141561020c57610205610346565b9050610214565b6102146100fe565b90565b61021f610379565b6001600160a01b0316336001600160a01b03161415610159576101548161040b565b600061024b610379565b6001600160a01b0316336001600160a01b0316141561020c57610205610379565b606061029183836040518060600160405280602781526020016108016027913961045f565b9392505050565b803b15155b919050565b6102aa610379565b6001600160a01b0316336001600160a01b031614156103415760405162461bcd60e51b815260206004820152604260248201527f5472616e73706172656e745570677261646561626c6550726f78793a2061646d60448201527f696e2063616e6e6f742066616c6c6261636b20746f2070726f78792074617267606482015261195d60f21b608482015260a4015b60405180910390fd5b610116565b600061035061053a565b905090565b3660008037600080366000845af43d6000803e808015610374573d6000f35b3d6000fd5b60007fb53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d61035b546001600160a01b0316905090565b6103b583610562565b6040516001600160a01b038416907fbc7cd75a20ee27fd9adebab32041f755214dbc6bffa90cc0225b39da2e5c2d3b90600090a26000825111806103f65750805b156101d557610405838361026c565b50505050565b7f7e644d79422f17c01e4894b5f4f588d331ebfa28653d42ae832dc59e38c9798f610434610379565b604080516001600160a01b03928316815291841660208301520160405180910390a161016181610611565b606061046a84610298565b6104c55760405162461bcd60e51b815260206004820152602660248201527f416464726573733a2064656c65676174652063616c6c20746f206e6f6e2d636f6044820152651b9d1c9858dd60d21b6064820152608401610338565b600080856001600160a01b0316856040516104e09190610785565b600060405180830381855af49150503d806000811461051b576040519150601f19603f3d011682016040523d82523d6000602084013e610520565b606091505b509150915061053082828661069d565b9695505050505050565b60007f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc61039d565b61056b81610298565b6105cd5760405162461bcd60e51b815260206004820152602d60248201527f455243313936373a206e657720696d706c656d656e746174696f6e206973206e60448201526c1bdd08184818dbdb9d1c9858dd609a1b6064820152608401610338565b807f360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc5b80546001600160a01b0319166001600160a01b039290921691909117905550565b6001600160a01b0381166106765760405162461bcd60e51b815260206004820152602660248201527f455243313936373a206e65772061646d696e20697320746865207a65726f206160448201526564647265737360d01b6064820152608401610338565b807fb53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d61036105f0565b606083156106ac575081610291565b8251156106bc5782518084602001fd5b8160405162461bcd60e51b815260040161033891906107a1565b80356001600160a01b038116811461029d57600080fd5b6000602082840312156106fe578081fd5b610291826106d6565b60008060006040848603121561071b578182fd5b610724846106d6565b9250602084013567ffffffffffffffff80821115610740578384fd5b818601915086601f830112610753578384fd5b813581811115610761578485fd5b876020828501011115610772578485fd5b6020830194508093505050509250925092565b600082516107978184602087016107d4565b9190910192915050565b60006020825282518060208401526107c08160408501602087016107d4565b601f01601f19169190910160400192915050565b60005b838110156107ef5781810151838201526020016107d7565b83811115610405575050600091015256fe416464726573733a206c6f772d6c6576656c2064656c65676174652063616c6c206661696c6564a264697066735822122093f028255035b61df476b13b9dba3c4f06f60e51b9b4caee31680b389aef327f64736f6c63430008020033b53127684a568b3173ae13b9f8a6016e243e63b6e8ee1178d6a717850b5d6103360894a13ba1a3210667c828492db98dca3e2076cc3735a920a3ca505d382bbc416464726573733a206c6f772d6c6576656c2064656c65676174652063616c6c206661696c65640000000000000000000000007d94972475898092507358c0037b53f368983c60000000000000000000000000b011ae0f3a8dcc0c02481221cd28acdf0367a6570000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000004454176949000000000000000000000000000000000000000000000000000000000000008968747470733a2f2f39613733652e77656273697465000000000000000000000000000000000000000000000000000000000000000000000000000000"
        const val FUNC_ALLOWANCE = "allowance"
        const val FUNC_APPROVE = "approve"
        const val FUNC_BALANCEOF = "balanceOf"
        const val FUNC_DECIMALS = "decimals"
        const val FUNC_DECREASEALLOWANCE = "decreaseAllowance"
        const val FUNC_INCREASEALLOWANCE = "increaseAllowance"
        const val FUNC_NAME = "name"
        const val FUNC_OWNER = "owner"
        const val FUNC_RENOUNCEOWNERSHIP = "renounceOwnership"
        const val FUNC_SYMBOL = "symbol"
        const val FUNC_TOTALSUPPLY = "totalSupply"
        const val FUNC_TRANSFER = "transfer"
        const val FUNC_TRANSFERFROM = "transferFrom"
        const val FUNC_TRANSFEROWNERSHIP = "transferOwnership"
        const val FUNC_INITIALIZE = "initialize"
        const val FUNC_ISTRUSTEDFORWARDER = "isTrustedForwarder"
        const val FUNC_SETTRUSTEDFORWARDER = "setTrustedForwarder"
        val APPROVAL_EVENT = Event(
            "Approval",
            listOf<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Uint256?>() {})
        )
        val OWNERSHIPTRANSFERRED_EVENT = Event(
            "OwnershipTransferred",
            listOf<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {})
        )
        val TRANSFER_EVENT = Event(
            "Transfer",
            listOf<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Uint256?>() {})
        )

        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            credentials: Credentials?,
            contractGasProvider: ContractGasProvider?
        ): KhoToken {
            return KhoToken(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            contractGasProvider: ContractGasProvider?
        ): KhoToken {
            return KhoToken(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(
            web3j: Web3j?,
            credentials: Credentials?,
            contractGasProvider: ContractGasProvider?
        ): RemoteCall<KhoToken> {
            return deployRemoteCall(
                KhoToken::class.java,
                web3j,
                credentials,
                contractGasProvider,
                BINARY,
                ""
            )
        }

        fun deploy(
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            contractGasProvider: ContractGasProvider?
        ): RemoteCall<KhoToken> {
            return deployRemoteCall(
                KhoToken::class.java,
                web3j,
                transactionManager,
                contractGasProvider,
                BINARY,
                ""
            )
        }

    }
}