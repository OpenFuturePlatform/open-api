import React, {Component} from 'react';
import {Card, Grid} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {fetchScaffoldItem} from "../actions/index";
import {ScaffoldStatusContainer} from '../components/ScaffoldStatus';

class ScaffoldSummary extends Component {

  componentDidMount() {
    const scaffoldAddress = this.getScaffoldAddress();
    this.props.actions.fetchScaffoldItem(scaffoldAddress);
  }

  getScaffoldAddress() {
    return this.props.match.params.scaffoldAddress;
  }

  render() {
    const scaffoldAddress = this.getScaffoldAddress();
    const {onchainScaffoldSummary} = this.props;

    if (!onchainScaffoldSummary) {
      return null;
    }

    return (
      <div style={{marginTop: '20px'}}>
        <Grid>
          <Grid.Row>
            <Grid.Column width={16}>
              <Card fluid>
                <Card.Content header="On-chain Scaffold Summary" meta="This data is coming from the Ethereum Network"/>
                <Card.Content>
                  {onchainScaffoldSummary.tokenBalance} tokens
                  <ScaffoldStatusContainer scaffoldAddress={scaffoldAddress} status={onchainScaffoldSummary.enabled} />
                </Card.Content>
                <Card.Content>
                  <div>Scaffold Description: {onchainScaffoldSummary.description}</div>
                  <div>Scaffold Owner Address: {onchainScaffoldSummary.vendorAddress}</div>
                </Card.Content>
                <Card.Content>
                  <div>
                    <div style={{width: '64%', display: 'inline-block'}}>
                      Scaffold Amount: {(Number.parseFloat(onchainScaffoldSummary.amount)).toFixed(5)} ether
                    </div>
                    <div style={{width: '34%', display: 'inline-block'}}>
                      {onchainScaffoldSummary.fiatAmount} {onchainScaffoldSummary.fiatCurrency}
                    </div>
                  </div>
                  <div>Scaffold Transactions: {onchainScaffoldSummary.transactionIndex}</div>
                </Card.Content>
              </Card>
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            <Grid.Column/>
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = ({onchainScaffoldSummary}) => ({onchainScaffoldSummary});

const mapDispatchToProps = dispatch => ({actions: bindActionCreators({fetchScaffoldItem}, dispatch)});

export const ScaffoldSummaryContainer = connect(mapStateToProps, mapDispatchToProps)(ScaffoldSummary);
