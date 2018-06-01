import React, {Component} from 'react';
import {Container} from 'semantic-ui-react';
import {Route, Switch} from 'react-router-dom';
import {connect} from 'react-redux';
import * as actions from '../actions';

import Header from './Header';
import Dashboard from './Dashboard';
import ScaffoldForm from './scaffolds/ScaffoldForm';
import ScaffoldSummary from './scaffolds/ScaffoldSummary';
import DeployingModal from './DeployingModal';
import WithdrawModal from './WithdrawModal';


class Scaffolds extends Component {

  componentDidMount() {
    this.fetchUser();
  }

  async fetchUser() {
    try {
      await this.props.fetchUser();
    } catch(e) {
      // if 404 then
      // this.props.history.push('/');
    }
  }

  render() {
    const url = this.props.match.url;

    return (
      <Container>
        <Header/>
        <Switch>
          <Route exact path={`${url}/`} component={Dashboard}/>
          <Route path={`${url}/new`} component={ScaffoldForm}/>
          <Route path={`${url}/:scaffoldAddress`} component={ScaffoldSummary}/>
        </Switch>
        <DeployingModal modalInfo={this.props.modalInfo}/>
        <WithdrawModal withdrawModalInfo={this.props.withdrawModalInfo}/>
      </Container>
    );
  }
}

function mapStateToProps({ modalInfo, withdrawModalInfo }) {
  return { modalInfo, withdrawModalInfo };
}

export default connect(mapStateToProps, actions)(Scaffolds);
