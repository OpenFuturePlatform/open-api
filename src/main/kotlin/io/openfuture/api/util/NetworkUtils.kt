package io.openfuture.api.util

import org.apache.commons.net.util.SubnetUtils

fun getIpRange(subnet: String): ArrayList<String> {
    val utils = SubnetUtils(subnet)

    return utils.info.allAddresses!!.toCollection(ArrayList())
}
