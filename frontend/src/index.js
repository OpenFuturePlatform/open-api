import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { applyMiddleware, createStore } from 'redux';
import reduxThunk from 'redux-thunk';
import { BrowserRouter, Switch, Redirect, Route } from 'react-router-dom';
import { loadState, saveState } from './utils/local-storage';
import { throttle } from 'lodash';
import reducers from './reducers';
import App from './App';
import Landing from './scenes/Landing';

const initState = loadState();
const store = createStore(reducers, initState, applyMiddleware(reduxThunk));

store.subscribe(
  throttle(
    () =>
      saveState({
        ethAccount: {
          activating: store.getState().ethAccount.activating,
          activatingHash: store.getState().ethAccount.activatingHash
        }
      }),
    1000
  )
);

ReactDOM.render(
  <Provider store={store}>
    <BrowserRouter>
      <Switch>
        <Route exact path="/" component={Landing} />
        <Route path="/" component={App} />
        <Redirect from="*" to="/" />
      </Switch>
    </BrowserRouter>
  </Provider>,
  document.querySelector('#root')
);
