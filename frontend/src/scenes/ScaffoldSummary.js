import React, { Component } from 'react';
import { Card, Grid } from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { ScaffoldStatusContainer } from '../components/ScaffoldStatus';
import { fetchScaffoldSummary, editScaffold } from '../actions/scaffolds';
import { ShareHolders } from '../components/ShareHolders';
import { WalletSelect } from '../components/WalletSelect';
import { ScaffoldEdit } from '../components/ScaffoldEdit';
import { WordWrap } from '../components-ui/WordWrap';

class ScaffoldSummary extends Component {
  componentDidMount() {
    const scaffoldAddress = this.getScaffoldAddress();
    this.props.actions.fetchScaffoldSummary(scaffoldAddress);
  }

  componentDidUpdate(prevProps) {
    const { byApiMethod } = this.props;
    const byApiMethodChanged = prevProps.byApiMethod !== byApiMethod;

    if (byApiMethodChanged) {
      const scaffoldAddress = this.getScaffoldAddress();
      this.props.actions.fetchScaffoldSummary(scaffoldAddress);
    }
  }

  getScaffoldAddress = () => this.props.match.params.scaffoldAddress;

  onEditScaffold = fields => this.props.actions.editScaffold(this.props.scaffold, fields);

  render() {
    const { address, scaffold, summary, error } = this.props;

    if (!scaffold) {
      return null;
    }

    return (
      <div style={{ marginTop: '20px' }}>
        <WalletSelect />
        <Grid>
          <Grid.Row>
            <Grid.Column width={16}>
              <Card fluid>
                <Card.Content header="On-chain Scaffold Summary" meta="This data is coming from the Ethereum Network" />
                <Card.Content>
                  <ScaffoldStatusContainer
                    scaffoldAddress={address}
                    abi={scaffold.abi}
                    summary={summary || {}}
                    error={error}
                  />
                </Card.Content>
                <Card.Content>
                  <div>
                    Scaffold Description: <WordWrap>{scaffold.description}</WordWrap>{' '}
                    <ScaffoldEdit scaffold={scaffold} onSubmit={this.onEditScaffold} />
                  </div>
                  <div>Scaffold Owner Address: {scaffold.vendorAddress}</div>
                </Card.Content>
                <Card.Content>
                  <div>
                    <div style={{ width: '64%', display: 'inline-block' }}>
                      Scaffold Amount: {Number.parseFloat(scaffold.conversionAmount).toFixed(5)} ether
                    </div>
                    <div style={{ width: '34%', display: 'inline-block' }}>
                      {scaffold.fiatAmount} {scaffold.currency}
                    </div>
                  </div>
                  <div>Scaffold Transactions: {summary ? summary.transactionIndex : ''}</div>
                </Card.Content>
              </Card>
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            <Grid.Column />
          </Grid.Row>
        </Grid>
        <ShareHolders scaffold={scaffold} />
      </div>
    );
  }
}

const mapStateToProps = (
  { scaffoldsById, auth: { byApiMethod } },
  {
    match: {
      params: { scaffoldAddress }
    }
  }
) => {
  const scaffoldSet = scaffoldsById[scaffoldAddress] || {};
  return { byApiMethod, ...scaffoldSet };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({ fetchScaffoldSummary, editScaffold }, dispatch)
});

export const ScaffoldSummaryContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ScaffoldSummary);
