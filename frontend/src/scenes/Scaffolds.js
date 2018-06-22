import React, {Component} from 'react';
import {Container} from 'semantic-ui-react';
import {Route, Switch} from 'react-router-dom';
import {connect} from 'react-redux';
import {fetchUser} from '../actions/index';
import {fetchGlobalProperties} from '../actions/global-properties';
import Header from '../components/Header';
import ScaffoldForm from './ScaffoldForm';
import DeployingModal from '../components/DeployingModal';
import WithdrawModal from '../components/WithdrawModal';
import {ScaffoldSummaryContainer} from './ScaffoldSummary';
import Dashboard from './Dashboard';

class Scaffolds extends Component {

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
    const {auth} = this.props;
    const url = this.props.match.url;

    if (!auth) {
      return null;
    }

    return (
      <Container>
        <Header/>
        <Switch>
          <Route exact path={`${url}/`} component={Dashboard}/>
          <Route path={`${url}/new`} component={ScaffoldForm}/>
          <Route path={`${url}/:scaffoldAddress`} component={ScaffoldSummaryContainer}/>
        </Switch>
        <DeployingModal modalInfo={this.props.modalInfo}/>
        <WithdrawModal withdrawModalInfo={this.props.withdrawModalInfo}/>
      </Container>
    );
  }
}

function mapStateToProps({modalInfo, withdrawModalInfo, auth}) {
  return {modalInfo, withdrawModalInfo, auth};
}

export default connect(mapStateToProps, {fetchGlobalProperties, fetchUser})(Scaffolds);
