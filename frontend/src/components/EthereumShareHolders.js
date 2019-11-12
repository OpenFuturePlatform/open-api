import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import 'react-table/react-table.css';
import '../css/table.css';
import {EtherscanLink} from '../components-ui/EtherscanLink';
import {Segment} from 'semantic-ui-react';
import {
  addEthereumShareHolder,
  editEthereumShareHolder,
  removeEthereumShareHolder
} from '../actions/ethereum-share-holders';
import {EthereumShareHolderSave} from './EthereumShareHolderSave';
import {Table} from '../components-ui/Table';
import {EthereumShareHolderRemove} from './EthereumShareHolderRemove';
import {getShareHoldersSelector} from '../selectors/getShareHoldersSelector';
import {getApiUsing} from '../selectors/getApiUsing';
import {getCriticalMetaMaskError} from '../selectors/getMetaMaskError';

const getColumns = (allHolders, onEdit, onRemove) => [
  {
    Header: 'Share Holder Address',
    accessor: 'address',
    Cell: ({ value }) => <EtherscanLink>{value}</EtherscanLink>,
    sortable: false
  },
  {
    Header: 'Holder Share',
    accessor: 'share',
    width: 200,
    Cell: ({ value }) => <span>{value}%</span>,
    sortable: false
  },
  {
    Header: '',
    accessor: 'address',
    width: 150,
    Cell: ({ value, original }) => (
      <span>
        <EthereumShareHolderSave
          editType
          shareHolder={original}
          allHolders={allHolders}
          onSubmit={devShare => onEdit(devShare)}
        />{' '}
        <EthereumShareHolderRemove onSubmit={() => onRemove(value)} />
      </span>
    ),
    sortable: false
  }
];

export class ShareHoldersComponent extends Component {
  onAddShareHolder = shareHolder => {
    const { scaffold } = this.props;
    return this.props.actions.addShareHolder(scaffold, shareHolder);
  };

  onEditShareHolder = shareHolder => {
    const { scaffold } = this.props;
    return this.props.actions.editShareHolder(scaffold, shareHolder);
  };

  onRemoveShareHolder = holderAddress => {
    const { scaffold } = this.props;
    return this.props.actions.removeShareHolder(scaffold, holderAddress);
  };

  render() {
    const { shareHolders, apiUsing, metaMaskError } = this.props;
    const columns = getColumns(shareHolders, this.onEditShareHolder, this.onRemoveShareHolder);
    const noDataText = !apiUsing && metaMaskError ? metaMaskError : undefined;

    return (
      <div className="table-with-add">
        <EthereumShareHolderSave onSubmit={this.onAddShareHolder} allHolders={shareHolders} />
        <Segment attached styles={{ padding: 0 }}>
          <Table data={shareHolders} columns={columns} noDataText={noDataText} />
        </Segment>
      </div>
    );
  }
}

const mapStateToProps = (state, { scaffold }) => {
  const shareHolders = getShareHoldersSelector(state, scaffold.address);
  const apiUsing = getApiUsing(state);
  const metaMaskError = getCriticalMetaMaskError(state);
  return { scaffold, shareHolders, apiUsing, metaMaskError };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {
      addShareHolder: addEthereumShareHolder,
      editShareHolder: editEthereumShareHolder,
      removeShareHolder: removeEthereumShareHolder
    },
    dispatch
  )
});

export const EthereumShareHolders = connect(
  mapStateToProps,
  mapDispatchToProps
)(ShareHoldersComponent);
