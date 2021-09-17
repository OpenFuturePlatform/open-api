import React, {Component} from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';
import {connect} from 'react-redux';
import GatewayApplicationForm from './GatewayApplicationForm';
import GatewayApplicationDashboard from "./GatewayApplicationDashboard";


class GatewayApplication extends Component {
    render() {
        const { auth, globalProperties } = this.props;
        const url = this.props.match.url;

        if (!auth || !globalProperties) {
            return null;
        }

        return (
            <div>
                <Switch>
                    <Route exact path={`${url}`} component={GatewayApplicationDashboard} />
                    <Route path={`${url}/new`} component={GatewayApplicationForm} />
                    <Redirect from="*" to={`${url}/application`} />
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

export default connect(mapStateToProps)(GatewayApplication);
