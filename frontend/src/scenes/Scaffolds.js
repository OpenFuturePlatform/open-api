import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import { connect } from 'react-redux';
import ScaffoldForm from './ScaffoldForm';
import DeployingModal from '../components/DeployingModal';
import WithdrawModal from '../components/WithdrawModal';
import { ScaffoldSummaryContainer } from './ScaffoldSummary';
import Dashboard from './Dashboard';

class Scaffolds extends Component {
  render() {
    const { auth, globalProperties } = this.props;
    const url = this.props.match.url;

    if (!auth || !globalProperties) {
      return null;
    }

    return (
      <div>
        <Switch>
          <Route exact path={`${url}/`} component={Dashboard} />
          <Route path={`${url}/new`} component={ScaffoldForm} />
          <Route
            path={`${url}/:scaffoldAddress`}
            component={ScaffoldSummaryContainer}
          />
        </Switch>
        <DeployingModal modalInfo={this.props.modalInfo} />
        <WithdrawModal withdrawModalInfo={this.props.withdrawModalInfo} />
      </div>
    );
  }
}

function mapStateToProps({
  modalInfo,
  withdrawModalInfo,
  auth,
  globalProperties
}) {
  return { modalInfo, withdrawModalInfo, auth, globalProperties };
}

export default connect(mapStateToProps)(Scaffolds);
