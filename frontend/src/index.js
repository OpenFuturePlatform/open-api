import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {applyMiddleware, createStore} from 'redux';
import reduxThunk from 'redux-thunk';

import reducers from './reducers';
import App from './App';
import {loadState, saveState} from './utils/local-storage';
import {throttle} from 'lodash';

const initState = loadState();
const store = createStore(reducers, initState, applyMiddleware(reduxThunk));

store.subscribe(throttle(() => saveState({
  ethAccount: {
    activating: store.getState().ethAccount.activating,
    activatingHash: store.getState().ethAccount.activatingHash
  }
}), 1000));

ReactDOM.render(
  <Provider store={store}><App/></Provider>,
  document.querySelector('#root')
);
