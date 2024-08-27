package com.nhnacademy.batch.batch.coupon.feign;

import com.nhnacademy.batch.utils.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 쿠폰 API 클라이언트.
 *
 * @author 김병우
 */
@FeignClient(name = "CouponControllerClient", url = "http://${feign.coupon.url}")
public interface CouponControllerClient {
    @GetMapping("/coupons/birthdays")
    ApiResponse<Void> registerCouponBook(@RequestHeader("Member-Id") Long memberId);
}
