import React, {Component} from 'react';
import {Card, Grid} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {ScaffoldStatusContainer} from '../components/ScaffoldStatus';
import {fetchScaffoldSummary} from '../actions/scaffolds';
import {ShareHolders} from '../components/ShareHolders';
import {WalletSelect} from '../components/WalletSelect';

class ScaffoldSummary extends Component {

  componentDidMount() {
    const scaffoldAddress = this.getScaffoldAddress();
    this.props.actions.fetchScaffoldSummary(scaffoldAddress);
  }

  componentDidUpdate(prevProps) {
    const {byApiMethod} = this.props;
    const byApiMethodChanged = prevProps.byApiMethod !== byApiMethod;

    if (byApiMethodChanged) {
      const scaffoldAddress = this.getScaffoldAddress();
      this.props.actions.fetchScaffoldSummary(scaffoldAddress);
    }
  }

  getScaffoldAddress() {
    return this.props.match.params.scaffoldAddress;
  }

  render() {
    const {address, scaffold, summary, error, byApiMethod} = this.props;

    if (!scaffold) {
      return null;
    }

    return (
      <div style={{marginTop: '20px'}}>
        <WalletSelect/>
        <Grid>
          <Grid.Row>
            <Grid.Column width={16}>
              <Card fluid>
                <Card.Content header="On-chain Scaffold Summary" meta="This data is coming from the Ethereum Network"/>
                <Card.Content>
                  <ScaffoldStatusContainer scaffoldAddress={address} summary={summary || {}} error={error}/>
                </Card.Content>
                <Card.Content>
                  <div>Scaffold Description: {scaffold.description}</div>
                  <div>Scaffold Owner Address: {scaffold.vendorAddress}</div>
                </Card.Content>
                <Card.Content>
                  <div>
                    <div style={{width: '64%', display: 'inline-block'}}>
                      Scaffold Amount: {(Number.parseFloat(scaffold.conversionAmount)).toFixed(5)} ether
                    </div>
                    <div style={{width: '34%', display: 'inline-block'}}>
                      {scaffold.fiatAmount} {scaffold.currency}
                    </div>
                  </div>
                  <div>Scaffold Transactions: {summary ? summary.transactionIndex : ''}</div>
                </Card.Content>
              </Card>
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            <Grid.Column/>
          </Grid.Row>
        </Grid>
        {!byApiMethod && <ShareHolders scaffold={{...scaffold}}/>}
      </div>
    );
  }
}

const mapStateToProps = ({ScaffoldsById, auth: {byApiMethod}}, {match: {params: {scaffoldAddress}}}) => {
  const scaffoldSet = ScaffoldsById[scaffoldAddress] || {};
  return ({byApiMethod, ...scaffoldSet});
};

const mapDispatchToProps = dispatch => ({actions: bindActionCreators({fetchScaffoldSummary}, dispatch)});

export const ScaffoldSummaryContainer = connect(mapStateToProps, mapDispatchToProps)(ScaffoldSummary);
