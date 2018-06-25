import eth from "../utils/eth";
import {OPEN_ABI} from "../const/open";

export const openTokenSelector = (state) => {
  const openAddress = state.globalProperties.openTokenAddress;
  return eth ? eth.contract(OPEN_ABI).at(openAddress) : null;
};

