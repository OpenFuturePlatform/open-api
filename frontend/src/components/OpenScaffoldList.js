import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, Grid} from 'semantic-ui-react';
import {fetchOpenScaffolds} from '../actions/scaffolds';
import {ProjectPagination} from '../components-ui/ProjectPagination';
import {SCAFFOLDS_LIMIT} from '../const';
import {WordWrap} from '../components-ui/WordWrap';

class OpenScaffoldList extends Component {
  componentDidMount() {
    this.fetchOpenScaffolds();
  }

  fetchOpenScaffolds = (offset = 0, limit = SCAFFOLDS_LIMIT) => {
    this.props.fetchOpenScaffolds(offset, limit);
  };

  renderScaffolds() {
    const scaffolds = this.props.openScaffolds;

    return scaffolds && scaffolds.list && scaffolds.list.map((scaffold, index) => {
      return (
        <Card fluid key={index}>
          <Card.Content>
            <Card.Header>
              <WordWrap>{scaffold.description}</WordWrap>
            </Card.Header>
          </Card.Content>
          <Card.Content>
            <Grid>
              <Grid.Row>
                <Grid.Column width={16}>
                  Developer Address: {scaffold.developerAddress}
                </Grid.Column>
              </Grid.Row>
            </Grid>
          </Card.Content>
        </Card>
      );
    });
  }

  render() {
    const scaffolds = this.props.openScaffolds;
    return (
      <Grid.Row>
        <Grid.Column width={16}>
          {this.renderScaffolds()}
          <ProjectPagination limit={SCAFFOLDS_LIMIT} totalCount={scaffolds.totalCount}
                             onChange={this.fetchOpenScaffolds}/>
        </Grid.Column>
      </Grid.Row>
    );
  }
}

const mapStateToProps = ({openScaffolds}) => ({openScaffolds: openScaffolds});

export default connect(
  mapStateToProps,
  {fetchOpenScaffolds}
)(OpenScaffoldList);
