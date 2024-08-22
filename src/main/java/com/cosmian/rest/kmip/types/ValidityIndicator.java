package com.cosmian.rest.kmip.types;

import com.cosmian.rest.kmip.json.KmipEnumUtils;

import java.util.Map;

/**
 * @author chenrenfu
 * @date 2024/8/22 13:59
 * @packageName:com.cosmian.rest.kmip.types
 * @className: ValidateState
 */
public enum ValidityIndicator {
    Valid(0x0000_0001),
    InValid(0x0000_0002),
    Unknown(0x0000_0003);
    private final int code;

    private ValidityIndicator(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return KmipEnumUtils.to_string(this);
    }

    static Map<String, ValidityIndicator> ENUM_MAP =
            KmipEnumUtils.to_map(ValidityIndicator.values());

    public static ValidityIndicator from(String name) throws IllegalArgumentException {
        ValidityIndicator o = ENUM_MAP.get(name);
        if (o == null) {
            throw new IllegalArgumentException("No ValidityIndicator with name: " + name);
        }
        return o;
    }

    public static ValidityIndicator from(int code) throws IllegalArgumentException {
        for (ValidityIndicator value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("No ValidityIndicator with code: " + code);
    }
}
