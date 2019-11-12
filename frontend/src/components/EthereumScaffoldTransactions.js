import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Button, Segment} from 'semantic-ui-react';
import {getTransactionSelector} from '../selectors/getTransactionSelector';
import {addMoreEthereumScaffoldTransactionsFromApi} from '../actions/ethereum-scaffold-transactions';
import {TransactionEvent} from './TransactionEvent';

class EthereumScaffoldTransactionsComponent extends Component {
  loadMore = () => {
    this.props.actions.fetchTransactions(this.props.transactions.list.length);
  };

  renderEventList = () =>
    this.props.transactions.list.map(({ event, date }, index) => (
      <Segment key={index} attached>
        <TransactionEvent event={event} date={date} />
      </Segment>
    ));

  renderPagination = () => {
    const { totalCount, limit } = this.props.transactions;
    if (totalCount <= limit) {
      return null;
    }
    return (
      <Button fluid attached="bottom" onClick={this.loadMore}>
        Load More
      </Button>
    );
  };

  render() {
    const { totalCount } = this.props.transactions;
    if (!totalCount) {
      return null;
    }
    return (
      <React.Fragment>
        <Segment attached="top" as="h5">
          Scaffold Events
        </Segment>
        {this.renderEventList()}
        {this.renderPagination()}
      </React.Fragment>
    );
  }
}

const mapStateToProps = (state, { scaffold }) => ({ transactions: getTransactionSelector(state, scaffold.address) });

const mapDispatchToProps = (dispatch, { scaffold }) => ({
  actions: bindActionCreators(
    { fetchTransactions: offset => addMoreEthereumScaffoldTransactionsFromApi(scaffold, offset) },
    dispatch
  )
});

export const ScaffoldTransaction = connect(
  mapStateToProps,
  mapDispatchToProps
)(EthereumScaffoldTransactionsComponent);
