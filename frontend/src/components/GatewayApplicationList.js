import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Card, Divider, Grid, Icon} from 'semantic-ui-react';
import {ProjectPagination} from '../components-ui/ProjectPagination';
import {GATEWAY_LIMIT} from '../const';
import {
    fetchGatewayApplications,
    getGatewayApplicationWallet,
    removeGatewayApplication
} from "../actions/gateways";
import {GatewayApplicationRemove} from "./GatewayApplicationRemove";
import {WordWrap} from "../components-ui/WordWrap";
import {Link} from "react-router-dom";


class GatewayApplicationList extends Component {
    componentDidMount() {
        this.fetchGatewayApplications();
    }

    fetchGatewayApplications = (offset = 0, limit = GATEWAY_LIMIT) => {
        this.props.fetchGatewayApplications(offset, limit);
    };

    onRemoveGateway = gateway => {
        this.props.removeGatewayApplication(gateway.id);
    }

    onFetchWallet = gateway => this.props.getGatewayApplicationWallet(gateway.id)

    renderApplications() {
        const gateways = this.props.gateways;

        return gateways.list.map((gateway, index) => {
            return (
                <Card fluid key={index}>
                    <Card.Content>

                        <Grid.Row>
                            <Grid.Column width={8}>
                                <Link to={`/applications/${gateway.id}`}>
                                    <Card.Header>
                                        <WordWrap>{gateway.name}</WordWrap>
                                    </Card.Header>
                                </Link>
                                <div className="meta">
                                    Webhook: {gateway.webHook}
                                </div>
                            </Grid.Column>
                        </Grid.Row>
                    </Card.Content>
                    <Card.Content extra>
                        <div className='ui three buttons'>

                            <Button basic color='red'>
                                <GatewayApplicationRemove gateway={gateway} onSubmit={() => this.onRemoveGateway(gateway)} />
                            </Button>

                        </div>
                    </Card.Content>
                </Card>
            );
        });
    }

    render() {
        const gateways = this.props.gateways;
        return (
            <Grid.Row>
                <Grid.Column width={16}>
                    {this.renderApplications()}
                    <ProjectPagination limit={GATEWAY_LIMIT} totalCount={gateways.totalCount} onChange={this.fetchGatewayApplications} />
                </Grid.Column>
            </Grid.Row>
        );
    }
}
const mapStateToProps = ({ gateways }) => ({ gateways: gateways });

export default connect(
    mapStateToProps,
    {
        fetchGatewayApplications,
        removeGatewayApplication,
        getGatewayApplicationWallet
    }
)(GatewayApplicationList);
