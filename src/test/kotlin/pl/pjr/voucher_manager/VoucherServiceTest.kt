package pl.pjr.voucher_manager

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import pl.pjr.voucher_manager.dao.RedisDao
import pl.pjr.voucher_manager.service.VoucherService
import kotlin.test.Test

import pl.pjr.voucher_manager.domain.Country
import pl.pjr.voucher_manager.service.CodeGenerator
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VoucherServiceTest {
	val hashMap = mutableMapOf<String, String>()
	val codeGenerator = CodeGenerator()
	val hashMapImpl = object : RedisDao {
		override fun set(keyName: String, keyValue: String) {
            hashMap[keyName] = keyValue
		}

        override fun get(keyName: String): String? {
            return hashMap[keyName]
        }

        override fun scan(): List<String> {
            return hashMap.keys.toList()
        }
    }
	val voucherService = VoucherService(hashMapImpl, codeGenerator)

	@Test
	fun whenCreatedThreeTimeUseVoucherAndUsedItThreeTimes_thenCurrentUseIsZero() {
        val voucher = voucherService.create(3)
		repeat(3) {
			voucherService.use(voucher.getCode())
		}

		assertEquals(0, voucherService.getCurrentUse(voucher.toString()))
	}

	@Test
	fun whenCreatedOneTimeVoucherAndUsedItWithLowercaseCode_thenCurrentUseIsZero() {
		val voucher = voucherService.create(1)

		voucherService.use(voucher.getCode().lowercase())

		assertEquals(0, voucherService.getCurrentUse(voucher.toString()))
	}

	@Test
	fun whenCreatedVoucherAndUsedItWithIncorrectCode_thenThrowException() {
		voucherService.create(3)

		val exception = assertFailsWith<IllegalArgumentException> {
			voucherService.use("ABCDEFGHIJ")
		}
		assertThat(exception.message, equalTo("No Voucher found with code ABCDEFGHIJ"))
	}

	@Test
	fun whenCreatedNTimesUseVoucherAndUsedItNPlusOneTimes_thenThrowException() {
		val times: Int = Random.nextInt(1, 100)

		val exception = assertFailsWith<RuntimeException> {
			val voucher = voucherService.create(times)
			repeat(times + 1) {
				voucherService.use(voucher.getCode())
			}
		}
		assertThat(exception.message, equalTo("Voucher has zero number of uses"))
	}

	@Test
	fun whenCreatedVoucherForGermanyAndUsedItInPoland_thenThrowException() {
		val voucher = voucherService.create(1, allowedCountries = setOf(Country.DE, Country.CZ) )

		val exception = assertFailsWith<RuntimeException> {
			voucherService.verifyIfAllowed(voucher, Country.PL)
		}
		assertThat(exception.message, equalTo("Voucher not allowed to be used in PL"))
	}

}
