import {SET_GLOBAL_PROPERTIES} from "../actions/types";
import {LOCAL_NETWORK, MAIN_NETWORK, RINKEBY_NETWORK} from "../const/index";

const networkMap = {
  [MAIN_NETWORK]: {id: 1, name : 'Main Ethereum Network'},
  [RINKEBY_NETWORK]: {id: 4, name: 'Rinkeby Test Network'},
  [LOCAL_NETWORK]: {id: 'local', name: 'Localhost 8545'}
};

const initialState = {
  network: {id: null, name: ''}
};

const globalProperties = (state = initialState, action) => {
  switch (action.type) {
    case SET_GLOBAL_PROPERTIES:
      const infura = action.payload.infura;
      const network = networkMap[infura];

      if (!network) {
        throw new Error('Infura from global properties is not allowed');
      }

      return {...state, ...action.payload, network};
    default:
      return state;
  }
};

export default globalProperties;
