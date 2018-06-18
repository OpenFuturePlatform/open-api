import './css/main.css';
import React from 'react';
import {BrowserRouter, Redirect, Route, Switch} from 'react-router-dom';

import Landing from './scenes/Landing';
import Scaffolds from './scenes/Scaffolds';

const App = () => (
  <BrowserRouter>
    <Switch>
      <Route exact path="/" component={Landing}/>
      <Route path="/scaffolds" component={Scaffolds}/>
      <Redirect from="*" to="/"/>
    </Switch>
  </BrowserRouter>
);

export default App;
