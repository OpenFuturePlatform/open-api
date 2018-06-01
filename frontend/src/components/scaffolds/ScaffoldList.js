import React, {Component} from 'react';
import {connect} from 'react-redux';
import {fetchScaffolds} from '../../actions';
import {Card, Divider, Grid, Pagination} from 'semantic-ui-react';
import {Link} from 'react-router-dom';

const ETHEREUM_NETWORK = process.env.REACT_APP_ETHEREUM_NETWORK;
const LIMIT = 10;

class ScaffoldList extends Component {
  componentDidMount() {
    this.fetchScaffolds(1);
  }

  fetchScaffolds(page = 1, limit = LIMIT) {
    this.props.fetchScaffolds(page, limit);
  }

  renderScaffolds() {
    const scaffolds = this.props.scaffolds;

    return scaffolds.list.map((scaffold, index) => {
      const scaffoldData = scaffold;
      return (
        <Card fluid key={index}>
          <Card.Content>
            <Link
              to={`scaffolds/${
                scaffold.address
                }`}
            >
              <Card.Header>
                {scaffoldData.description}
              </Card.Header>
            </Link>
            <div className="meta">
              Scaffold Address:{' '}
              <a
                href={`https://${ETHEREUM_NETWORK}/address/${
                  scaffold.address
                  }`}
                target="_blank"
              >
                {scaffold.address}
              </a>
            </div>
          </Card.Content>
          <Card.Content>
            <Grid>
              <Grid.Row>
                <Grid.Column width={16}>
                  Developer Address:{' '}
                  {scaffold.developerAddress}
                </Grid.Column>
              </Grid.Row>
              <Grid.Row>
                <Grid.Column width={4}>
                  Fiat Amount: {scaffoldData.fiatAmount}
                </Grid.Column>
                <Grid.Column width={4}>
                  Currecy: {scaffoldData.currency}
                </Grid.Column>
                <Grid.Column width={8}>
                  Scaffold Amount:{' '}
                  {scaffoldData.conversionAmount} Ether
                </Grid.Column>
              </Grid.Row>
            </Grid>
            <Divider/>
            <Grid>
              {scaffold.properties.map((field, index) => {
                return (
                  <Grid.Row key={'property-' + index}>
                    <Grid.Column width={8}>
                      Property Name: {field.name}
                    </Grid.Column>
                    <Grid.Column width={8}>
                      Datatype: {field.type}
                    </Grid.Column>
                  </Grid.Row>
                );
              })}
            </Grid>
          </Card.Content>
        </Card>
      );
    });
  }

  renderPagination() {
    const scaffolds = this.props.scaffolds;
    const totalPages = Math.ceil(scaffolds.totalCount / LIMIT);

    if (totalPages <= 1) {
      return null;
    }

    return (
      <div style={{display: 'flex', justifyContent: 'center'}}>
        <Pagination defaultActivePage={1}
                    totalPages={totalPages}
                    onPageChange={(e, {activePage}) => this.fetchScaffolds(activePage)}/>
      </div>
    );
  }

  render() {
    return (
      <Grid.Row>
        <Grid.Column width={16}>
          {this.renderScaffolds()}
          {this.renderPagination()}
        </Grid.Column>
      </Grid.Row>
    );
  }
}

const mapStateToProps = ({scaffolds}) => ({scaffolds: scaffolds});

export default connect(mapStateToProps, {fetchScaffolds})(ScaffoldList);
