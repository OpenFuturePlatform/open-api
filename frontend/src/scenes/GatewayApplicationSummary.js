import React, {Component} from "react";
import {Card, Grid} from "semantic-ui-react";
import {WordWrap} from "../components-ui/WordWrap";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {fetchGatewayApplicationDetails} from "../actions/gateways";

import {GatewayApplicationWallet} from "../components/GatewayApplicationWallet";
import {getGatewayApplicationWallet} from "../actions/gateway-wallet";
import {KeyGenerate} from "../components/KeyGenerate";
import {WalletGenerate} from "../components/GatewayApplicationWalletGenerate";
import {GatewayKeyGenerate} from "../components/GatewayApplicationKeyGenerate";

class GatewayApplicationSummary extends Component {
    async componentDidMount() {
        const gatewayId = this.getGatewayId();
        await this.props.actions.fetchGatewayApplicationDetails(gatewayId);
        await this.props.actions.getGatewayApplicationWallet(gatewayId);
    }

    componentDidUpdate(prevProps) {
        const { byApiMethod } = this.props;
        const byApiMethodChanged = prevProps.byApiMethod !== byApiMethod;

        if (byApiMethodChanged) {
            const gatewayId = this.getGatewayId();
            this.props.actions.fetchGatewayApplicationDetails(gatewayId);
        }
    }

    getGatewayId = () => this.props.match.params.id;

    onKeyGenerate = () => {
      const { gateway } = this.props;
    }

    render() {
        const { gateway } = this.props;

        if (!gateway) {
            return null;
        }

        return (
            <div style={{ marginTop: '20px' }}>
                <Grid>
                    <Grid.Row>

                        <Grid.Column width={16}>
                            <Card fluid>
                                <Card.Content>
                                    <div>
                                        <strong> <WordWrap>{gateway.name}</WordWrap>{' '}</strong>
                                    </div>
                                </Card.Content>
                                <Card.Content>
                                    <GatewayKeyGenerate  gateway={gateway} onSubmit={this.onKeyGenerate()} />
                                    <div className="table-key"><strong>Access Key</strong></div>
                                    <div className="table-value table-value-background-color access-key selectable"
                                         id="credentials-sb">{gateway.apiAccessKey}
                                    </div>

                                    <div className="table-key"><strong>Secret Key</strong></div>
                                    <div className="table-value table-value-background-color secret-key selectable"
                                         id="credentials-sb">{gateway.apiSecretKey}
                                    </div>

                                </Card.Content>
                            </Card>

                        </Grid.Column>
                    </Grid.Row>
                    <Grid.Row>
                        <Grid.Column />
                    </Grid.Row>
                </Grid>
                <GatewayApplicationWallet gateway={gateway}/>
            </div>
        );
    }
}

const mapStateToProps = (
    { gatewayById, auth: { byApiMethod } },
    {
        match: {
            params: { id }
        }
    }
) => {
    const gatewaySet = gatewayById[id] || {};

    return { byApiMethod, ...gatewaySet };
};

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(
        {
            fetchGatewayApplicationDetails,
            getGatewayApplicationWallet
        },
        dispatch
    )
});

export const GatewayApplicationSummaryContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(GatewayApplicationSummary);
