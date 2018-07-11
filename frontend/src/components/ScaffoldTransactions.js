import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Segment } from 'semantic-ui-react';
import { getTransactionSelector } from '../selectors/getTransactionSelector';
import { fetchScaffoldTransactionsFromApi } from '../actions/scaffold-transactions';
import { ProjectPagination } from '../components-ui/ProjectPagination';
import { TRANSACTIONS_LIMIT } from '../const/index';
import { TransactionEvent } from './TransactionEvent';

class ScaffoldTransactionsComponent extends Component {
  componentDidMount() {
    // this.fetchTransactions();
  }

  fetchTransactions = (offset, limit) => {
    this.props.actions.fetchTransactions(offset, limit);
  };

  renderEventList = () =>
    this.props.transactions.list.map(({ event, date }, index) => (
      <Segment key={index} attached>
        <TransactionEvent event={event} date={date} />
      </Segment>
    ));

  renderPagination = () => {
    const { totalCount } = this.props.transactions;
    if (totalCount <= TRANSACTIONS_LIMIT) {
      return null;
    }
    return (
      <Segment attached="bottom">
        <ProjectPagination
          totalCount={totalCount}
          limit={TRANSACTIONS_LIMIT}
          onChange={this.fetchTransactions}
          size="mini"
        />
      </Segment>
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
    { fetchTransactions: (...paginationSet) => fetchScaffoldTransactionsFromApi(scaffold.address, ...paginationSet) },
    dispatch
  )
});

export const ScaffoldTransaction = connect(
  mapStateToProps,
  mapDispatchToProps
)(ScaffoldTransactionsComponent);
