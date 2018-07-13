import React, { Component } from 'react';
import { Route, Switch, Redirect } from 'react-router-dom';
import { fetchGlobalProperties } from './actions/global-properties';
import { fetchUser } from './actions/index';
import { connect } from 'react-redux';
import Scaffolds from './scenes/Scaffolds';
import Keys from './scenes/Keys';
import Header from './components/Header';
import { Container } from 'semantic-ui-react';
import './css/main.css';

class App extends Component {
  async componentDidMount() {
    await this.props.fetchUser();
    await this.props.fetchGlobalProperties();
  }

  renderAuthorizedContent = () => {
    const { auth, globalProperties } = this.props;
    if (!auth.isAuthorized || !globalProperties.network.id) {
      return null;
    }

    return (
      <Switch>
        <Route path="/scaffolds" component={Scaffolds} />
        <Route path="/keys" component={Keys} />
        <Redirect from="*" to="/scaffolds" />
      </Switch>
    );
  };

  render() {
    return (
      <Container style={{ paddingTop: '10px', paddingBottom: '50px' }}>
        <Header />
        {this.renderAuthorizedContent()}
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
