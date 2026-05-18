package pl.pjr.voucher_manager.domain

import tools.jackson.databind.deser.jdk.UUIDDeserializer
import java.util.UUID

data class Voucher(
    private var currentUse: Int,
    private val allowedCountries: Set<Country> = setOf(Country.PL),
    private val code: String,
) {


    constructor(times: Int, code: String, allowedCountries: Set<Country>) : this(times, allowedCountries, code)

    fun useOnce() {
        currentUse -= 1
    }

    fun getCode() = code

    fun getCurrentUse() = currentUse

    fun getAllowedCountries() = allowedCountries

    override fun toString() = "${code}-[${allowedCountries.joinToString(",")}]"


}
