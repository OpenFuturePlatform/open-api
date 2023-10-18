import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Card, Divider, Grid, Icon} from 'semantic-ui-react';
import {ProjectPagination} from '../components-ui/ProjectPagination';
import {GATEWAY_LIMIT} from '../const';
import {
  fetchGatewayApplications,
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
                <div className="meta" style={{display: "inline-flex", width: "100%"}}>
                  <div className="webhook" style={{width: "100%"}}>
                  Webhook: {gateway.webHook}
                  </div>
                  <div className='ui buttons'>
                    <Button basic color='red'>
                      <GatewayApplicationRemove gateway={gateway} onSubmit={() => this.onRemoveGateway(gateway)}/>
                    </Button>
                  </div>
                </div>

              </Grid.Column>
            </Grid.Row>
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
          <ProjectPagination limit={GATEWAY_LIMIT} totalCount={gateways.totalCount}
                             onChange={this.fetchGatewayApplications}/>
        </Grid.Column>
      </Grid.Row>
    );
  }
}

const mapStateToProps = ({gateways}) => ({gateways: gateways});

export default connect(
  mapStateToProps,
  {
    fetchGatewayApplications,
    removeGatewayApplication
  }
)(GatewayApplicationList);
