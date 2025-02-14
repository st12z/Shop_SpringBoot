package com.thucjava.shopapp.config;

import com.thucjava.shopapp.utils.VNPayUtil;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "payment.vnpay")
public class VNPayConfig {
    private String vnp_PayUrl;
    private String vnp_ReturnUrl;
    private String vnp_TmnCode;
    private String secretKey;
    private String vnp_Version;
    private String vnp_Command;
    private String orderType;
    public Map<String,String> getVNPayConfig() {
        Map<String,String> config = new HashMap<String,String>();
        config.put("vnp_ReturnUrl", vnp_ReturnUrl);
        config.put("vnp_TmnCode", vnp_TmnCode);
        config.put("vnp_Version", vnp_Version);
        config.put("vnp_Command", vnp_Command);
        config.put("vnp_OrderType", orderType);
        config.put("vnp_CurrCode","VND");
        config.put("vnp_TxnRef", VNPayUtil.getRandomString(8));
        config.put("vnp_OrderInfo","Thanh toan don hang:");
        config.put("vnp_Locale","vn");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = sdf.format(cal.getTime());
        config.put("vnp_CreateDate", vnpCreateDate);
        cal.add(Calendar.MINUTE,10);
        String vnpExpireDate = sdf.format(cal.getTime());
        config.put("vnp_ExpireDate", vnpExpireDate);
        return config;
    }
}
