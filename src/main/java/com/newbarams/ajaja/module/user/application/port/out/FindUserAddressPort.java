package com.newbarams.ajaja.module.user.application.port.out;

import com.newbarams.ajaja.module.remind.application.model.RemindAddress;

public interface FindUserAddressPort {
	RemindAddress findUserAddressByUserId(Long userId);
}
