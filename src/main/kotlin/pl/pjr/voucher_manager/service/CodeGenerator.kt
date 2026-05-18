package pl.pjr.voucher_manager.service

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CodeGenerator() {

    private val codes: MutableSet<String> = mutableSetOf()

    private val allowedChars = ('A'..'Z')

    fun addCode(code: String) {
        codes.add(code)
    }

    fun generateNewCode(): String {
        while(true) {
            val code =
                (0..9)
                .map { allowedChars.random() }
                .joinToString("")
            if (!codes.contains(code)) {
                return code
            }
        }
    }
}

