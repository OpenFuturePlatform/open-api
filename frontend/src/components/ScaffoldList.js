import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Card, Divider, Grid } from 'semantic-ui-react';
import { Link } from 'react-router-dom';
import { EtherscanLink } from '../components-ui/EtherscanLink';
import { fetchScaffolds } from '../actions/scaffolds';
import { ProjectPagination } from '../components-ui/ProjectPagination';
import { SCAFFOLDS_LIMIT } from '../const';

class ScaffoldList extends Component {
  componentDidMount() {
    this.fetchScaffolds();
  }

  fetchScaffolds = (offset = 0, limit = SCAFFOLDS_LIMIT) => {
    this.props.fetchScaffolds(offset, limit);
  };

  renderScaffolds() {
    const scaffolds = this.props.scaffolds;

    return scaffolds.list.map((scaffold, index) => {
      const scaffoldData = scaffold;
      return (
        <Card fluid key={index}>
          <Card.Content>
            <Link to={`scaffolds/${scaffold.address}`}>
              <Card.Header>{scaffoldData.description}</Card.Header>
            </Link>
            <div className="meta">
              Scaffold Address: <EtherscanLink>{scaffold.address}</EtherscanLink>
            </div>
          </Card.Content>
          <Card.Content>
            <Grid>
              <Grid.Row>
                <Grid.Column width={16}>
                  Developer Address: {scaffold.developerAddress || scaffold.vendorAddress}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column width={4}>Fiat Amount: {scaffoldData.fiatAmount}</Grid.Column>
                <Grid.Column width={4}>Currecy: {scaffoldData.currency}</Grid.Column>
                <Grid.Column width={8}>Scaffold Amount: {scaffoldData.conversionAmount} Ether</Grid.Column>
              </Grid.Row>
            </Grid>
            <Divider />
            <Grid>
              {scaffold.properties.map((field, index) => {
                return (
                  <Grid.Row key={'property-' + index}>
                    <Grid.Column width={8}>Property Name: {field.name}</Grid.Column>
                    <Grid.Column width={8}>Datatype: {field.type}</Grid.Column>
                  </Grid.Row>
                );
              })}
            </Grid>
          </Card.Content>
        </Card>
      );
    });
  }

  render() {
    const scaffolds = this.props.scaffolds;
    return (
      <Grid.Row>
        <Grid.Column width={16}>
          {this.renderScaffolds()}
          <ProjectPagination limit={SCAFFOLDS_LIMIT} totalCount={scaffolds.totalCount} onChange={this.fetchScaffolds} />
        </Grid.Column>
      </Grid.Row>
    );
  }
}

const mapStateToProps = ({ scaffolds }) => ({ scaffolds: scaffolds });

export default connect(
  mapStateToProps,
  { fetchScaffolds }
)(ScaffoldList);
