import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { applyMiddleware, createStore } from 'redux';
import reduxThunk from 'redux-thunk';
import { BrowserRouter, Switch, Redirect, Route } from 'react-router-dom';
import reducers from './reducers';
import App from './App';
import Landing from './scenes/Landing';

const initState = {};
const store = createStore(reducers, initState, applyMiddleware(reduxThunk));

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
