import React, {Component} from "react";
import {Card, Grid} from "semantic-ui-react";
import {WordWrap} from "../components-ui/WordWrap";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {fetchGatewayApplicationDetails, generateGatewayApplicationWallet} from "../actions/gateways";
import {GatewayApplicationWallet} from "../components/GatewayApplicationWallet";

class GatewayApplicationSummary extends Component {
    async componentDidMount() {
        const gatewayId = this.getGatewayId();
        await this.props.actions.fetchGatewayApplicationDetails(gatewayId);
    }

    componentDidUpdate(prevProps) {
        const { byApiMethod } = this.props;
        const byApiMethodChanged = prevProps.byApiMethod !== byApiMethod;

        if (byApiMethodChanged) {
            const gatewayId = this.getGatewayId();
            console.log(gatewayId);
            this.props.actions.fetchGatewayApplicationDetails(gatewayId);
        }
    }

    getGatewayId = () => this.props.match.params.id;

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
                                        Gateway Title: <WordWrap>{gateway.name}</WordWrap>{' '}
                                    </div>
                                </Card.Content>
                                <Card.Content>
                                    <div>
                                        Access Key: <WordWrap>{gateway.apiAccessKey}</WordWrap>{' '}
                                    </div>
                                    <div>
                                        Secret Key:  <WordWrap>{gateway.apiSecretKey}</WordWrap>{' '}
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
            fetchGatewayApplicationDetails
        },
        dispatch
    )
});

export const GatewayApplicationSummaryContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(GatewayApplicationSummary);