import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Card, Divider, Grid, Icon} from 'semantic-ui-react';
import {ProjectPagination} from '../components-ui/ProjectPagination';
import {GATEWAY_LIMIT} from '../const';
import {fetchGatewayApplications, removeGatewayApplication} from "../actions/gateways";
import {GatewayApplicationRemove} from "./GatewayApplicationRemove";


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

    renderApplications() {
        const gateways = this.props.gateways;

        return gateways.list.map((gateway, index) => {

            return (
                <Card fluid key={index}>
                    <Card.Content>
                        <div className="meta">
                            Gateway Name: {gateway.name}
                        </div>
                    </Card.Content>
                    <Card.Content extra>
                        <div className='ui two buttons'>
                            <Button basic color='green'>
                                Add Wallet
                            </Button>
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
        removeGatewayApplication
    }
)(GatewayApplicationList);
