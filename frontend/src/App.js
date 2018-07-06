import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import { fetchGlobalProperties } from './actions/global-properties';
import { fetchUser } from './actions/index';
import { connect } from 'react-redux';
import Scaffolds from './scenes/Scaffolds';
import Keys from './scenes/Keys';
import Header from './components/Header';
import { Container } from 'semantic-ui-react';
import './css/main.css';

class App extends Component {
  componentDidMount() {
    this.fetchUser();
    this.props.fetchGlobalProperties();
  }

  async fetchUser() {
    try {
      await this.props.fetchUser();
    } catch (e) {
      // if 404 then
      // this.props.history.push('/');
    }
  }

  render() {
    const { auth, globalProperties } = this.props;

    if (!auth || !globalProperties) {
      return null;
    }

    return (
      <Container style={{ paddingTop: '10px', paddingBottom: '50px' }}>
        <Header />
        <Switch>
          <Route path="/scaffolds" component={Scaffolds} />
          <Route path="/keys" component={Keys} />
        </Switch>
      </Container>
    );
  }
}

function mapStateToProps({ auth, globalProperties }) {
  return { auth, globalProperties };
}

export default connect(
  mapStateToProps,
  { fetchGlobalProperties, fetchUser }
)(App);
