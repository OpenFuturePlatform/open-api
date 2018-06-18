import eth from "./eth";
import {OPEN_ABI, OPEN_ADDRESS} from "../const/open";

export const openToken = eth ? eth.contract(OPEN_ABI).at(OPEN_ADDRESS) : null;

export default openToken;
