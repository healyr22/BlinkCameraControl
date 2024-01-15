package com.robware;

import com.robware.json.JsonMapper;
import org.junit.jupiter.api.Test;

public class TempTest {

    record Tempy(String hello, Boolean isbool){}

    @Test
    public void test() throws Exception {
        var str = """
                {"hello": "value"}""";
        var result = JsonMapper.mapper().readValue(str, Tempy.class);

        System.out.println("Got result: " + result);
    }

    static final String content = """
            {
                "account": {
                    "account_id": 25739,
                    "user_id": 25739,
                    "client_id": 1039644,
                    "client_trusted": false,
                    "new_account": false,
                    "tier": "u020",
                    "region": "ap",
                    "account_verification_required": false,
                    "phone_verification_required": false,
                    "client_verification_required": true,
                    "require_trust_client_device": true,
                    "country_required": false,
                    "verification_channel": "phone",
                    "user": {
                        "user_id": 25739,
                        "country": "CA"
                    },
                    "amazon_account_linked": true,
                    "braze_external_id": "f4a401b0c5a25f5b7c92a2e2c4689c9a01171d33d287b7ffc71ddcbaab77fc54"
                },
                "auth": {
                    "token": "NMB42NFVGkNc0hdHo2jX-A"
                },
                "phone": {
                    "number": "+1******6513",
                    "last_4_digits": "6513",
                    "country_calling_code": "1",
                    "valid": true
                },
                "verification": {
                    "email": {
                        "required": false
                    },
                    "phone": {
                        "required": true,
                        "channel": "sms"
                    }
                },
                "lockout_time_remaining": 0,
                "force_password_reset": false,
                "allow_pin_resend_seconds": 90
            }""";
}
