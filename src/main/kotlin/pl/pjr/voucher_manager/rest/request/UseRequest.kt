package pl.pjr.voucher_manager.rest.request

import pl.pjr.voucher_manager.domain.Country

class UseRequest(
    val code: String,
    val country: Country
)