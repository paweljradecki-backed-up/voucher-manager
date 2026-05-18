package pl.pjr.voucher_manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VoucherManagerApplication

fun main(args: Array<String>) {
	runApplication<VoucherManagerApplication>(*args)
}
