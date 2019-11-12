import React, {Component} from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';
import {connect} from 'react-redux';
import ScaffoldForm from './EthereumScaffoldForm';
import DeployingModal from '../components/DeployingModal';
import WithdrawModal from '../components/WithdrawModal';
import {ScaffoldSummaryContainer} from './EthereumScaffoldSummary';
import EthereumDashboard from './EthereumDashboard';
import OpenDashboard from './OpenDashboard';
import OpenScaffoldForm from './OpenScaffoldForm';

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
          <Route exact path={`${url}/ethereum`} component={EthereumDashboard} />
          <Route path={`${url}/ethereum/new`} component={ScaffoldForm} />
          <Route
            path={`${url}/ethereum/:scaffoldAddress`}
            component={ScaffoldSummaryContainer}
          />
          <Route exact path={`${url}/open`} component={OpenDashboard} />
          <Route path={`${url}/open/new`} component={OpenScaffoldForm} />
          <Redirect from="*" to={`${url}/ethereum`} />
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
