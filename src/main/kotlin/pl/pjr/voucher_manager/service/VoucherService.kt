package pl.pjr.voucher_manager.service

import org.springframework.stereotype.Service
import pl.pjr.voucher_manager.dao.RedisDao
import pl.pjr.voucher_manager.dao.RedisDaoImpl
import pl.pjr.voucher_manager.domain.Country
import pl.pjr.voucher_manager.domain.Voucher

@Service
class VoucherService(val redisDaoImpl: RedisDao, val codeGenerator: CodeGenerator) {

    fun create(times: Int): Voucher {
        return create(times, allowedCountries = setOf(Country.PL))
    }

    fun create(times: Int, allowedCountries: Set<Country>): Voucher {
        val code = codeGenerator.generateNewCode()
        if (!verifyIfExists(code)) {
            redisDaoImpl.set("$code-${allowedCountries}", times.toString())
            codeGenerator.addCode(code)
            return Voucher(times, code, allowedCountries)
        }
        throw IllegalStateException("Generated code already exists")
    }

    fun verifyIfAllowed(voucher: Voucher, country: Country): Boolean {
        val allowedCountries = voucher.getAllowedCountries()
        if (!allowedCountries.contains(country)) {
            throw RuntimeException("Voucher not allowed to be used in $country")
        }
        return true;
    }

    fun verifyIfExists(code: String): Boolean {
        redisDaoImpl.get(code) ?: return false
        return true
    }

    fun use(voucherCode: String) {
        use(voucherCode, Country.PL)
    }

    fun use(voucherCode: String, country: Country) {
        redisDaoImpl.scan()
            .stream()
            .filter {
                it.startsWith(voucherCode, ignoreCase = true)
            }.toList()
            .firstOrNull()?.let {
                val codeAndCountries = it.split("-")
                val code = codeAndCountries[0]
                val currentUse = redisDaoImpl.get(it)?.toInt()?:throw IllegalStateException()
                val countries = codeAndCountries[1].subSequence(1, codeAndCountries[1].length-1).split(",").map { country ->
                    Country.valueOf(country.uppercase())
                }.toSet()
                val voucher = Voucher(currentUse, code, countries)

                require(verifyIfAllowed(voucher, country))
                if (voucher.getCurrentUse() == 0) {
                    throw RuntimeException("Voucher has zero number of uses")
                }
                voucher.useOnce()
                redisDaoImpl.set(voucher.toString(), voucher.getCurrentUse().toString())
                return
            }
        throw IllegalArgumentException("No Voucher found with code $voucherCode")
    }

    fun getCurrentUse(voucherString: String): Int {
        return redisDaoImpl.get(voucherString)?.toInt()?: throw IllegalStateException()
    }
}