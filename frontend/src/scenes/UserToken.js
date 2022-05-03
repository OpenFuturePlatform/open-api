import React, {Component} from "react";
import {Redirect, Route, Switch} from "react-router-dom";
import GatewayApplicationDashboard from "./GatewayApplicationDashboard";
import GatewayApplicationForm from "./GatewayApplicationForm";
import {GatewayApplicationSummaryContainer} from "./GatewayApplicationSummary";
import {connect} from "react-redux";
import UserTokenDashboard from "./UserTokenDashboard";
import UserTokenForm from "./UserTokenForm";

class UserToken extends Component {
  render() {
    const { auth, globalProperties } = this.props;
    const url = this.props.match.url;

    if (!auth || !globalProperties) {
      return null;
    }

    return (
      <div>
        <Switch>
          <Route exact path={`${url}`} component={UserTokenDashboard} />
          <Route path={`${url}/new`} component={UserTokenForm} />
        </Switch>
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

export default connect(mapStateToProps)(UserToken);
