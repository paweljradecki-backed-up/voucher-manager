package pl.pjr.voucher_manager.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.pjr.voucher_manager.dao.RedisDaoImpl
import pl.pjr.voucher_manager.rest.request.CreateRequest
import pl.pjr.voucher_manager.rest.request.UseRequest
import pl.pjr.voucher_manager.rest.response.CreateResponse
import pl.pjr.voucher_manager.rest.response.UseResponse
import pl.pjr.voucher_manager.rest.response.toCreateResponse
import pl.pjr.voucher_manager.service.VoucherService


@RestController
@RequestMapping("/api/voucher")
class VoucherRestController(
    private val service: VoucherService,
    private val redisDaoImpl: RedisDaoImpl
) {

    @GetMapping("/echo")
    fun echo() = "ECHO"

    @RequestMapping(method = [RequestMethod.POST])
    fun create(@RequestBody request: CreateRequest): CreateResponse {
        return service.create(request.times)
            .toCreateResponse()
    }

    @RequestMapping(method = [RequestMethod.PUT])
    fun use(@RequestBody request: UseRequest): ResponseEntity<UseResponse> {
        try {
            service.use(request.code, request.country)
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UseResponse(false));
        }
        return ResponseEntity.ok(UseResponse())
    }
}

