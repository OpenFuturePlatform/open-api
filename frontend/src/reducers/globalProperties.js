import {SET_GLOBAL_PROPERTIES} from "../actions/types";

const networkMap = {
  1: {id: 1, name: 'Main Ethereum Network'},
  2: {id: 2, name: 'Morden Network'},
  3: {id: 3, name: 'Ropsten Network'},
  4: {id: 4, name: 'Rinkeby Test Network'},
  42: {id: 42, name: 'Kovan Network'},
  0: {id: 'local', name: 'Localhost 8545'}
};

const initialState = {
  network: null
};

const globalProperties = (state = initialState, action) => {
  switch (action.type) {
    case SET_GLOBAL_PROPERTIES:
      const netId = action.payload.networkVersion;
      const network = networkMap[netId] || {id: netId, name: `Network with id=${netId} (perhaps Localhost 8545)`};

      return {...state, ...action.payload, network};
    default:
      return state;
  }
};

export default globalProperties;
