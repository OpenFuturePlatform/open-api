import React, {Component} from 'react';
import {Card, Grid} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {ScaffoldStatusContainer} from '../components/EthereumScaffoldStatus';
import {editEthereumScaffold, fetchEthereumScaffoldDetails} from '../actions/scaffolds';
import {EthereumShareHolders} from '../components/EthereumShareHolders';
import {WalletSelect} from '../components/WalletSelect';
import {EthereumScaffoldEdit} from '../components/EthereumScaffoldEdit';
import {ScaffoldTransaction} from '../components/EthereumScaffoldTransactions';
import {WordWrap} from '../components-ui/WordWrap';
import {subscribeEthAccount, unsubscribeEthAccount} from '../actions/eth-account';
import {subscribeTransactionsByApi, unsubscribeTransactionsByApi} from '../actions/ethereum-scaffold-transactions';
import {EtherscanLink} from '../components-ui/EtherscanLink';

class EthereumScaffoldSummary extends Component {
  async componentDidMount() {
    const scaffoldAddress = this.getScaffoldAddress();
    await this.props.actions.subscribeEthAccount();
    await this.props.actions.fetchEthereumScaffoldDetails(scaffoldAddress);
    this.props.actions.subscribeTransactionsByApi(scaffoldAddress);
  }

  componentDidUpdate(prevProps) {
    const { byApiMethod } = this.props;
    const byApiMethodChanged = prevProps.byApiMethod !== byApiMethod;

    if (byApiMethodChanged) {
      const scaffoldAddress = this.getScaffoldAddress();
      this.props.actions.fetchEthereumScaffoldDetails(scaffoldAddress);
    }
  }

  componentWillUnmount() {
    this.props.actions.unsubscribeEthAccount();
    this.props.actions.unsubscribeTransactionsByApi();
  }

  getScaffoldAddress = () => this.props.match.params.scaffoldAddress;

  onEditScaffold = fields => this.props.actions.editEthereumScaffold(this.props.scaffold, fields);

  render() {
    const { scaffold, summary, error } = this.props;

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
                  <ScaffoldStatusContainer scaffold={scaffold} summary={summary || {}} error={error} />
                </Card.Content>
                <Card.Content>
                  <div>
                    Scaffold Title: <WordWrap>{scaffold.title}</WordWrap>{' '}
                    <EthereumScaffoldEdit scaffold={scaffold} onSubmit={this.onEditScaffold} />
                  </div>
                  <div>
                    Scaffold Address: <EtherscanLink>{scaffold.address}</EtherscanLink>
                  </div>
                  <div>
                    Scaffold Owner Address: <EtherscanLink>{scaffold.developerAddress}</EtherscanLink>
                  </div>
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
        <EthereumShareHolders scaffold={scaffold} />
        <ScaffoldTransaction scaffold={scaffold} />
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
  actions: bindActionCreators(
    {
      fetchEthereumScaffoldDetails,
      editEthereumScaffold,
      subscribeEthAccount,
      unsubscribeEthAccount,
      subscribeTransactionsByApi,
      unsubscribeTransactionsByApi
    },
    dispatch
  )
});

export const ScaffoldSummaryContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(EthereumScaffoldSummary);
