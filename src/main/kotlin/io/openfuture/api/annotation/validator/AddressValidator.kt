package io.openfuture.api.annotation.validator

import io.openfuture.api.annotation.Address
import io.openfuture.api.util.EthereumUtils
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AddressValidator : ConstraintValidator<Address, Any> {

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        return value is String && EthereumUtils.isValidChecksumAddress(value)
    }

}