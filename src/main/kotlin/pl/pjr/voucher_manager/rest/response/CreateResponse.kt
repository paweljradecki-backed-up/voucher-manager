package pl.pjr.voucher_manager.rest.response

import pl.pjr.voucher_manager.domain.Voucher

class CreateResponse(
    val currentUse: Int,
)

fun Voucher.toCreateResponse() : CreateResponse {
    return CreateResponse(this.getCurrentUse())
}